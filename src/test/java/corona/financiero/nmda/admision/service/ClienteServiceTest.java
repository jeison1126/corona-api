package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.CYCAdapter;
import corona.financiero.nmda.admision.dto.ClienteActivoRequestDTO;
import corona.financiero.nmda.admision.dto.ComunaDTO;
import corona.financiero.nmda.admision.dto.RegionDTO;
import corona.financiero.nmda.admision.dto.RegistroClienteRequestDTO;
import corona.financiero.nmda.admision.dto.cyc.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ParProfesionRepository parProfesionRepository;

    @Mock
    private ParEstadoCivilRepository parEstadoCivilRepository;

    @Mock
    private ParNacionalidadRepository parNacionalidadRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Validaciones validaciones;

    @Mock
    private ParActividadRepository parActividadRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private TarjetaActivacionRepository tarjetaActivacionRepository;

    @Mock
    private AdmisionFaseService admisionFaseService;

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Mock
    private RegionComunaService regionComunaService;

    @Mock
    private CYCAdapter cycAdapter;

    @Mock
    private TarjetaRepository tarjetaRepository;

    //@Test
    void registroDatosClienteTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        ParNacionalidadEntity parNacionalidadEntity = parNacionalidadEntity();
        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.of(parNacionalidadEntity);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        ParEstadoCivilEntity parEstadoCivilEntity = parEstadoCivilEntity();
        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = Optional.of(parEstadoCivilEntity);
        when(parEstadoCivilRepository.findById(request.getEstadoCivil())).thenReturn(estadoCivilEntityOptional);

        ParActividadEntity parActividadEntity = parActividadEntity();
        Optional<ParActividadEntity> actividadEntityOptional = Optional.of(parActividadEntity);
        when(parActividadRepository.findById(request.getActividad())).thenReturn(actividadEntityOptional);

        ClienteEntity clienteEntity = clienteEntity();
        Optional<ClienteEntity> clienteEntityOptional = Optional.of(clienteEntity);
        when(clienteRepository.findById(rutFormateado)).thenReturn(clienteEntityOptional);

        TarjetaEntity tarjetaEntity = tarjetaEntity();
        Optional<TarjetaEntity> tarjetaEntityOptional = Optional.of(tarjetaEntity);
        when(tarjetaRepository.findByClienteEntityAndVigenciaIsTrue(clienteEntity)).thenReturn(tarjetaEntityOptional);

        CYCCrearCuentaRequestDTO cycCrearCuentaRequestDTO = cycCrearCuentaRequestDTO();
        CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO = cycCrearCuentaResponseDTO();
        when(cycAdapter.crearCuentaCYC(cycCrearCuentaRequestDTO)).thenReturn(cycCrearCuentaResponseDTO);

        clienteService.registroDatosCliente(request);
    }

    @Test
    void registroDatosClienteValidacionRequestErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(null))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionNacionalidadErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setNacionalidad(0);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionEstadoCivilErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setEstadoCivil(0);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionProfesionErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setProfesion(0);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionActividadErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setActividad(0);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCodigoRegionNullErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCodigoRegion(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCodigoRegionEmptyErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCodigoRegion("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCodigoComunaNullErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCodigoComuna(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCodigoComunaEmptyErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCodigoComuna("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCalleNullErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCalle(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionCalleEmptyErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setCalle("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionNunmeroNullErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setNumero(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteValidacionNumeroEmptyErrorTest() {
        RegistroClienteRequestDTO request = request();
        request.setNumero("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteProfesionErrorTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);


        Optional<ParProfesionEntity> profesionEntityOptional = Optional.ofNullable(null);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }


    @Test
    void registroDatosClienteNacionalidadErrorTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.ofNullable(null);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteEstadoCivilErrorTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        ParNacionalidadEntity parNacionalidadEntity = parNacionalidadEntity();
        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.of(parNacionalidadEntity);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = Optional.ofNullable(null);
        when(parEstadoCivilRepository.findById(request.getEstadoCivil())).thenReturn(estadoCivilEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteAcitividadErrorTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        ParNacionalidadEntity parNacionalidadEntity = parNacionalidadEntity();
        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.of(parNacionalidadEntity);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        ParEstadoCivilEntity parEstadoCivilEntity = parEstadoCivilEntity();
        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = Optional.of(parEstadoCivilEntity);
        when(parEstadoCivilRepository.findById(request.getEstadoCivil())).thenReturn(estadoCivilEntityOptional);


        Optional<ParActividadEntity> actividadEntityOptional = Optional.ofNullable(null);
        when(parActividadRepository.findById(request.getActividad())).thenReturn(actividadEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    @Test
    void registroDatosClienteErrorTest() {

        RegistroClienteRequestDTO request = request();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        ParNacionalidadEntity parNacionalidadEntity = parNacionalidadEntity();
        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.of(parNacionalidadEntity);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        ParEstadoCivilEntity parEstadoCivilEntity = parEstadoCivilEntity();
        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = Optional.of(parEstadoCivilEntity);
        when(parEstadoCivilRepository.findById(request.getEstadoCivil())).thenReturn(estadoCivilEntityOptional);

        ParActividadEntity parActividadEntity = parActividadEntity();
        Optional<ParActividadEntity> actividadEntityOptional = Optional.of(parActividadEntity);
        when(parActividadRepository.findById(request.getActividad())).thenReturn(actividadEntityOptional);

        Optional<ClienteEntity> clienteEntityOptional = Optional.ofNullable(null);
        when(clienteRepository.findById(rutFormateado)).thenReturn(clienteEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> clienteService.registroDatosCliente(request))
                .withNoCause();
    }

    //@Test
    void registroDatosClienteCompletoTest() {

        RegistroClienteRequestDTO request = requestCompleto();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParProfesionEntity parProfesionEntity = parProfesionEntity();
        Optional<ParProfesionEntity> profesionEntityOptional = Optional.of(parProfesionEntity);
        when(parProfesionRepository.findById(request.getProfesion())).thenReturn(profesionEntityOptional);

        ParNacionalidadEntity parNacionalidadEntity = parNacionalidadEntity();
        Optional<ParNacionalidadEntity> nacionalidadEntityOptional = Optional.of(parNacionalidadEntity);
        when(parNacionalidadRepository.findById(request.getNacionalidad())).thenReturn(nacionalidadEntityOptional);

        ParEstadoCivilEntity parEstadoCivilEntity = parEstadoCivilEntity();
        Optional<ParEstadoCivilEntity> estadoCivilEntityOptional = Optional.of(parEstadoCivilEntity);
        when(parEstadoCivilRepository.findById(request.getEstadoCivil())).thenReturn(estadoCivilEntityOptional);

        ParActividadEntity parActividadEntity = parActividadEntity();
        Optional<ParActividadEntity> actividadEntityOptional = Optional.of(parActividadEntity);
        when(parActividadRepository.findById(request.getActividad())).thenReturn(actividadEntityOptional);

        ClienteEntity clienteEntity = clienteEntity();
        Optional<ClienteEntity> clienteEntityOptional = Optional.of(clienteEntity);
        when(clienteRepository.findById(rutFormateado)).thenReturn(clienteEntityOptional);

        TarjetaEntity tarjetaEntity = tarjetaEntity();
        Optional<TarjetaEntity> tarjetaEntityOptional = Optional.of(tarjetaEntity);
        when(tarjetaRepository.findByClienteEntityAndVigenciaIsTrue(clienteEntity)).thenReturn(tarjetaEntityOptional);


        CYCCrearCuentaRequestDTO cycCrearCuentaRequestDTO = cycCrearCuentaRequestDTO();
        CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO = cycCrearCuentaResponseDTO();
        when(cycAdapter.crearCuentaCYC(cycCrearCuentaRequestDTO)).thenReturn(cycCrearCuentaResponseDTO);

        clienteService.registroDatosCliente(request);
    }




    @Test
    void valdiarClienteTarjetaActivaTest() {
        ClienteActivoRequestDTO request = new ClienteActivoRequestDTO();
        request.setRut("11111111-1");

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        TarjetaActivacionEntity tarjetaActivacionEntity = datosTarjetaActivacionEntity();
        Optional<TarjetaActivacionEntity> tarjetaActivacionEntityOptional = Optional.of(tarjetaActivacionEntity);
        when(tarjetaActivacionRepository.validarClienteTarjetaActiva(rutFormateado)).thenReturn(tarjetaActivacionEntityOptional);


        clienteService.validarClienteTarjetaActiva(request);
    }

    @Test
    void valdiarClienteTarjetaActivaNoContentErrorTest() {
        ClienteActivoRequestDTO request = new ClienteActivoRequestDTO();
        request.setRut("11111111-1");

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<TarjetaActivacionEntity> tarjetaActivacionEntityOptional = Optional.ofNullable(null);
        when(tarjetaActivacionRepository.validarClienteTarjetaActiva(rutFormateado)).thenReturn(tarjetaActivacionEntityOptional);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> clienteService.validarClienteTarjetaActiva(request))
                .withNoCause();
    }

    private EvaluacionProductoEntity evaluacionProductoEntity() {
        EvaluacionProductoEntity evaluacionProductoEntity = new EvaluacionProductoEntity();
        evaluacionProductoEntity.setEvaluacionProductoId(1l);
        evaluacionProductoEntity.setParTipoProductoEntity(parTipoProductoEntity());
        evaluacionProductoEntity.setVigencia(true);
        return evaluacionProductoEntity;
    }


    private ParTipoProductoEntity parTipoProductoEntity() {
        ParTipoProductoEntity parTipoProductoEntity = new ParTipoProductoEntity();
        parTipoProductoEntity.setTipoProductoId(1l);
        parTipoProductoEntity.setDescripcion("Mastercard Full");

        return parTipoProductoEntity;
    }

    private TarjetaActivacionEntity datosTarjetaActivacionEntity() {

        TarjetaActivacionEntity tarjetaActivacionEntity = new TarjetaActivacionEntity();
        tarjetaActivacionEntity.setActivacionId(1l);
        tarjetaActivacionEntity.setTarjetaEntity(datosTarjetaEntity());
        tarjetaActivacionEntity.setFechaIngreso(LocalDateTime.now());
        tarjetaActivacionEntity.setUsuarioIngreso("USR_TMP");
        return tarjetaActivacionEntity;
    }

    private TarjetaEntity datosTarjetaEntity() {
        TarjetaEntity tarjetaEntity = new TarjetaEntity();
        tarjetaEntity.setTarjetaId(1l);
        tarjetaEntity.setClienteEntity(clienteEntity());
        tarjetaEntity.setVigencia(true);
        tarjetaEntity.setNumeroTarjeta(1234l);
        tarjetaEntity.setEvaluacionProductoEntity(evaluacionProductoEntity());
        return tarjetaEntity;
    }

    private ClienteEntity clienteEntity() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setRut("11111111-1");
        clienteEntity.setNombre("Juan");
        clienteEntity.setApellidoPaterno("Perez");
        clienteEntity.setApellidoMaterno("Soto");
        clienteEntity.setFechaNacimiento(LocalDate.now().minusYears(30));
        clienteEntity.setProspectoEntity(prospectoEntity());
        clienteEntity.setEmail("a@b.cl");
        return clienteEntity;
    }

    private ProspectoEntity prospectoEntity() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setFechaNacimiento(LocalDate.now().minusYears(30));
        prospectoEntity.setVigencia(true);
        prospectoEntity.setRut("11111111-1");
        return prospectoEntity;
    }


    private ParActividadEntity parActividadEntity() {
        ParActividadEntity parActividadEntity = new ParActividadEntity();
        parActividadEntity.setActividadId(1l);
        parActividadEntity.setDescripcion("Dependiente");
        parActividadEntity.setVigencia(true);
        return parActividadEntity;
    }

    private RegistroClienteRequestDTO request() {
        var request = new RegistroClienteRequestDTO();
        request.setRut("11111111-1");
        request.setCalle("Av uno");
        request.setActividad(1);
        request.setCodigoComuna("1301");
        request.setCodigoRegion("13");
        request.setEnvioEcc(true);
        request.setEnvioSMS(false);
        request.setNumero("23");
        request.setProfesion(2);
        request.setNacionalidad(1);
        request.setEstadoCivil(1);

        return request;
    }

    private RegistroClienteRequestDTO requestCompleto() {
        var request = new RegistroClienteRequestDTO();
        request.setRut("11111111-1");
        request.setCalle("Av uno");
        request.setActividad(1);
        request.setCodigoComuna("1301");
        request.setCodigoRegion("13");
        request.setEnvioEcc(true);
        request.setEnvioSMS(false);
        request.setNumero("23");
        request.setProfesion(2);
        request.setNacionalidad(1);
        request.setEstadoCivil(1);
        request.setLatitud(-33.45694f);
        request.setLongitud(-70.64827f);
        request.setTelefonoAdicional("987654321");
        request.setDepartamento("3F");

        return request;
    }

    private ParEstadoCivilEntity parEstadoCivilEntity() {
        ParEstadoCivilEntity parEstadoCivilEntity = new ParEstadoCivilEntity();
        parEstadoCivilEntity.setEstadoCivilId(1l);
        parEstadoCivilEntity.setDescripcion("Soltero");
        parEstadoCivilEntity.setVigencia(true);

        return parEstadoCivilEntity;
    }

    private ParNacionalidadEntity parNacionalidadEntity() {
        ParNacionalidadEntity parNacionalidadEntity = new ParNacionalidadEntity();
        parNacionalidadEntity.setNacionalidadId(1l);
        parNacionalidadEntity.setDescripcion("Chilena");
        parNacionalidadEntity.setVigencia(true);
        return parNacionalidadEntity;
    }

    private ParProfesionEntity parProfesionEntity() {
        ParProfesionEntity parProfesionEntity = new ParProfesionEntity();
        parProfesionEntity.setProfesionId(1l);
        parProfesionEntity.setVigencia(true);
        parProfesionEntity.setDescripcion("Arquitecto");

        return parProfesionEntity;
    }

    private CYCCrearCuentaRequestDTO cycCrearCuentaRequestDTO() {
        CYCCrearCuentaRequestDTO cycCrearCuentaRequestDTO = new CYCCrearCuentaRequestDTO();
        cycCrearCuentaRequestDTO.setDatosCreacionCuenta(cycDatosCreacionCuentaDTO());
        return cycCrearCuentaRequestDTO;
    }


    private CYCDatosCreacionCuentaDTO cycDatosCreacionCuentaDTO() {
        CYCDatosCreacionCuentaDTO cycDatosCreacionCuentaDTO = new CYCDatosCreacionCuentaDTO();
        cycDatosCreacionCuentaDTO.setHora("170000");
        cycDatosCreacionCuentaDTO.setDiaPago("5");
        cycDatosCreacionCuentaDTO.setFecha("20220816");
        cycDatosCreacionCuentaDTO.setCodigoEstadoCuenta("A123");
        cycDatosCreacionCuentaDTO.setTipoCliente("TI");
        cycDatosCreacionCuentaDTO.setCupoAsignado("300000");
        cycDatosCreacionCuentaDTO.setRutUsuario("111111111");
        cycDatosCreacionCuentaDTO.setNumeroTarjeta("7111111111");
        cycDatosCreacionCuentaDTO.setNumeroOperacion(String.valueOf(Instant.now().getEpochSecond()));
        cycDatosCreacionCuentaDTO.setDatosPersonales(cycDatosPersonalesDTO());
        cycDatosCreacionCuentaDTO.setDatosDemograficos(cycDatosDemograficosDTO());
        return cycDatosCreacionCuentaDTO;
    }

    private CYCDatosDemograficosDTO cycDatosDemograficosDTO() {
        CYCDatosDemograficosDTO cycDatosDemograficosDTO = new CYCDatosDemograficosDTO();
        cycDatosDemograficosDTO.setCiudad("");
        cycDatosDemograficosDTO.setCasilla("");
        cycDatosDemograficosDTO.setRegion("08");
        cycDatosDemograficosDTO.setComuna("08101");
        cycDatosDemograficosDTO.setComentarioAdicional("");
        cycDatosDemograficosDTO.setNumero("133");
        cycDatosDemograficosDTO.setCodigoPais("56");
        cycDatosDemograficosDTO.setCalle("Ohiggins");
        return cycDatosDemograficosDTO;
    }


    private CYCDatosPersonalesDTO cycDatosPersonalesDTO() {
        CYCDatosPersonalesDTO cycDatosPersonalesDTO = new CYCDatosPersonalesDTO();
        cycDatosPersonalesDTO.setSexo("M");
        cycDatosPersonalesDTO.setSegundoNombre("PEDRO");
        cycDatosPersonalesDTO.setRenta("1300000");
        cycDatosPersonalesDTO.setCondicionLaboral("A");
        cycDatosPersonalesDTO.setNumeroTelefono("133");
        cycDatosPersonalesDTO.setEmail("a@b.cl");
        cycDatosPersonalesDTO.setPrimerNombre("JUAN");
        cycDatosPersonalesDTO.setRutCliente("111111111");
        cycDatosPersonalesDTO.setNacionalidad("56");
        cycDatosPersonalesDTO.setProfesion("A");
        cycDatosPersonalesDTO.setFechaNacimiento("1985-01-01");
        cycDatosPersonalesDTO.setApellidoPaterno("Perez");
        cycDatosPersonalesDTO.setApellidoMaterno("SOTO");
        cycDatosPersonalesDTO.setProfesion("A");
        cycDatosPersonalesDTO.setEstadoCivil("S");

        return cycDatosPersonalesDTO;
    }

    private CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO() {
        CYCCrearCuentaResponseDTO cycCrearCuentaResponseDTO = new CYCCrearCuentaResponseDTO();
        cycCrearCuentaResponseDTO.setCodigo("00");
        cycCrearCuentaResponseDTO.setDescripcion("OK");
        return cycCrearCuentaResponseDTO;
    }

    private TarjetaEntity tarjetaEntity() {
        TarjetaEntity tarjetaEntity = new TarjetaEntity();
        tarjetaEntity.setDiaPago(5);
        tarjetaEntity.setCupoAprobado(400000);
        tarjetaEntity.setVigencia(true);
        tarjetaEntity.setClienteEntity(clienteEntity());
        tarjetaEntity.setNumeroTarjeta(7111111111l);
        tarjetaEntity.setFechaIngreso(LocalDateTime.now());
        tarjetaEntity.setUsuarioIngreso("USR_TMP");
        return tarjetaEntity;
    }
}
