package corona.financiero.nmda.admision.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import corona.financiero.nmda.admision.adapter.EquifaxAdapter;
import corona.financiero.nmda.admision.dto.equifax.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ScoringService {
    @Autowired
    private EquifaxAdapter equifaxAdapter;

    @Autowired
    private ConsultaScoringRepository consultaScoringRepository;

    @Autowired
    private ConsultaScoringRegistroMorosidadProtestoRepository consultaScoringRegistroMorosidadProtestoRepository;

    @Autowired
    private ConsultaScoringPersonaRepository consultaScoringPersonaRepository;

    @Autowired
    private ProspectoRepository prospectoRepository;

    @Autowired
    private ResumenMorosidadesProtestosImpagosRepository resumenMorosidadesProtestosImpagosRepository;

    private static final int MES_VIGENCIA = 1;

    private static final int DIAS_VIGENCIA = 29;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    private static final String FECHA_INVALIDA = "0000-00-00";

    private static final int EDAD_MINIMA = 28;

    private static final int EDAD_MAXIMA = 73;

    private static final int PROTESTO_CLIENTE = 0;

    private static final int MORA_CLIENTE = 0;

    private static final long SCORE_MINIMO = 500;
    private static final long SCORE_MEDIO = 600;
    private static final long SCORE_MAXIMO = 700;


    @Autowired
    private Validaciones validaciones;

    private ConsultaScoringEntity consultaScoringLocal(String rut) {
        log.debug("Consultando scoring local...");
        List<ConsultaScoringEntity> consultaScoringEntityList = consultaScoringRepository.buscarRegistroEquifaxVigente(rut);

        List<ConsultaScoringEntity> listaInvalidar = new ArrayList<>();
        ConsultaScoringEntity retorno = null;
        if (consultaScoringEntityList != null && !consultaScoringEntityList.isEmpty()) {
            for (ConsultaScoringEntity c : consultaScoringEntityList) {
                LocalDate now = LocalDate.now();

                Period period = Period.between(c.getFechaConsulta(), now);

                if (period.getMonths() >= MES_VIGENCIA || period.getDays() >= DIAS_VIGENCIA) {
                    log.debug("Invalidando scoring por tiempo transcurrido");
                    c.setVigencia(false);
                    c.setFechaModificacion(LocalDateTime.now());
                    c.setUsuarioModificacion(USUARIO_TEMPORAL);
                    listaInvalidar.add(c);
                    continue;
                }
                retorno = c;
            }
        }

        //se invalidan todas las que no cumplan
        if (!listaInvalidar.isEmpty()) {
            log.debug("Invalidando scoring anteriores");
            consultaScoringRepository.saveAll(listaInvalidar);
            consultaScoringRepository.flush();
        }
        return retorno;
    }

    @SuppressWarnings("java:S3776")
    public Long recopilarInformacionScoring(String rut) {
        log.debug("Rut scoringg: {}", rut);

        ConsultaScoringEntity consultaScoringEntity = consultaScoringLocal(rut);

        if (consultaScoringEntity == null) {
            log.debug("No existen datos, se debe consumir servicio equifax...");
            ProspectoEntity prospectoEntity = null;
            prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rut);

            if (prospectoEntity == null) {
                throw new NoContentException();
            }


            EquifaxRequestDTO requestDTO = new EquifaxRequestDTO();
            requestDTO.setRut(rut);
            Map<String, Object> responseEquifax = equifaxAdapter.infoProspectoEquifax(requestDTO);

            Map<String, Object> errores = (Map<String, Object>) responseEquifax.get("errores");

            if (errores != null) {
                log.debug("Se detectaron errores en consulta informe equifax");
                return Constantes.REGLA_PROBLEMA_SERVICIO_SCORING;
            }

            Map<String, Object> identificacionPersona = (Map<String, Object>) responseEquifax.get("identificacionPersona");
            ObjectMapper identificacionPersonaMapper = new ObjectMapper();
            EquifaxIdentificacionPersonaResponseDTO identificacionPersonaResponseDTO = identificacionPersonaMapper.convertValue(identificacionPersona, EquifaxIdentificacionPersonaResponseDTO.class);

            Map<String, Object> registroMorosidadesYProtestos = (Map<String, Object>) responseEquifax.get("registroMorosidadesYProtestos");
            ObjectMapper registroMorosidadesYProtestosMapper = new ObjectMapper();
            EquifaxRegistroMorosidadProtestoResponseDTO registroMorosidadProtestoResponseDTO = registroMorosidadesYProtestosMapper.convertValue(registroMorosidadesYProtestos, EquifaxRegistroMorosidadProtestoResponseDTO.class);


            Map<String, Object> resumenMorosidadesYProtestosImpagos = (Map<String, Object>) responseEquifax.get("resumenMorosidadesYProtestosImpagos");

            ObjectMapper resumenMorosidadesYProtestosImpagosMapper = new ObjectMapper();
            EquifaxResumenMorosidadesProtestosImpagosResponseDTO resumenMorosidadesProtestosImpagosResponseDTO = resumenMorosidadesYProtestosImpagosMapper.convertValue(resumenMorosidadesYProtestosImpagos, EquifaxResumenMorosidadesProtestosImpagosResponseDTO.class);


            if (identificacionPersonaResponseDTO.getApellidoPaterno() != null) {
                prospectoEntity.setApellidoPaterno(identificacionPersonaResponseDTO.getApellidoPaterno());
            }

            if (identificacionPersonaResponseDTO.getApellidoMaterno() != null) {
                prospectoEntity.setApellidoMaterno(identificacionPersonaResponseDTO.getApellidoMaterno());
            }

            if (identificacionPersonaResponseDTO.getNombres() != null) {
                prospectoEntity.setNombres(identificacionPersonaResponseDTO.getNombres());
            }

            if (identificacionPersonaResponseDTO.getFechaNacimiento() != null && !identificacionPersonaResponseDTO.getFechaNacimiento().equalsIgnoreCase(FECHA_INVALIDA)) {
                prospectoEntity.setFechaNacimiento(validaciones.convertirStringALocaldate(identificacionPersonaResponseDTO.getFechaNacimiento()));
            }

            //TODO se deja comentado evaluacion scoring, porque servicio equifax funciona solo con algunos rut
            //int valorScore = equifaxAdapter.infoScoreEquifax(rut);
            int valorScore = 700;
            if (valorScore == Constantes.ERROR_SCORING) {
                log.debug("Se detectaron errores en consulta scoring equifax");
                return Constantes.REGLA_PROBLEMA_SERVICIO_SCORING;
            }

            prospectoEntity.setFechaModificacion(LocalDateTime.now());
            prospectoEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
            prospectoRepository.save(prospectoEntity);
            prospectoRepository.flush();

            consultaScoringEntity = new ConsultaScoringEntity();


            consultaScoringEntity.setScore((long) valorScore);

            consultaScoringEntity.setFechaConsulta(LocalDate.now());
            consultaScoringEntity.setProspectoEntity(prospectoEntity);
            consultaScoringEntity.setVigencia(true);
            consultaScoringEntity.setFechaRegistro(LocalDateTime.now());
            consultaScoringEntity.setUsuarioRegistro(USUARIO_TEMPORAL);

            log.debug("guardando scoring...");

            consultaScoringRepository.save(consultaScoringEntity);
            log.debug("score guardado!");


            log.debug("guardando scoring persona...");
            ConsultaScoringPersonaEntity consultaScoringPersonaEntity = mapeaRespuestaScoringPersonaAEntidad(identificacionPersonaResponseDTO);
            consultaScoringPersonaEntity.setConsultaScoringEntity(consultaScoringEntity);
            consultaScoringPersonaRepository.save(consultaScoringPersonaEntity);
            log.debug("datos persona guardado!");

            log.debug("Guardando scoring morosidades...");
            ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = mapeaRespuestaScoringMorosidadAEntidad(registroMorosidadProtestoResponseDTO);
            consultaScoringRegistroMorosidadProtestoEntity.setConsultaScoringEntity(consultaScoringEntity);
            consultaScoringRegistroMorosidadProtestoRepository.save(consultaScoringRegistroMorosidadProtestoEntity);
            log.debug("registros scoringguardado!");

            log.debug("Guardando resumen morosidades protestos impagos...");
            ResumenMorosidadesProtestosImpagosEntity resumenMorosidadesProtestosImpagosEntity = mapeaResumenMorosidadesProtestosEntity(resumenMorosidadesProtestosImpagosResponseDTO);
            resumenMorosidadesProtestosImpagosEntity.setConsultaScoringEntity(consultaScoringEntity);
            resumenMorosidadesProtestosImpagosRepository.save(resumenMorosidadesProtestosImpagosEntity);
            log.debug("Datos resumen morosidades guaerdados!");
            log.debug("Fin proceso de guardar respuesta equifax en BD...");
        }

        long resultadoEvaluacion = evaluacionScoring(rut);
        if (resultadoEvaluacion == Constantes.REGLA_PROBLEMA_SERVICIO_SCORING) {
            //se debe invalidar los registros de scoring
            consultaScoringEntity.setFechaModificacion(LocalDateTime.now());
            consultaScoringEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
            consultaScoringEntity.setVigencia(false);
            consultaScoringRepository.save(consultaScoringEntity);
            consultaScoringRepository.flush();
        }
        return resultadoEvaluacion;

    }

    private long evaluacionScoring(String rut) {
        ConsultaScoringEntity consultaScoringEntity = consultaScoringLocal(rut);
        log.debug("Evaluando resultado scoring...");
        if (consultaScoringEntity == null) {
            log.debug("cai en evaluacion 1");
            return Constantes.REGLA_PROBLEMA_SERVICIO_SCORING;
        }

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity);

        long retorno = 0;
        if (consultaScoringPersonaEntity == null) {
            log.debug("Problema con servicio de scoring");
            return Constantes.REGLA_PROBLEMA_SERVICIO_SCORING;
        }

        if (consultaScoringPersonaEntity.getEdad() == null) {
            log.debug("Problema con datos de servicio de scoring");
            return Constantes.REGLA_PROBLEMA_SERVICIO_SCORING;
        }
        int edad = Integer.parseInt(consultaScoringPersonaEntity.getEdad());
        if (edad < EDAD_MINIMA) {
            log.debug("No cumple conedad minima");
            return Constantes.REGLA_EVALUACION_EDAD_MINIMA;
        }

        if (edad > EDAD_MAXIMA) {
            log.debug("No cumple con edad maxima");
            return Constantes.REGLA_EVALUACION_EDAD_MAXIMA;
        }

        long resultado = validarRangosScoring(edad, consultaScoringEntity.getScore());
        if (resultado > 0) {
            log.debug("No cumple con score requerido para rango de edad");
            return Constantes.REGLA_EVALUACION_SCORE_MINIMO;
        }


        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity);

        if (consultaScoringRegistroMorosidadProtestoEntity == null) {
            log.debug("Sin datos de protestos o morosidades");
            return retorno;
        }

        if (consultaScoringRegistroMorosidadProtestoEntity.getCantidadDocumentoBoletinProtestoImpagos() == null || consultaScoringRegistroMorosidadProtestoEntity.getCantidadDocumentoBoletinProtestoImpagos().trim().isEmpty()) {
            log.debug("Sin datos de protestos");
            return retorno;
        }

        int protestos = Integer.parseInt(consultaScoringRegistroMorosidadProtestoEntity.getCantidadDocumentoBoletinProtestoImpagos());
        if (protestos > PROTESTO_CLIENTE) {
            log.debug("No cumple con regla de protestos");
            return Constantes.REGLA_EVALUACION_PROTESTOS_CLIENTE;
        }

        if (consultaScoringRegistroMorosidadProtestoEntity.getCantidadMorososComercio() == null || consultaScoringRegistroMorosidadProtestoEntity.getCantidadMorososComercio().trim().isEmpty()) {
            log.debug("Sin datos de morosidades");
            return retorno;
        }

        int mora = Integer.parseInt(consultaScoringRegistroMorosidadProtestoEntity.getCantidadMorososComercio());
        if (mora > MORA_CLIENTE) {
            log.debug("No cumple con regla de mora");
            return Constantes.REGLA_EVALUACION_MORA_CLIENTE;
        }

        return retorno;

    }

    private long validarRangosScoring(int edad, long score) {
        if (edad >= EDAD_MINIMA && edad <= 30 && score < SCORE_MAXIMO) {
            return Constantes.REGLA_EVALUACION_SCORE_MINIMO;
        }

        if (edad >= 31 && edad <= 40 && score < SCORE_MEDIO) {
            return Constantes.REGLA_EVALUACION_SCORE_MINIMO;
        }

        if (edad >= 41 && edad <= EDAD_MAXIMA && score < SCORE_MINIMO) {
            return Constantes.REGLA_EVALUACION_SCORE_MINIMO;
        }

        return 0;
    }

    private ResumenMorosidadesProtestosImpagosEntity mapeaResumenMorosidadesProtestosEntity(EquifaxResumenMorosidadesProtestosImpagosResponseDTO request) {
        ResumenMorosidadesProtestosImpagosEntity resumenMorosidadesProtestosImpagosEntity = new ResumenMorosidadesProtestosImpagosEntity();
        resumenMorosidadesProtestosImpagosEntity.setAccesoCamaraComercio(request.isAccesoCamaraComercio());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentos12A24Meses(request.getCantidadDocumentos12A24Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentos6A12Meses(request.getCantidadDocumentos6A12Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosAcumulados12Meses(request.getCantidadDocumentosAcumulados12Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosAcumulados24Meses(request.getCantidadDocumentosAcumulados24Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosMasDe24Meses(request.getCantidadDocumentosMasDe24Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosAcumuladosUltimos6Meses(request.getCantidadDocumentosAcumuladosUltimos6Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosMasDe24Meses(request.getCantidadDocumentosMasDe24Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadDocumentosUltimos6Meses(request.getCantidadDocumentosUltimos6Meses());
        resumenMorosidadesProtestosImpagosEntity.setCantidadTotalImpagos(request.getCantidadTotalImpagos());
        if (request.getFechaVencimientoUltimoImpago() != null && !request.getFechaVencimientoUltimoImpago().equalsIgnoreCase(FECHA_INVALIDA))
            resumenMorosidadesProtestosImpagosEntity.setFechaVencimientoUltimoImpago(validaciones.convertirStringALocaldate(request.getFechaVencimientoUltimoImpago()));

        resumenMorosidadesProtestosImpagosEntity.setIndicadorErrorConectarseAlCSS(request.isIndicadorErrorConectarseAlCSS());
        resumenMorosidadesProtestosImpagosEntity.setIndicadorExistenciaInformacionParaSeccion(request.isIndicadorExistenciaInformacionParaSeccion());
        resumenMorosidadesProtestosImpagosEntity.setMontoDocumentos12A24Meses(request.getMontoDocumentos12A24Meses());
        resumenMorosidadesProtestosImpagosEntity.setMontoDocumentos6A12Meses(request.getMontoDocumentos6A12Meses());
        resumenMorosidadesProtestosImpagosEntity.setMontoDocumentosMasDe24Meses(request.getMontoDocumentosMasDe24Meses());
        resumenMorosidadesProtestosImpagosEntity.setMontoDocumentosUltimos6Meses(request.getMontoDocumentosUltimos6Meses());
        resumenMorosidadesProtestosImpagosEntity.setMontoTotalImpagos(request.getMontoTotalImpagos());
        resumenMorosidadesProtestosImpagosEntity.setMontoUltimoImpago(request.getMontoUltimoImpago());
        resumenMorosidadesProtestosImpagosEntity.setTipoDeudaUltimoImpago(request.getTipoDeudaUltimoImpago());

        return resumenMorosidadesProtestosImpagosEntity;
    }

    private ConsultaScoringPersonaEntity mapeaRespuestaScoringPersonaAEntidad(EquifaxIdentificacionPersonaResponseDTO request) {
        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = new ConsultaScoringPersonaEntity();

        consultaScoringPersonaEntity.setApellidoPaterno(request.getApellidoPaterno());
        consultaScoringPersonaEntity.setApellidoMaterno(request.getApellidoMaterno());
        consultaScoringPersonaEntity.setNombres(request.getNombres());
        consultaScoringPersonaEntity.setEdad(request.getEdad());

        if (request.getFechaNacimiento() != null && !request.getFechaNacimiento().equalsIgnoreCase(FECHA_INVALIDA))
            consultaScoringPersonaEntity.setFechaNacimiento(validaciones.convertirStringALocaldate(request.getFechaNacimiento()));
        consultaScoringPersonaEntity.setNombreCompleto(request.getNombre());
        return consultaScoringPersonaEntity;
    }

    private ConsultaScoringRegistroMorosidadProtestoEntity mapeaRespuestaScoringMorosidadAEntidad(EquifaxRegistroMorosidadProtestoResponseDTO request) {
        ConsultaScoringRegistroMorosidadProtestoEntity scoringMorosidad = new ConsultaScoringRegistroMorosidadProtestoEntity();

        scoringMorosidad.setCantidadDocumentoBoletinProtestoImpagos(request.getCantidadDocumentosBoletinProtestosEImpagos());
        scoringMorosidad.setCantidadDocumentoIcom(request.getCantidadDocumentosICOM());
        scoringMorosidad.setCantidadImpagosInformados(request.getCantidadImpagosInformados());
        scoringMorosidad.setCantidadMorososComercio(request.getCantidadMorososComercio());
        scoringMorosidad.setCantidadMultasInfgracLaboralPrevisional(request.getCantidadMultasEInfraccionesEnLaboralYPrevisional());
        scoringMorosidad.setMontoTotalImpago(request.getMontoTotalImpago());

        EquifaxMorosidadesBEDResponseDTO morosidadesBED = request.getMorosidadesBED();

        scoringMorosidad.setBedCodigoMoneda(morosidadesBED.getCodigoMoneda());
        if (morosidadesBED.getFechaIngresoEFX() != null)
            scoringMorosidad.setBedFechaIngresoEfx(validaciones.convertirStringALocaldate(morosidadesBED.getFechaIngresoEFX()));

        if (morosidadesBED.getFechaVencimiento() != null)
            scoringMorosidad.setBedFechaVencimiento(validaciones.convertirStringALocaldate(morosidadesBED.getFechaVencimiento()));


        scoringMorosidad.setBedJustificacionDescripcion(morosidadesBED.getJustificacionDescripcion());

        if (morosidadesBED.getJustificacionFecha() != null && !morosidadesBED.getJustificacionFecha().equalsIgnoreCase(FECHA_INVALIDA))
            scoringMorosidad.setBedJustificacionFecha(validaciones.convertirStringALocaldate(morosidadesBED.getJustificacionFecha()));
        scoringMorosidad.setBedMercadoCodigo(morosidadesBED.getMercadoCodigo());
        scoringMorosidad.setBedMercadoDescripcion(morosidadesBED.getMercadoDescripcion());
        scoringMorosidad.setBedMontoImpago(morosidadesBED.getMontoImpago());
        scoringMorosidad.setBedNombreLibrador(morosidadesBED.getNombreLibrador());
        scoringMorosidad.setBedNombreLocalidad(morosidadesBED.getNombreLocalidad());
        scoringMorosidad.setBedNroChequeOperacion(morosidadesBED.getNroChequeOperacion());
        scoringMorosidad.setBedTipoDeuda(morosidadesBED.getTipoDeuda());
        scoringMorosidad.setBedTipoDocumento(morosidadesBED.getTipoDocumento());

        EquifaxMorosidadesBolcomResponseDTO morosidadesBOLCOM = request.getMorosidadesBOLCOM();
        scoringMorosidad.setBolcomCodigoMoneda(morosidadesBOLCOM.getCodigoMoneda());

        if (morosidadesBOLCOM.getFechaIngreso() != null)
            scoringMorosidad.setBolcomFechaIngreso(validaciones.convertirStringALocaldate(morosidadesBOLCOM.getFechaIngreso()));

        if (morosidadesBOLCOM.getFechaVencimiento() != null)
            scoringMorosidad.setBolcomFechaVencimiento(validaciones.convertirStringALocaldate(morosidadesBOLCOM.getFechaVencimiento()));
        scoringMorosidad.setBolcomJustificacionDescripcion(morosidadesBOLCOM.getJustificacionDescripcion());

        if (morosidadesBOLCOM.getJustificacionFecha() != null)
            scoringMorosidad.setBolcomJustificacionFecha(validaciones.convertirStringALocaldate(morosidadesBOLCOM.getJustificacionFecha()));
        scoringMorosidad.setBolcomMercadoCodigo(morosidadesBOLCOM.getMercadoCodigo());
        scoringMorosidad.setBolcomMercadoDescripcion(morosidadesBOLCOM.getMercadoDescripcion());
        scoringMorosidad.setBolcomMontoImpago(morosidadesBOLCOM.getMontoImpago());
        scoringMorosidad.setBolcomNombreLibrador(morosidadesBOLCOM.getNombreLibrador());
        scoringMorosidad.setBolcomNombreLocalidad(morosidadesBOLCOM.getNombreLocalidad());
        scoringMorosidad.setBolcomNombreNotario(morosidadesBOLCOM.getNombreNotario());
        scoringMorosidad.setBolcomNroBoletin(morosidadesBOLCOM.getNroBoletin());
        scoringMorosidad.setBolcomNroChequeOperacion(morosidadesBOLCOM.getNroChequeOperacion());
        scoringMorosidad.setBolcomTipoCredito(morosidadesBOLCOM.getTipoCredito());
        scoringMorosidad.setBolcomTipoDeuda(morosidadesBOLCOM.getTipoDeuda());
        scoringMorosidad.setBolcomTipoDocumento(morosidadesBOLCOM.getTipoDocumento());
        scoringMorosidad.setBolcomTipoMotivo(morosidadesBOLCOM.getTipoMotivo());

        EquifaxMorosidadesIcomResponseDTO morosidadesICOM = request.getMorosidadesICOM();

        scoringMorosidad.setIcomCodigoMoneda(morosidadesICOM.getCodigoMoneda());

        if (morosidadesICOM.getFechaVencimiento() != null)
            scoringMorosidad.setIcomFechaVencimiento(validaciones.convertirStringALocaldate(morosidadesICOM.getFechaVencimiento()));
        scoringMorosidad.setIcomMercadoCodigo(morosidadesICOM.getMercadoCodigo());
        scoringMorosidad.setIcomMercadodDescripcion(morosidadesICOM.getMercadoDescripcion());
        scoringMorosidad.setIcomMontoImpago(morosidadesICOM.getMontoImpago());
        scoringMorosidad.setIcomNombreLibrador(morosidadesICOM.getNombreLibrador());
        scoringMorosidad.setIcomNombreLocalidad(morosidadesICOM.getNombreLocalidad());
        scoringMorosidad.setIcomNroChequeOperacion(morosidadesICOM.getNroChequeOperacion());
        scoringMorosidad.setIcomTipoCredito(morosidadesICOM.getTipoCredito());
        scoringMorosidad.setIcomTipoDeuda(morosidadesICOM.getTipoDeuda());
        scoringMorosidad.setIcomTipoDocumento(morosidadesICOM.getTipoDocumento());
        scoringMorosidad.setIcomTipoMotivo(morosidadesICOM.getTipoMotivo());

        return scoringMorosidad;
    }
}
