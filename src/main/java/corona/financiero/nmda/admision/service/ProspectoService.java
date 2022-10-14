package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.EmailAdapter;
import corona.financiero.nmda.admision.adapter.IntellectAdapter;
import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.dto.notificacion.EmailAttachmentDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailParametersDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailPropertiesDTO;
import corona.financiero.nmda.admision.dto.notificacion.EmailToDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProspectoService {

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private ProspectoRepository prospectoRepository;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    @Autowired
    private ParMensajeRechazoRepository parMensajeRechazoRepository;

    @Autowired
    private ParCanalRepository parCanalRepository;

    @Autowired
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    @Autowired
    private PepRepository pepRepository;

    @Autowired
    private ReglaNegocioRepository reglaNegocioRepository;

    @Autowired
    private AdmisionReglaNegocioRepository admisionReglaNegocioRepository;

    @Autowired
    private IntellectAdapter intellectAdapter;


    private static final String USUARIO_TEMPORAL = "USR_TMP";

    @Value("${dominios.email.lista.negra}")
    private String dominiosListaNegra;

    @Autowired
    private ScoringService scoringService;

    @Autowired
    private EmailAdapter emailAdapter;

    @Value("${email.riesgo}")
    private String emailRiesgo;

    @Autowired
    private CampaniasService campaniasService;

    @Autowired
    private ParOrigenRepository parOrigenRepository;

    @Autowired
    private AdmisionFaseService admisionFaseService;

    @Autowired
    private MFAService mfaService;

    @Autowired
    private MotorReglasService motorReglasService;


    @Value("${cantidad.dias.vigencia}")
    private int cantidadDiasVigencia;

    //@Transactional
    public RegistroProspectoResponseDTO registroProspecto(ProspectoDTO prospectoDTO) {
        log.debug("Flujo registro prospecto");
        validarRegistroCliente(prospectoDTO);

        String rutFormateado = validaciones.formateaRutHaciaBD(prospectoDTO.getRut());
        String emailFormateado = prospectoDTO.getEmail().toLowerCase(Locale.ROOT);

        log.debug("Rut formateado: {}", rutFormateado);
        log.debug("Correo: {}", emailFormateado);
        log.debug("Movil: {}", prospectoDTO.getMovil());


        //validacion si ya existe como cliente corona en nueva BD
        if (existeCliente(rutFormateado)) {
            log.debug("Ya se encuentra registrado como cliente activo");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_EXISTENTE);
            log.error(reglaNegocioEntity.getParMensajeRechazoEntity().getDescripcion());
            throw new BadRequestException(reglaNegocioEntity.getParMensajeRechazoEntity().getMensajeFuncional());
        }
        //agregar validaciones de busqueda de usuario en servicio INTELLECT
        if (intellectAdapter.validateIfClientExist(rutFormateado)) {
            log.debug("Ya se encuentra registrado como cliente intellect");
            ReglaNegocioEntity reglaNegocioEntity = obtenerReglaNegocioPorReglaId(Constantes.REGLA_CLIENTE_EXISTENTE);
            log.error(reglaNegocioEntity.getParMensajeRechazoEntity().getDescripcion());
            throw new BadRequestException(reglaNegocioEntity.getParMensajeRechazoEntity().getMensajeFuncional());
        }

        ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado);

        if (prospectoEntity != null) {
            log.debug("Se debe invalidar solicitudes anteriores para prospecto rut: {}", prospectoEntity.getRut());
            List<SolicitudAdmisionEntity> solicitudesPrevias = solicitudAdmisionRepository.findAllByProspectoEntityAndVigenciaIsTrue(prospectoEntity);
            List<SolicitudAdmisionEntity> solicitudesAInvalidar = solicitudesPrevias.stream().map(s -> {
                s.setVigencia(false);
                return s;
            }).collect(Collectors.toList());

            log.debug("Solicitudes a invalidar: {}", solicitudesAInvalidar.size());

            solicitudAdmisionRepository.saveAll(solicitudesAInvalidar);
            solicitudAdmisionRepository.flush();

        } else {
            prospectoEntity = new ProspectoEntity();
        }

        prospectoEntity.setEmail(emailFormateado);
        prospectoEntity.setRut(rutFormateado);
        prospectoEntity.setVigencia(true);
        prospectoEntity.setMovil(Long.parseLong(prospectoDTO.getMovil()));

        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setUsuarioIngreso(USUARIO_TEMPORAL);

        prospectoRepository.save(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();

        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity);
        //por defecto para admision presencial, en caso web se debe considerar WEB

        Optional<ParCanalEntity> parCanalEntityOptional = parCanalRepository.findById(Constantes.CANAL_TIENDA);
        ParCanalEntity parCanalEntity = parCanalEntityOptional.get();

        solicitudAdmisionEntity.setParCanalEntity(parCanalEntity);
        //se debe definir cual es el estado inicial de formulario prospecto
        Optional<ParEstadoSolicitudEntity> parEstadoSolicitudEntityOptional = parEstadoSolicitudRepository.findById(Constantes.ESTADO_INICIADO);
        ParEstadoSolicitudEntity parEstadoSolicitudEntity = parEstadoSolicitudEntityOptional.get();
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(parEstadoSolicitudEntity);


        //al tener integrado token de usuario, obtener datos para registro (usuario y sucursal)
        solicitudAdmisionEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        solicitudAdmisionEntity.setSucursalId(1l);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        solicitudAdmisionEntity.setVigencia(true);

        Optional<ParOrigenEntity> origenEntityOptional = parOrigenRepository.findById(EnumOrigenSolcitudAdmision.NORMAL.getCodigo());
        ParOrigenEntity parOrigenEntity = origenEntityOptional.get();
        solicitudAdmisionEntity.setParOrigenEntity(parOrigenEntity);

        solicitudAdmisionRepository.save(solicitudAdmisionEntity);

        admisionFaseService.actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COMERCIAL);

        RegistroProspectoResponseDTO response = null;

        CampaniaResponseDTO campaniaResponseDTO = campaniasService.evaluaCampania(solicitudAdmisionEntity, rutFormateado);

        //Si prospecto se encuentra en alguna campaña activa, se registra la fase sin tener que evaluar datos basicos
        if (campaniaResponseDTO.isCampania()) {
            log.debug("Tiene campania activa");
            response = new RegistroProspectoResponseDTO();
            response.setExito(true);
            response.setMensaje("Formulario de registro procesado correctamente con campaña activa");
            log.debug("Fin formulario prospecto sin errores");

            return response;
        }

        //evaluacion interna
        response = motorReglasService.reglasEvaluacionInterna(rutFormateado, solicitudAdmisionEntity);
        if (response != null) {
            return response;
        }

        //validacion scoring equifax
        response = motorReglasService.reglaEvaluacionScoringEquifax(rutFormateado, solicitudAdmisionEntity);
        if (response != null) {
            return response;
        }

        //evaluaciones internas - scoring pasadas, se genera codigo MFA

        MFARequestDTO mfaRequestDTO = new MFARequestDTO();
        mfaRequestDTO.setRut(rutFormateado);
        mfaService.enviarSMS(mfaRequestDTO);


        validarProspectoPEP(rutFormateado);

        response = new RegistroProspectoResponseDTO();
        response.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());
        response.setExito(true);
        response.setMensaje("Formulario de registro procesado correctamente");
        log.debug("Fin formulario prospecto sin errores y fin evaluaciones internas");


        return response;
    }


    private ReglaNegocioEntity obtenerReglaNegocioPorReglaId(long reglaId) {

        Optional<ReglaNegocioEntity> rechazoRepositoryById = reglaNegocioRepository.findById(reglaId);

        log.debug("Regla negocio: {}", rechazoRepositoryById.isPresent());

        return rechazoRepositoryById.get();
    }

    private void validarProspectoPEP(String rut) {

        Optional<PepEntity> pepEntityOptional = pepRepository.findById(rut);
        if (pepEntityOptional.isPresent()) {

            ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rut);

            String nombreProespecto = prospectoEntity.getNombres() + " " + prospectoEntity.getApellidoPaterno() + " " + prospectoEntity.getApellidoMaterno();

            EmailPropertiesDTO emailRequestDTO = new EmailPropertiesDTO();
            emailRequestDTO.setTemplateId("pep-template");
            emailRequestDTO.setSubject("Notificacion PEP");

            List<EmailToDTO> cc = new ArrayList<>();
            emailRequestDTO.setCc(cc);

            EmailToDTO to = new EmailToDTO();
            to.setEmail(emailRiesgo);

            emailRequestDTO.setTo(Arrays.asList(to));

            EmailParametersDTO paramRut = new EmailParametersDTO();
            paramRut.setParam("rut");
            paramRut.setValue(validaciones.formateaRutHaciaFront(rut));

            EmailParametersDTO paramNombre = new EmailParametersDTO();
            paramNombre.setParam("nombres");
            paramNombre.setValue(nombreProespecto);
            emailRequestDTO.setParams(Arrays.asList(paramRut, paramNombre));

            List<EmailAttachmentDTO> attach = new ArrayList<>();
            emailRequestDTO.setAttachments(attach);

            emailAdapter.notificar(emailRequestDTO);

            log.debug("Notificacion PEP enviada");
        }
    }

    private void validarRegistroCliente(ProspectoDTO prospectoDTO) {

        if (prospectoDTO == null) {
            throw new BadRequestException();
        }

        if (prospectoDTO.getEmail() == null || prospectoDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un email");
        }

        if (!validaciones.validaEmail(prospectoDTO.getEmail())) {
            throw new BadRequestException("El email ingresado no es valido");
        }

        List<String> dominios = Arrays.asList(dominiosListaNegra.split(","));

        if (validaciones.validaEmailListaNegra(prospectoDTO.getEmail(), dominios)) {
            throw new BadRequestException("El email ingresado se encuentra en lista de emails no permitidos");
        }

        if (prospectoDTO.getMovil() == null || prospectoDTO.getMovil().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un numero de movil");
        }

        if (!validaciones.validaNumeroMovil(prospectoDTO.getMovil())) {
            throw new BadRequestException("El numero movil ingresado no es valido");
        }

        validaciones.validacionGeneralRut(prospectoDTO.getRut());
    }


    public ProspectoResponseDTO buscarCliente(String rutCliente) {

        log.debug("En metodo buscar cliente");
        validaciones.validacionGeneralRut(rutCliente);
        String rutFormateado = validaciones.formateaRutHaciaBD(rutCliente);

        log.debug("Consultar datos de prospecto en BD");
        ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado);

        if (prospectoEntity == null) {
            throw new NoContentException();
        }

        SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionRepository.obtenerSolicitudActivaYVigente(prospectoEntity.getProspectoId());

        List<AdmisionFaseEntity> fasesRegistradas = admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity);
        AtomicReference<Boolean> fase = new AtomicReference<>(false);

        fasesRegistradas.stream().filter(f -> f.getParFaseEntity().getFaseId() == Constantes.FASE_EVALUACION_SCORING).forEach(f -> fase.set(true));

        ProspectoResponseDTO prospectoDTO = new ProspectoResponseDTO();

        prospectoDTO.setEmail(prospectoEntity.getEmail());
        prospectoDTO.setMovil(String.valueOf(prospectoEntity.getMovil()));
        prospectoDTO.setRut(validaciones.formateaRutHaciaFront(prospectoEntity.getRut()));
        prospectoDTO.setProspectoId(prospectoEntity.getProspectoId());
        boolean faseMinimaRequerida = fase.get().booleanValue();

        if (solicitudAdmisionEntity != null && faseMinimaRequerida) {
            LocalDate hasta = solicitudAdmisionEntity.getFechaSolicitud().plusDays(cantidadDiasVigencia);
            Date date = validaciones.convertToDateViaSqlDate(hasta);
            SolicitudAdmisionEntity solicitudFechaVigenteEntity = solicitudAdmisionRepository.validarFecha(solicitudAdmisionEntity.getSolicitudId(), date);


            if (solicitudFechaVigenteEntity != null) {
                prospectoDTO.setSolicitudId(solicitudAdmisionEntity.getSolicitudId());
            }

        }

        return prospectoDTO;
    }

    private boolean existeCliente(String rutCliente) {
        boolean existe = false;
        Optional<ClienteEntity> optional = clienteRepository.findById(rutCliente);
        if (optional.isPresent()) {
            existe = true;
        }

        return existe;
    }

    public DatosBasicosMinimosResponseDTO datosPersonalesBasicos(String rutCliente) {

        validaciones.validacionGeneralRut(rutCliente);
        String rutFormateado = validaciones.formateaRutHaciaBD(rutCliente);

        ProspectoEntity prospectoEntity = prospectoRepository.findByRut(rutFormateado);

        if (prospectoEntity == null) {
            throw new NoContentException();
        }

        DatosBasicosMinimosResponseDTO datos = new DatosBasicosMinimosResponseDTO();
        datos.setNombre(prospectoEntity.getNombres());
        datos.setApellidoPatenro(prospectoEntity.getApellidoPaterno());
        datos.setApellidoMaterno(prospectoEntity.getApellidoMaterno());
        datos.setRut(validaciones.formateaRutHaciaFront(prospectoEntity.getRut()));
        datos.setProspectoId(prospectoEntity.getProspectoId());

        return datos;
    }
}
