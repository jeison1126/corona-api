package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.RegistroProspectoResponseDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MotorReglasService {

    @Autowired
    private CotizacionPrevisionalDetalleRepository cotizacionPrevisionalDetalleRepository;

    @Autowired
    private EvaluacionProductoService evaluacionProductoService;

    private static final int RENTA_MINIMA = 450000;

    private static final int TRAMO_MINIMO = 450000;
    private static final int FACTOR_MINIMO = 20;

    private static final int TRAMO_MEDIO = 600000;
    private static final int FACTOR_MEDIO = 30;

    private static final int TRAMO_MAXIMO = 600001;
    private static final int FACTOR_MAXIMO = 40;

    private static final int CUPO_MAXIMO = 500000;

    private static final int DIAS_ENTRE_INTENTOS = 30;

    @Autowired
    private EstafaRepository estafaRepository;

    @Autowired
    private NoRecomendadosRepository noRecomendadosRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private CamaraComercioRepository camaraComercioRepository;

    @Autowired
    private CMFR04ValidacionFinancieraService validacionFinancieraService;

    @Autowired
    private ReglaNegocioRepository reglaNegocioRepository;

    @Autowired
    private AdmisionReglaNegocioRepository admisionReglaNegocioRepository;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    @Autowired
    private ScoringService scoringService;

    private static final long SIN_ERROR = 0;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    private static final int CANTIDAD_MINIMA_COTIZACIONES = 6;

    @Autowired
    private MotivoCartaRechazoRepository motivoCartaRechazoRepository;

    public RegistroProspectoResponseDTO reglasEvaluacionInterna(String rut, SolicitudAdmisionEntity solicitudAdmisionEntity) {
        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_INTERNA);
        log.debug("validando posibles causales de rechazo para prospecto {}", rut);
        List<ReglaNegocioEntity> reglaNegocioEntities = new ArrayList<>();
        //tabla estafa
        Optional<EstafaEntity> estafaEntityOptional = estafaRepository.findById(rut);

        if (estafaEntityOptional.isPresent()) {
            log.debug("Prospecto se encuentra en regla de estafa");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_ESTAFA);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }

        //tabla No Recomendados
        Optional<NoRecomendadosEntity> noRecomendadosEntityOptional = noRecomendadosRepository.findById(rut);
        if (noRecomendadosEntityOptional.isPresent()) {
            log.debug("Prospecto se encuentra en regla de no recomendados");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_NO_RECOMENDADOS);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }

        //tabla funcionarios
        Optional<FuncionariosEntity> funcionariosEntityOptional = funcionariosRepository.findById(rut);
        if (funcionariosEntityOptional.isPresent()) {
            log.debug("Prospecto se encuentra en regla de funcionario");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_FUNCIONARIO);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }


        //tabla camara comercio
        Optional<CamaraComercioEntity> camaraComercioEntityOptional = camaraComercioRepository.findById(rut);
        if (camaraComercioEntityOptional.isPresent()) {
            log.debug("Prospecto se encuentra en regla de camara de comercio");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_CAMARA_COMERCIO);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }

        //tabla r04
        if (validacionFinancieraService.isClienteMoroso(rut)) {
            log.debug("Prospecto se encuentra en regla de R04");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_R04);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }

        //validar cliente con rechazo por X cantidad de dias
        if (verificarIntentosAnterioresXDias(rut)) {
            log.debug("Prospecto se encuentra en regla de varias solicitudes dentro del rango de dias de exclusion");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_EVALUACION_DIAS_INTENTOS);
            reglaNegocioEntities.add(reglaNegocioEntity);
        }

        if (reglaNegocioEntities.isEmpty()) {
            return null;
        }


        log.debug("Procesar los errores (causales de rechazo) encontrados: {}", reglaNegocioEntities.size());
        List<AdmisionReglaNegocioEntity> admisionReglaNegocioEntities = reglaNegocioEntities.stream().map(r -> {
            AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
            admisionReglaNegocioEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
            admisionReglaNegocioEntity.setReglaNegocioEntity(r);
            admisionReglaNegocioEntity.setFechaRegistro(LocalDateTime.now());
            admisionReglaNegocioEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
            admisionReglaNegocioEntity.setVigencia(true);

            return admisionReglaNegocioEntity;

        }).collect(Collectors.toList());

        admisionReglaNegocioRepository.saveAll(admisionReglaNegocioEntities);
        admisionReglaNegocioRepository.flush();
        actualizarEstadoSolicitudAdmision(solicitudAdmisionEntity, Constantes.ESTADO_NO_APROBADO);

        RegistroProspectoResponseDTO response = new RegistroProspectoResponseDTO();
        response.setExito(false);
        response.setMensaje("Cliente no tiene oferta disponible");
        response.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());
        String textoCartaRechazo = "Cliente no cumple con las politicas internas necesarias";
        if (reglaNegocioEntities.size() == 1) {
            ParMensajeRechazoEntity parMensajeRechazoEntity = reglaNegocioEntities.get(0).getParMensajeRechazoEntity();
            if (parMensajeRechazoEntity.getMensajeFuncional() != null) {
                response.setMensaje(parMensajeRechazoEntity.getMensajeFuncional());
            }

            if (parMensajeRechazoEntity.getMensajeCartaRechazo() != null) {
                textoCartaRechazo = parMensajeRechazoEntity.getMensajeCartaRechazo();
            }

        }

        guardaMotivoCartaRechazo(solicitudAdmisionEntity, textoCartaRechazo);


        return response;
    }

    public boolean verificarIntentosAnterioresXDias(String rut) {

        LocalDate now = LocalDate.now();
        LocalDate desde = now.minusDays(DIAS_ENTRE_INTENTOS);
        boolean retorno = false;

        log.debug("Rut: {}",rut);
        log.debug("Fecha actual: {}",now);
        log.debug("Rango de fecha de inicio: {}",desde);

        List<SolicitudAdmisionEntity> solicitudAdmisionEntities = solicitudAdmisionRepository.verificarIntentosAnterioresXDias(rut, desde, now);
        log.debug("Solicitudes: {}",solicitudAdmisionEntities.size());

        if (solicitudAdmisionEntities != null && solicitudAdmisionEntities.size() > 1) {
            retorno = true;
            log.debug("existe");
        }

        return retorno;
    }

    public RegistroProspectoResponseDTO reglaEvaluacionScoringEquifax(String rutFormateado, SolicitudAdmisionEntity solicitudAdmisionEntity) {
        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_SCORING);

        long respuesta = scoringService.recopilarInformacionScoring(rutFormateado);


        if (respuesta == Constantes.REGLA_PROBLEMA_SERVICIO_SCORING) {
            log.debug("Se detecta errores en respuesta de servicio scoring, se invalida solicitud evaluacion (estado incompleto)");
            actualizarEstadoSolicitudAdmision(solicitudAdmisionEntity, Constantes.ESTADO_INCOMPLETO);
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(respuesta);
            actualizarAdmisionReglaNegocio(solicitudAdmisionEntity, reglaNegocioEntity);
            throw new BadRequestException(reglaNegocioEntity.getParMensajeRechazoEntity().getMensajeFuncional());
        }


        //se detectan errores pero distintos a respuesta de servicio scoring (errores de validaciones)
        if (respuesta != SIN_ERROR) {
            log.debug("Se detecta errores en validaciones de scoring");
            actualizarEstadoSolicitudAdmision(solicitudAdmisionEntity, Constantes.ESTADO_NO_APROBADO);

            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(respuesta);
            actualizarAdmisionReglaNegocio(solicitudAdmisionEntity, reglaNegocioEntity);

            guardaMotivoCartaRechazo(solicitudAdmisionEntity, reglaNegocioEntity.getParMensajeRechazoEntity().getMensajeCartaRechazo());

            RegistroProspectoResponseDTO response = new RegistroProspectoResponseDTO();
            response.setExito(false);
            response.setMensaje(reglaNegocioEntity.getParMensajeRechazoEntity().getMensajeFuncional());
            response.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());

            return response;
        }

        return null;

    }


    public void reglasCotizacionesPrevisionales(ProspectoEntity prospectoEntity, SolicitudAdmisionEntity solicitudAdmisionEntity) {

        log.debug("Consultar las cotizaciones del prospecto rut: {}", prospectoEntity.getRut());
        List<CotizacionPrevisionalDetalleEntity> cotizacionPrevisionalDetalleEntities = cotizacionPrevisionalDetalleRepository.findAllByCotizacionPrevisionalEntityProspectoEntityAndCotizacionPrevisionalEntityVigenciaIsTrue(prospectoEntity);

        log.debug("Cantidad cotizaciones: {}", cotizacionPrevisionalDetalleEntities.size());

        if (cotizacionPrevisionalDetalleEntities.size() < CANTIDAD_MINIMA_COTIZACIONES) {
            log.debug("No cumple minimo de cotizaciones, se asigna cupo de cortesia");
            evaluacionProductoService.cargaProductoCortesia(solicitudAdmisionEntity);
        }

        Long sumaRenta = cotizacionPrevisionalDetalleEntities.stream().map(c -> c.getRemuneracionImponible()).reduce(0l, Long::sum);
        log.debug("Suma renta: {}", sumaRenta);

        int rentaPromedio = sumaRenta.intValue() / cotizacionPrevisionalDetalleEntities.size();

        log.debug("renta promedio: {}", rentaPromedio);

        if (rentaPromedio < RENTA_MINIMA) {
            log.debug("No supera la renta minima solicitada, se asigna cupo de cortesia");
            evaluacionProductoService.cargaProductoCortesia(solicitudAdmisionEntity);
            return;
        }

        int cupoAprobado = 0;

        if (rentaPromedio <= TRAMO_MINIMO) {
            cupoAprobado = rentaPromedio - ((rentaPromedio * FACTOR_MINIMO) / 100);
        }

        if (rentaPromedio > TRAMO_MINIMO && rentaPromedio <= TRAMO_MEDIO) {
            cupoAprobado = rentaPromedio - ((rentaPromedio * FACTOR_MEDIO) / 100);
        }

        if (rentaPromedio >= TRAMO_MAXIMO) {
            cupoAprobado = rentaPromedio - ((rentaPromedio * FACTOR_MAXIMO) / 100);
        }

        if (cupoAprobado > CUPO_MAXIMO) {
            log.debug("Cupo aprobado excede maximo,se deja en tope permitido de {}", CUPO_MAXIMO);
            cupoAprobado = CUPO_MAXIMO;
        }
        log.debug("Cupo Aprobado segun calculos... {}", cupoAprobado);

        evaluacionProductoService.cargaProductosPreEvaluados(solicitudAdmisionEntity, cupoAprobado, null);
    }


    private void actualizarAdmisionReglaNegocio(SolicitudAdmisionEntity solicitudAdmisionEntity, ReglaNegocioEntity reglaNegocioEntity) {
        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
        admisionReglaNegocioEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        admisionReglaNegocioEntity.setReglaNegocioEntity(reglaNegocioEntity);
        admisionReglaNegocioEntity.setFechaRegistro(LocalDateTime.now());
        admisionReglaNegocioEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        admisionReglaNegocioEntity.setVigencia(true);
        admisionReglaNegocioRepository.save(admisionReglaNegocioEntity);
        admisionReglaNegocioRepository.flush();
    }


    private void actualizarEstadoSolicitudAdmision(SolicitudAdmisionEntity solicitudAdmisionEntity, long estado) {
        Optional<ParEstadoSolicitudEntity> estadoNoAprobadoOptional = parEstadoSolicitudRepository.findById(estado);
        ParEstadoSolicitudEntity estadoNoAprobado = estadoNoAprobadoOptional.get();
        solicitudAdmisionEntity.setFechaModificacion(LocalDateTime.now());
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(estadoNoAprobado);
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
        solicitudAdmisionRepository.flush();
    }

    private ReglaNegocioEntity obtenerReglaNegocioPorReglaId(long reglaId) {
        Optional<ReglaNegocioEntity> rechazoRepositoryById = reglaNegocioRepository.findById(reglaId);
        log.debug("Regla negocio: {}", rechazoRepositoryById.isPresent());
        return rechazoRepositoryById.get();
    }

    private void guardaMotivoCartaRechazo(SolicitudAdmisionEntity solicitudAdmisionEntity, String descripcion) {
        MotivoCartaRechazoEntity motivoCartaRechazoEntity = new MotivoCartaRechazoEntity();
        motivoCartaRechazoEntity.setDescripcion(descripcion);
        motivoCartaRechazoEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        motivoCartaRechazoEntity.setFechaRegistro(LocalDateTime.now());
        motivoCartaRechazoEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        motivoCartaRechazoRepository.save(motivoCartaRechazoEntity);
        motivoCartaRechazoRepository.flush();

    }
}
