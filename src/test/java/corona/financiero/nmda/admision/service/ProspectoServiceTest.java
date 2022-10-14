package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.EmailAdapter;
import corona.financiero.nmda.admision.adapter.IntellectAdapter;
import corona.financiero.nmda.admision.dto.CampaniaResponseDTO;
import corona.financiero.nmda.admision.dto.ProspectoDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.EnumOrigenSolcitudAdmision;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProspectoServiceTest {

    @InjectMocks
    private ProspectoService prospectoService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProspectoRepository prospectoRepository;

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Mock
    private AdmisionFaseRepository admisionFaseRepository;

    @Mock
    private PepRepository pepRepository;

    @Mock
    private ReglaNegocioRepository reglaNegocioRepository;


    @Mock
    private IntellectAdapter intellectAdapter;


    @Mock
    private EmailAdapter emailAdapter;

    @Mock
    private CampaniasService campaniasService;

    @Mock
    private ParOrigenRepository parOrigenRepository;

    @Mock
    private AdmisionFaseService admisionFaseService;

    @Mock
    private MFAService mfaService;

    @Mock
    private MotorReglasService motorReglasService;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(prospectoService, "dominiosListaNegra", "@corona.cl,@corona.com");
        ReflectionTestUtils.setField(prospectoService, "cantidadDiasVigencia", 30);
    }

    @Test
    void buscarClienteTest() {
        String rutCliente = "11111111-1";
        String rutFormateado = "111111111";

        doNothing().when(validaciones).validacionGeneralRut(rutCliente);

        when(validaciones.formateaRutHaciaBD(rutCliente)).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setRut(rutFormateado);
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setVigencia(true);
        prospectoEntity.setUsuarioIngreso("USR_TMP");
        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setEmail("a@b.cl");

        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmision(prospectoEntity);

        AdmisionFaseEntity admisionFaseEntity = obtieneAdmisionFase();
        List<AdmisionFaseEntity> admisionFaseEntities = Arrays.asList(admisionFaseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(admisionFaseEntities);


        int cantidadDiasVigencia = 30;
        LocalDate hasta = LocalDate.now().plusDays(cantidadDiasVigencia);

        when(solicitudAdmisionRepository.obtenerSolicitudActivaYVigente(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        prospectoService.buscarCliente(rutCliente);

    }

    @Test
    void buscarClienteSolicitudVigenteTest() {
        String rutCliente = "11111111-1";
        String rutFormateado = "111111111";

        doNothing().when(validaciones).validacionGeneralRut(rutCliente);

        when(validaciones.formateaRutHaciaBD(rutCliente)).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setRut(rutFormateado);
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setVigencia(true);
        prospectoEntity.setUsuarioIngreso("USR_TMP");
        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setEmail("a@b.cl");

        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmision(prospectoEntity);

        AdmisionFaseEntity admisionFaseEntity = obtieneAdmisionFase();
        List<AdmisionFaseEntity> admisionFaseEntities = Arrays.asList(admisionFaseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(admisionFaseEntities);

        int cantidadDiasVigencia = 30;
        LocalDate hasta = LocalDate.now().plusDays(cantidadDiasVigencia);

        Date date = new Date();

        when(solicitudAdmisionRepository.obtenerSolicitudActivaYVigente(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        when(validaciones.convertToDateViaSqlDate(hasta)).thenReturn(date);
        when(solicitudAdmisionRepository.validarFecha(solicitudAdmisionEntity.getSolicitudId(), date)).thenReturn(solicitudAdmisionEntity);

        prospectoService.buscarCliente(rutCliente);

    }

    @Test
    void buscarClienteSinFaseMinimaTest() {
        String rutCliente = "11111111-1";
        String rutFormateado = "111111111";

        doNothing().when(validaciones).validacionGeneralRut(rutCliente);

        when(validaciones.formateaRutHaciaBD(rutCliente)).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setRut(rutFormateado);
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setVigencia(true);
        prospectoEntity.setUsuarioIngreso("USR_TMP");
        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setEmail("a@b.cl");

        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmision(prospectoEntity);
        when(solicitudAdmisionRepository.obtenerSolicitudActivaYVigente(prospectoEntity.getProspectoId())).thenReturn(null);

        AdmisionFaseEntity admisionFaseEntity = obtieneAdmisionFaseInicial();
        List<AdmisionFaseEntity> admisionFaseEntities = Arrays.asList(admisionFaseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(admisionFaseEntities);

        when(solicitudAdmisionRepository.obtenerSolicitudActivaYVigente(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        prospectoService.buscarCliente(rutCliente);

    }


    @Test
    void buscarClienteProspectoErrorTest() {
        String rutCliente = "11111111-1";
        String rutFormateado = "111111111";

        doNothing().when(validaciones).validacionGeneralRut(rutCliente);

        when(validaciones.formateaRutHaciaBD(rutCliente)).thenReturn(rutFormateado);

        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> prospectoService.buscarCliente(rutCliente))
                .withNoCause();
    }


    @Test
    void registroProspectoClienteIntellectErrorTest() {

        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setMovil("987654321");
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@b.cl");

        doNothing().when(validaciones).validacionGeneralRut(prospectoDTO.getRut());
        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);
        when(validaciones.validaNumeroMovil(prospectoDTO.getMovil())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(prospectoDTO.getRut())).thenReturn(rutFormateado);

        ClienteEntity c = null;
        Optional<ClienteEntity> optional = Optional.ofNullable(c);
        when(clienteRepository.findById(rutFormateado)).thenReturn(optional);

        when(intellectAdapter.validateIfClientExist(rutFormateado)).thenReturn(true);

        ReglaNegocioEntity reglaNegocioEntity = obtieneReglaNegocio();
        Optional<ReglaNegocioEntity> reglaNegocioEntityOptional = Optional.of(reglaNegocioEntity);
        when(reglaNegocioRepository.findById(Constantes.REGLA_CLIENTE_EXISTENTE)).thenReturn(reglaNegocioEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }


    @Test
    void registroProspectoClienteExisteErrorTest() {

        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setMovil("987654321");
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@b.cl");

        doNothing().when(validaciones).validacionGeneralRut(prospectoDTO.getRut());
        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);
        when(validaciones.validaNumeroMovil(prospectoDTO.getMovil())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(prospectoDTO.getRut())).thenReturn(rutFormateado);

        ClienteEntity c = new ClienteEntity();
        c.setUsuarioRegistro("TMP_USR");
        c.setVigencia(true);
        c.setFechaRegistro(LocalDateTime.now());
        c.setEmail("a@b.cl");
        c.setRut("111111111");
        Optional<ClienteEntity> optional = Optional.of(c);
        when(clienteRepository.findById(rutFormateado)).thenReturn(optional);

        ReglaNegocioEntity reglaNegocioEntity = obtieneReglaNegocio();
        Optional<ReglaNegocioEntity> reglaNegocioEntityOptional = Optional.of(reglaNegocioEntity);
        when(reglaNegocioRepository.findById(Constantes.REGLA_CLIENTE_EXISTENTE)).thenReturn(reglaNegocioEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidRequestErrorTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(null))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidEmailNullErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setMovil("987654321");
        prospectoDTO.setRut("11111111-1");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidEmailEmptyErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setMovil("987654321");
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidEmailErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setMovil("987654321");
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("asasas.com");
        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidMovilNullErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@b.cl");

        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);

        when(validaciones.validaEmailListaNegra(any(), any())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidMovillEmptyErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@b.cl");
        prospectoDTO.setMovil("");

        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);

        when(validaciones.validaEmailListaNegra(any(), any())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoInvalidMovilErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@b.cl");
        prospectoDTO.setMovil("9876543");
        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);
        when(validaciones.validaNumeroMovil(prospectoDTO.getMovil())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void registroProspectoEmailListaNegraErrorTest() {
        ProspectoDTO prospectoDTO = new ProspectoDTO();
        prospectoDTO.setRut("11111111-1");
        prospectoDTO.setEmail("a@corona.cl");
        prospectoDTO.setMovil("987654321");

        when(validaciones.validaEmail(prospectoDTO.getEmail())).thenReturn(true);

        String listaNegra = "@corona.cl,@corona.com";
        List<String> dominios = Arrays.asList(listaNegra.split(","));
        when(validaciones.validaEmailListaNegra(prospectoDTO.getEmail(), dominios)).thenReturn(true);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> prospectoService.registroProspecto(prospectoDTO))
                .withNoCause();
    }

    @Test
    void datosPersonalesBasicosTest() {
        String rut = "11111111-1";

        doNothing().when(validaciones).validacionGeneralRut(rut);

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();

        when(prospectoRepository.findByRut(rutFormateado)).thenReturn(prospectoEntity);

        prospectoService.datosPersonalesBasicos(rut);
    }

    @Test
    void datosPersonalesBasicosErrorTest() {
        String rut = "11111111-1";

        doNothing().when(validaciones).validacionGeneralRut(rut);

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        when(prospectoRepository.findByRut(rutFormateado)).thenReturn(null);


        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> prospectoService.datosPersonalesBasicos(rut))
                .withNoCause();
    }


    private ProspectoEntity obtieneProspecto(String email, String movil, String rut) {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setEmail(email);
        prospectoEntity.setRut(rut);
        prospectoEntity.setVigencia(true);

        prospectoEntity.setMovil(Long.parseLong(movil));
        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setUsuarioIngreso("USR_TMP");

        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");

        return prospectoEntity;
    }

    private ParCanalEntity obtieneParCanal() {
        ParCanalEntity parCanalEntity = new ParCanalEntity();
        parCanalEntity.setCanalId(Constantes.CANAL_TIENDA);
        parCanalEntity.setFechaRegistro(LocalDateTime.now());
        parCanalEntity.setUsuarioRegistro("USR_TMP");
        parCanalEntity.setDescripcion("PRESENCIAL");
        parCanalEntity.setVigencia(true);

        return parCanalEntity;
    }


    private ParEstadoSolicitudEntity obtieneParEstadoSolicitud(long estado) {
        ParEstadoSolicitudEntity parEstadoSolicitudEntity = new ParEstadoSolicitudEntity();
        parEstadoSolicitudEntity.setEstadoSolicitudId(estado);
        parEstadoSolicitudEntity.setFechaRegistro(LocalDateTime.now());
        parEstadoSolicitudEntity.setDescripcion("INICIADO");
        parEstadoSolicitudEntity.setUsuarioRegistro("USR_TMP");
        parEstadoSolicitudEntity.setVigencia(true);

        return parEstadoSolicitudEntity;
    }


    private SolicitudAdmisionEntity obtieneSolicitudAdmision(ProspectoEntity prospectoEntity) {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setUsuarioRegistro("USR_TMP");
        solicitudAdmisionEntity.setSucursalId(1l);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        solicitudAdmisionEntity.setVigencia(true);
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity);
        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());

        return solicitudAdmisionEntity;
    }

    private ParOrigenEntity obtieneParOrigenEntity() {
        ParOrigenEntity parOrigenEntity = new ParOrigenEntity();
        parOrigenEntity.setOrigenId(1l);
        parOrigenEntity.setDescripcion("Normal");
        parOrigenEntity.setVigencia(true);
        return parOrigenEntity;
    }

    private AdmisionFaseEntity obiteneAdmisionFase(SolicitudAdmisionEntity solicitudAdmisionEntity) {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(Constantes.FASE_EVALUACION_COMERCIAL);
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setParFaseEntity(parFaseEntity);
        admisionFaseEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        admisionFaseEntity.setVigencia(true);
        admisionFaseEntity.setFechaRegistro(LocalDateTime.now());
        admisionFaseEntity.setUsuarioRegistro("USR_TMP");

        return admisionFaseEntity;
    }


    private CampaniaResponseDTO obtieneCampaniaResponseDTO() {
        CampaniaResponseDTO campaniaResponseDTO = new CampaniaResponseDTO();
        campaniaResponseDTO.setCampania(false);
        campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.NORMAL);

        return campaniaResponseDTO;
    }


    private ProspectoEntity datosMinimosProspecto() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setRut("111111111");

        return prospectoEntity;
    }

    private AdmisionFaseEntity obtieneAdmisionFase() {
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1l);
        admisionFaseEntity.setParFaseEntity(obtieneParFaseEntity());
        return admisionFaseEntity;
    }

    private ParFaseEntity obtieneParFaseEntity() {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(8l);
        parFaseEntity.setDescripcion("Evaluacion Cotizaciones");
        parFaseEntity.setVigencia(true);
        return parFaseEntity;
    }

    private AdmisionFaseEntity obtieneAdmisionFaseInicial() {
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1l);
        admisionFaseEntity.setParFaseEntity(obtieneParFaseEntityInicial());
        return admisionFaseEntity;
    }

    private ParFaseEntity obtieneParFaseEntityInicial() {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1l);
        parFaseEntity.setDescripcion("Evaluacion");
        parFaseEntity.setVigencia(true);
        return parFaseEntity;
    }

    private ReglaNegocioEntity obtieneReglaNegocio() {
        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setMensajeRechazoId(1l);
        parMensajeRechazoEntity.setMensajeFuncional("Mensaje funcional");
        parMensajeRechazoEntity.setDescripcion("Mensaje interno");
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(Constantes.REGLA_CLIENTE_EXISTENTE);
        reglaNegocioEntity.setDescripcion("Cliente Existente");
        reglaNegocioEntity.setVigencia(true);
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setParMensajeRechazoEntity(parMensajeRechazoEntity);

        return reglaNegocioEntity;
    }

    private Optional obtieneReglaNegocio(long reglaId) {
        ReglaNegocioEntity reglaNegocioEntity = obtieneReglaNegocio();
        Optional<ReglaNegocioEntity> reglaNegocioEntityOptional = Optional.of(reglaNegocioEntity);
        return reglaNegocioEntityOptional;
    }
}
