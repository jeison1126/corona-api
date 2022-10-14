package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.dto.cotizacion.CotizacionesRequestDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.EnumOrigenSolcitudAdmision;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class BiometriaService {

    @Autowired
    private Validaciones validaciones;

    @Value("${disp.biometrico.uri}")
    private String dispositivoBiometricoUri;

    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    @Autowired
    private CargoAutorizadorExcepcionRepository cargoAutorizadorExcepcionRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @Autowired
    private BiometriaConsultaRepository biometriaConsultaRepository;

    @Autowired
    private BiometriaFaseRepository biometriaFaseRepository;

    @Autowired
    private CotizacionService cotizacionService;

    @Autowired
    private EvaluacionProductoService evaluacionProductoService;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private MotorReglasService motorReglasService;

    @Autowired
    private ParOrigenRepository parOrigenRepository;

    @Autowired
    private MotivoCartaRechazoRepository motivoCartaRechazoRepository;

    @Autowired
    private FirmaDocumentoService firmaDocumentoService;


    private static final int ULTIMAS_12_COTIZACIONES = 12;

    private static final String USUARIO_TEMPORAL = "USR_TMP";
    private static final String CODIGO_CEDULA_VENCIDA = "LCB-004-B";

    public VerificacionBiometriaResponseDTO verificaValidacionBiometrica(ActivarDispositivoRequestDTO request) {
        validarRequest(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        VerificacionBiometriaResponseDTO verificacionBiometriaResponseDTO = null;
        Optional<BiometriaFaseEntity> biometriaFaseEntityOptional = biometriaFaseRepository.verificaAprobacionValidacionBiometrica(rutFormateado, request.getSolicitudAdmisionId());
        if (biometriaFaseEntityOptional.isPresent()) {
            verificacionBiometriaResponseDTO = new VerificacionBiometriaResponseDTO();
            verificacionBiometriaResponseDTO.setValidacionBiometrica(true);
            verificacionBiometriaResponseDTO.setValidacionExcepcion(false);
            verificacionBiometriaResponseDTO.setSolicitudId(request.getSolicitudAdmisionId());

            return verificacionBiometriaResponseDTO;
        }

        Optional<BiometriaConsultaEntity> biometriaConsultaEntityOptional = biometriaConsultaRepository.verificaAprobacionValidacionExcepcion(request.getSolicitudAdmisionId());

        if (biometriaConsultaEntityOptional.isPresent()) {
            verificacionBiometriaResponseDTO = new VerificacionBiometriaResponseDTO();
            verificacionBiometriaResponseDTO.setValidacionBiometrica(false);
            verificacionBiometriaResponseDTO.setValidacionExcepcion(true);
            verificacionBiometriaResponseDTO.setSolicitudId(request.getSolicitudAdmisionId());

            return verificacionBiometriaResponseDTO;
        }

        throw new BadRequestException("No ha realizado validacion biometrica");
    }

    public ActivarDispositivoResponseDTO activarDispositivoBiometrico(ActivarDispositivoRequestDTO request, boolean isProspecto, long fase) {
        log.debug("Solicitud de activacion para prospecto");

        validarRequest(request);
        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        if (isProspecto) {
            obtenerSolicitudAdmisionActiva(rutFormateado, request.getSolicitudAdmisionId(), fase);
        } else {
            validacionEjecutivoCargo(rutFormateado);
            obtenerSolicitudAdmisionActiva(null, request.getSolicitudAdmisionId(), fase);
        }


        return respuestaActivacionDispositivo(request.getRut());
    }

    private void validarRequest(ActivarDispositivoRequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }

        if (request.getRut() == null || request.getRut().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un rut");
        }
        if (!validaciones.validaRut(request.getRut())) {
            throw new BadRequestException("El rut ingresado no es valido");
        }

        if (request.getSolicitudAdmisionId() == null || request.getSolicitudAdmisionId() <= 0) {
            log.error("id solicitud admision invalido");
            throw new BadRequestException("Debe ingresar el id solicitud admision valido");
        }
    }


    private void validacionEjecutivoCargo(String rut) {
        Optional<FuncionariosEntity> funcionariosEntityOptional = funcionariosRepository.findByRutAndVigenciaIsTrue(rut);

        if (funcionariosEntityOptional.isEmpty()) {
            throw new BadRequestException("El rut ingresado no corresponde a un ejecutivo de CORONA");
        }

        FuncionariosEntity funcionariosEntity = funcionariosEntityOptional.get();

        Optional<CargoAutorizadorExcepcionEntity> cargoAutorizadorExcepcionEntityOptional = cargoAutorizadorExcepcionRepository.validarCargoAutorizadorExcepcion(funcionariosEntity.getCargoId());

        if (cargoAutorizadorExcepcionEntityOptional.isEmpty()) {
            throw new NoContentException();
        }
    }

    private ActivarDispositivoResponseDTO respuestaActivacionDispositivo(String rut) {
        ActivarDispositivoResponseDTO response = new ActivarDispositivoResponseDTO();
        response.setRut(rut);
        response.setType("POST");
        response.setUrl(dispositivoBiometricoUri);

        return response;
    }

    private BiometriaResponseDTO respuestaRegistroBiometrico(BiometriaRequestDTO request) {
        BiometriaResponseDTO responseDTO = new BiometriaResponseDTO();

        log.debug("Codigo: {}", request.getCodigo());
        if (request.getCodigo() != null && !request.getCodigo().equalsIgnoreCase(CODIGO_CEDULA_VENCIDA)) {
            responseDTO.setError(true);
        }
        responseDTO.setMensaje(request.getDetalle());

        log.debug("Response: {}", responseDTO.toString());
        return responseDTO;
    }

    //@Transactional
    public BiometriaResponseDTO registrarBiometriaCotizaciones(String rut, Long solicitudId, BiometriaRequestDTO request) {
        log.debug("Registrar biometria prospecto para prospecto con rut: {} y solicitud: {}", rut, solicitudId);
        log.debug("request: {}", request.toString());
        validarRequestBiometrico(solicitudId);

        String rutFormateado = validaciones.formateaRutHaciaBD(rut);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_EVALUACION_SCORING, request, Constantes.BIOMETRIA_COTIZACIONES, false);

        //sin error desde biometria, se debe consultar por las cotizaciones
        if (request.getResultado() && request.getCodigo() == null && request.getDetalle() == null) {
            CotizacionesRequestDTO cotizacionesRequestDTO = new CotizacionesRequestDTO();
            cotizacionesRequestDTO.setRut(rut);
            cotizacionesRequestDTO.setCantidad(ULTIMAS_12_COTIZACIONES);

            Map<String, Object> respuesta = cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, request.getData().getTransaccion());

            if (respuesta == null) {
                //se inicia flujo de cortesia, por no tene r respuesta de servicio de previred con sus cotizaciones
                request.setDetalle("No se pudo obtener cotizaciones previsionales, se ofrece producto de cortesia");
                evaluacionProductoService.cargaProductoCortesia(solicitudAdmisionEntity);

                //registrar en tabla solicitud admision la observacion de problema con servicio previred
                solicitudAdmisionEntity.setObservacion("No se pudo obtener cotizaciones previsionales, se ofrece producto de cortesia");
                solicitudAdmisionEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
                solicitudAdmisionEntity.setFechaModificacion(LocalDateTime.now());
                solicitudAdmisionRepository.save(solicitudAdmisionEntity);
                solicitudAdmisionRepository.flush();

                admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);
                return respuestaRegistroBiometrico(request);
            }
        }

        //se debe llamar a motor de reglas para evaluar datos de cotizaciones
        ProspectoEntity prospectoEntity = solicitudAdmisionEntity.getProspectoEntity();
        motorReglasService.reglasCotizacionesPrevisionales(prospectoEntity, solicitudAdmisionEntity);

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);

        return respuestaRegistroBiometrico(request);
    }

    private BiometriaConsultaEntity mapearBiometriaEntity(BiometriaRequestDTO request, String rut, boolean excepcion) {

        log.debug("Request biometrico a mapear: {}", request.toString());
        BiometriaConsultaEntity biometriaConsultaEntity = new BiometriaConsultaEntity();
        biometriaConsultaEntity.setResultado(request.getResultado());
        biometriaConsultaEntity.setCodigo(request.getCodigo());
        biometriaConsultaEntity.setDetalle(request.getDetalle());
        biometriaConsultaEntity.setApellido(request.getData().getApellido());
        biometriaConsultaEntity.setRut(rut);
        biometriaConsultaEntity.setSerie(request.getData().getSerie());
        biometriaConsultaEntity.setTipo(request.getData().getTipo());
        if (request.getData().getVencimiento() != null && !request.getData().getVencimiento().trim().isEmpty())
            biometriaConsultaEntity.setVencimiento(validaciones.convertirStringALocaldate(request.getData().getVencimiento()));

        biometriaConsultaEntity.setHuellaCoincide(request.getData().getHuellaCoincide());
        biometriaConsultaEntity.setTransaccionId(request.getData().getTransaccion());
        biometriaConsultaEntity.setExcepcion(excepcion);
        biometriaConsultaEntity.setFechaRegistro(LocalDateTime.now());
        biometriaConsultaEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        biometriaConsultaEntity.setVigencia(true);

        return biometriaConsultaEntity;
    }

    private void validarRequestBiometrico(Long solicitudId) {

        if (solicitudId == null || solicitudId <= 0) {
            log.error("id solicitud admision invalido");
            throw new BadRequestException("Falta indicar id solicitud admision valido");
        }
    }


    private AdmisionFaseEntity obtenerSolicitudAdmisionActiva(String rut, long solicitudId, long fase) {
        log.debug("Fase: {}", fase);
        Optional<AdmisionFaseEntity> admisionFaseEntityOptional;
        if (rut != null) {
            log.debug("Normal");
            admisionFaseEntityOptional = admisionFaseRepository.validaSolicitudAdmisionYFaseActiva(rut, solicitudId, fase);
            if (admisionFaseEntityOptional.isEmpty()) {
                throw new BadRequestException("No se detectaron solicitud de admision activa");
            }
            return admisionFaseEntityOptional.get();
        }
        log.debug("Excepcion");
        admisionFaseEntityOptional = admisionFaseRepository.validaSolicitudAdmisionYFaseActivaExcepcion(solicitudId, fase);
        if (admisionFaseEntityOptional.isEmpty()) {
            throw new BadRequestException("No se detectaron solicitud de admision activa");
        }
        return admisionFaseEntityOptional.get();

    }

    @Transactional
    public BiometriaResponseDTO registrarBiometriaCotizacionesExcepcion(String rut, Long solicitudId, BiometriaRequestDTO request) {

        validarRequestBiometrico(solicitudId);
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        validacionEjecutivoCargo(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_EVALUACION_SCORING, request, Constantes.BIOMETRIA_COTIZACIONES, true);


        Optional<ParOrigenEntity> origenEntityOptional = parOrigenRepository.findById(EnumOrigenSolcitudAdmision.NORMAL_EXCEPCION.getCodigo());
        ParOrigenEntity parOrigenEntity = origenEntityOptional.get();
        solicitudAdmisionEntity.setParOrigenEntity(parOrigenEntity);
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
        solicitudAdmisionRepository.flush();


        //se registra evaluacion excepcion (productos de cortesia)
        evaluacionProductoService.cargaProductoCortesia(solicitudAdmisionEntity);
        //registro de fase finalizada
        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);
        return respuestaRegistroBiometrico(request);
    }


    public ActivarDispositivoResponseDTO activarDispositivoBiometricoCartaRechazo(ActivarDispositivoRequestDTO request, boolean isProspecto) {

        validarRequest(request);

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional;

        if (isProspecto) {
            motivoCartaRechazoEntityOptional = motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(request.getSolicitudAdmisionId(), rutFormateado);
        } else {
            validacionEjecutivoCargo(rutFormateado);
            motivoCartaRechazoEntityOptional = motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudId(request.getSolicitudAdmisionId());
        }


        if (motivoCartaRechazoEntityOptional.isEmpty()) {
            log.debug("No se encontro carta de rechazo asociada");
            throw new BadRequestException("No se encontraron carta de rechazo asociado");
        }

        return respuestaActivacionDispositivo(request.getRut());
    }

    public BiometriaResponseDTO registrarBiometriaFirmaElectronica(String rut, Long solicitudId, boolean pep, BiometriaRequestDTO request) {
        log.debug("Registrar biometria prospecto para firma electronica con rut: {} y solicitud: {}", rut, solicitudId);
        log.debug("request: {}", request.toString());
        validarRequestBiometrico(solicitudId);

        String rutFormateado = validaciones.formateaRutHaciaBD(rut);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_DATOS_CLIENTE, request, Constantes.BIOMETRIA_FIRMA_CONTRATO, false);

        if (request.getResultado() && request.getCodigo() == null && request.getDetalle() == null) {
            log.debug("Continuar con flujo e integracion servicio firma de contratos");
        }

        solicitudAdmisionEntity.setPep(pep);
        solicitudAdmisionRepository.save(solicitudAdmisionEntity);
        solicitudAdmisionRepository.flush();

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_FIRMA_CONTRATOS);

        return respuestaRegistroBiometrico(request);
    }


    public BiometriaFirmaContratoExcepcionResponseDTO registrarBiometriaFirmaElectronicaExcepcion(String rut, Long solicitudId, int usuarioEcertId, BiometriaRequestDTO request) {
        validarRequestBiometrico(solicitudId);
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        String rutEjecutivoFormateado = validaciones.formateaRutHaciaBD(request.getData().getRut());
        validacionEjecutivoCargo(rutEjecutivoFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_DATOS_CLIENTE, request, Constantes.BIOMETRIA_FIRMA_CONTRATO, true);

        List<DocumentoDTO> documentosManuales = new ArrayList<>();
        if (request.getResultado() && request.getCodigo() == null && request.getDetalle() == null) {
            log.debug("Continuar con flujo excepcion e imprimir contratos para firma manual... cambiar response a descarga de archivos con los contratos");
            documentosManuales = firmaDocumentoService.preparaDocumentosManuales(rut, solicitudId, usuarioEcertId);
        }

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_FIRMA_CONTRATOS);


        BiometriaFirmaContratoExcepcionResponseDTO response = new BiometriaFirmaContratoExcepcionResponseDTO();
        response.setDocumentos(documentosManuales);
        BiometriaResponseDTO biometriaResponseDTO = respuestaRegistroBiometrico(request);
        response.setError(biometriaResponseDTO.isError());
        response.setMensaje(biometriaResponseDTO.getMensaje());
        return response;
    }

    public BiometriaResponseDTO registrarBiometriaImpresionTarjeta(String rut, Long solicitudId, BiometriaRequestDTO request) {
        log.debug("Registrar biometria prospecto para impresion de tarjeta con rut: {} y solicitud: {}", rut, solicitudId);
        log.debug("request: {}", request.toString());
        validarRequestBiometrico(solicitudId);

        String rutFormateado = validaciones.formateaRutHaciaBD(rut);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_FIRMA_CONTRATOS, request, Constantes.BIOMETRIA_IMPRESION_TARJETA, false);


        //sin error desde biometria, se debe consultar por las cotizaciones
        if (request.getResultado() && request.getCodigo() == null && request.getDetalle() == null) {
            log.debug("agregar codigo para integracion con servicio de impresion de terjetas");
        }

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA);

        return respuestaRegistroBiometrico(request);
    }

    public BiometriaResponseDTO registrarBiometriaImpresionTarjetaExcepcion(String rut, Long solicitudId, BiometriaRequestDTO request) {
        validarRequestBiometrico(solicitudId);
        String rutFormateado = validaciones.formateaRutHaciaBD(rut);
        validacionEjecutivoCargo(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = registroResultadoBiometria(rutFormateado, solicitudId, Constantes.FASE_FIRMA_CONTRATOS, request, Constantes.BIOMETRIA_IMPRESION_TARJETA, true);

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA);
        return respuestaRegistroBiometrico(request);
    }

    private SolicitudAdmisionEntity registroResultadoBiometria(String rut, long solicitudId, long fase, BiometriaRequestDTO request, long tipoBiometriaId, boolean excepcion) {

        AdmisionFaseEntity admisionFaseEntity = (!excepcion) ? obtenerSolicitudAdmisionActiva(rut, solicitudId, fase) : obtenerSolicitudAdmisionActiva(null, solicitudId, fase);
        SolicitudAdmisionEntity solicitudAdmisionEntity = admisionFaseEntity.getSolicitudAdmisionEntity();
        ProspectoEntity prospectoEntity = solicitudAdmisionEntity.getProspectoEntity();
        ParFaseEntity parFaseEntity = admisionFaseEntity.getParFaseEntity();


        BiometriaConsultaEntity biometriaConsultaEntity = mapearBiometriaEntity(request, rut, excepcion);
        biometriaConsultaEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        biometriaConsultaRepository.save(biometriaConsultaEntity);
        biometriaConsultaRepository.flush();

        BiometriaFaseEntity biometriaFaseEntity = new BiometriaFaseEntity();
        biometriaFaseEntity.setBiometriaConsultaEntity(biometriaConsultaEntity);
        biometriaFaseEntity.setParFaseEntity(parFaseEntity);
        biometriaFaseEntity.setProspectoEntity(prospectoEntity);
        biometriaFaseEntity.setFechaRegistro(LocalDateTime.now());
        biometriaFaseEntity.setVigencia(true);

        ParTipoBiometriaEntity parTipoBiometria = new ParTipoBiometriaEntity();
        parTipoBiometria.setTipoBiometriaId(tipoBiometriaId);
        biometriaFaseEntity.setParTipoBiometriaEntity(parTipoBiometria);

        biometriaFaseRepository.save(biometriaFaseEntity);
        biometriaFaseRepository.flush();


        return solicitudAdmisionEntity;
    }
}
