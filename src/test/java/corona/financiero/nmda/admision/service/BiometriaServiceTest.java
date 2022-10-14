package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ActivarDispositivoRequestDTO;
import corona.financiero.nmda.admision.dto.ActivarDispositivoResponseDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BiometriaServiceTest {
    @Mock
    private Validaciones validaciones;


    @Mock
    private AdmisionFaseRepository admisionFaseRepository;

    @Mock
    private CargoAutorizadorExcepcionRepository cargoAutorizadorExcepcionRepository;

    @Mock
    private FuncionariosRepository funcionariosRepository;

    @Mock
    private BiometriaConsultaRepository biometriaConsultaRepository;

    @Mock
    private BiometriaFaseRepository biometriaFaseRepository;

    @InjectMocks
    private BiometriaService biometriaService;

    @Mock
    private CotizacionService cotizacionService;

    @Mock
    private MotivoCartaRechazoRepository motivoCartaRechazoRepository;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(biometriaService, "dispositivoBiometricoUri", "http://localhost:1760/bio");
    }

    @Test
    void activarBiometriaProspectoOKTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        AdmisionFaseEntity admisionFaseEntity = datosAdmisionFaseEntity();

        Optional<AdmisionFaseEntity> admisionFaseEntityOptional = Optional.of(admisionFaseEntity);

        when(admisionFaseRepository.validaSolicitudAdmisionYFaseActiva(rutFormateado, request.getSolicitudAdmisionId(), Constantes.FASE_EVALUACION_SCORING)).thenReturn(admisionFaseEntityOptional);

        biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING);

    }

    @Test
    void activarBiometriaProspecto_ErroerSolicitudAdmisionActivaTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<AdmisionFaseEntity> admisionFaseEntityOptional = Optional.ofNullable(null);

        when(admisionFaseRepository.validaSolicitudAdmisionYFaseActiva(rutFormateado, request.getSolicitudAdmisionId(), Constantes.FASE_EVALUACION_SCORING)).thenReturn(admisionFaseEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(null, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestRutNullErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        request.setRut(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestRutEmptyErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        request.setRut("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestRutvalidacionErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();

        when(validaciones.validaRut(request.getRut())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestSolicitudIdNullErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        request.setSolicitudAdmisionId(null);
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaProspecto_RequestSolicitudIdInvalidaErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        request.setSolicitudAdmisionId(-1l);
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaExcepcionOKTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = obtenerFuncionarioEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(funcionariosEntityOptional);

        CargoAutorizadorExcepcionEntity cargoAutorizadorExcepcionEntity = new CargoAutorizadorExcepcionEntity();
        cargoAutorizadorExcepcionEntity.setCargoId(1l);
        Optional<CargoAutorizadorExcepcionEntity> cargoAutorizadorExcepcionEntityOptional = Optional.of(cargoAutorizadorExcepcionEntity);
        when(cargoAutorizadorExcepcionRepository.validarCargoAutorizadorExcepcion(funcionariosEntity.getCargoId())).thenReturn(cargoAutorizadorExcepcionEntityOptional);


        AdmisionFaseEntity admisionFaseEntity = datosAdmisionFaseEntity();

        Optional<AdmisionFaseEntity> admisionFaseEntityOptional = Optional.of(admisionFaseEntity);

        when(admisionFaseRepository.validaSolicitudAdmisionYFaseActivaExcepcion(request.getSolicitudAdmisionId(), Constantes.FASE_EVALUACION_SCORING)).thenReturn(admisionFaseEntityOptional);

        biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING);

    }

    @Test
    void activarBiometriaExcepcion_SinEjecutivoErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);


        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.ofNullable(null);
        when(funcionariosRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(funcionariosEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaExcepcion_SinCargoErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = obtenerFuncionarioEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(funcionariosEntityOptional);

        Optional<CargoAutorizadorExcepcionEntity> cargoAutorizadorExcepcionEntityOptional = Optional.ofNullable(null);
        when(cargoAutorizadorExcepcionRepository.validarCargoAutorizadorExcepcion(funcionariosEntity.getCargoId())).thenReturn(cargoAutorizadorExcepcionEntityOptional);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void activarBiometriaExcepcion_AdmisionActivaErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        String rutFormateado = "111111111";
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = obtenerFuncionarioEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(funcionariosEntityOptional);

        CargoAutorizadorExcepcionEntity cargoAutorizadorExcepcionEntity = obtenerCargoAutorizadorExceptionEntity();
        Optional<CargoAutorizadorExcepcionEntity> cargoAutorizadorExcepcionEntityOptional = Optional.of(cargoAutorizadorExcepcionEntity);
        when(cargoAutorizadorExcepcionRepository.validarCargoAutorizadorExcepcion(funcionariosEntity.getCargoId())).thenReturn(cargoAutorizadorExcepcionEntityOptional);


        Optional<AdmisionFaseEntity> admisionFaseEntityOptional = Optional.ofNullable(null);
        when(admisionFaseRepository.validaSolicitudAdmisionYFaseActivaExcepcion(request.getSolicitudAdmisionId(), Constantes.FASE_EVALUACION_SCORING)).thenReturn(admisionFaseEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING))
                .withNoCause();

    }

    @Test
    void verificaValidacionBiometricaProspectoOKTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        BiometriaFaseEntity biometriaFaseEntity = new BiometriaFaseEntity();
        biometriaFaseEntity.setVigencia(true);
        biometriaFaseEntity.setFechaRegistro(LocalDateTime.now());
        Optional<BiometriaFaseEntity> biometriaFaseEntityOptional = Optional.of(biometriaFaseEntity);
        when(biometriaFaseRepository.verificaAprobacionValidacionBiometrica(rutFormateado, request.getSolicitudAdmisionId())).thenReturn(biometriaFaseEntityOptional);

        biometriaService.verificaValidacionBiometrica(request);
    }

    @Test
    void verificaValidacionBiometricaExcepcionOKTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<BiometriaFaseEntity> biometriaFaseEntityOptional = Optional.ofNullable(null);
        when(biometriaFaseRepository.verificaAprobacionValidacionBiometrica(rutFormateado, request.getSolicitudAdmisionId())).thenReturn(biometriaFaseEntityOptional);

        BiometriaConsultaEntity biometriaConsultaEntity = new BiometriaConsultaEntity();
        biometriaConsultaEntity.setFechaRegistro(LocalDateTime.now());
        biometriaConsultaEntity.setVigencia(true);
        Optional<BiometriaConsultaEntity> biometriaConsultaEntityOptional = Optional.of(biometriaConsultaEntity);
        when(biometriaConsultaRepository.verificaAprobacionValidacionExcepcion(request.getSolicitudAdmisionId())).thenReturn(biometriaConsultaEntityOptional);

        biometriaService.verificaValidacionBiometrica(request);
    }

    @Test
    void verificaValidacionBiometricaErrorTest() {
        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<BiometriaFaseEntity> biometriaFaseEntityOptional = Optional.ofNullable(null);
        when(biometriaFaseRepository.verificaAprobacionValidacionBiometrica(rutFormateado, request.getSolicitudAdmisionId())).thenReturn(biometriaFaseEntityOptional);

        Optional<BiometriaConsultaEntity> biometriaConsultaEntityOptional = Optional.ofNullable(null);
        when(biometriaConsultaRepository.verificaAprobacionValidacionExcepcion(request.getSolicitudAdmisionId())).thenReturn(biometriaConsultaEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.verificaValidacionBiometrica(request))
                .withNoCause();
    }

    @Test
    void activarDispositivoBiometricoCartaRechazoProspectoOKTest() {

        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();

        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        MotivoCartaRechazoEntity motivoCartaRechazoEntity = obtenerMotivoCartaRechazoEntity();
        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional = Optional.of(motivoCartaRechazoEntity);


        when(motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(request.getSolicitudAdmisionId(), rutFormateado)).thenReturn(motivoCartaRechazoEntityOptional);

        biometriaService.activarDispositivoBiometricoCartaRechazo(request, true);

    }

    @Test
    void activarDispositivoBiometricoCartaRechazoExcepcionOKTest() {

        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();

        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(funcionariosEntityOptional);

        CargoAutorizadorExcepcionEntity cargoAutorizadorExcepcionEntity = datosCargoAutorizadorExcepcionEntity();
        Optional<CargoAutorizadorExcepcionEntity> cargoAutorizadorExcepcionEntityOptional = Optional.of(cargoAutorizadorExcepcionEntity);

        when(cargoAutorizadorExcepcionRepository.validarCargoAutorizadorExcepcion(funcionariosEntity.getCargoId())).thenReturn(cargoAutorizadorExcepcionEntityOptional);


        MotivoCartaRechazoEntity motivoCartaRechazoEntity = obtenerMotivoCartaRechazoEntity();
        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional = Optional.of(motivoCartaRechazoEntity);

        when(motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudId(request.getSolicitudAdmisionId())).thenReturn(motivoCartaRechazoEntityOptional);

        biometriaService.activarDispositivoBiometricoCartaRechazo(request, false);

    }

    @Test
    void activarDispositivoBiometricoCartaRechazoProspectoErrorTest() {

        ActivarDispositivoRequestDTO request = obtieneActivarDispositivoRequestDTO();

        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional = Optional.ofNullable(null);

        when(motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(request.getSolicitudAdmisionId(), rutFormateado)).thenReturn(motivoCartaRechazoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> biometriaService.activarDispositivoBiometricoCartaRechazo(request, true))
                .withNoCause();

    }

    private CargoAutorizadorExcepcionEntity datosCargoAutorizadorExcepcionEntity() {
        CargoAutorizadorExcepcionEntity cargoAutorizadorExcepcionEntity = new CargoAutorizadorExcepcionEntity();
        cargoAutorizadorExcepcionEntity.setCargoId(302l);
        cargoAutorizadorExcepcionEntity.setDescripcion("Super usuario");
        cargoAutorizadorExcepcionEntity.setVigencia(true);
        cargoAutorizadorExcepcionEntity.setFechaRegistro(LocalDateTime.now());

        return cargoAutorizadorExcepcionEntity;
    }

    private FuncionariosEntity datosFuncionariosEntity() {
        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("111111111");
        funcionariosEntity.setVigencia(true);
        funcionariosEntity.setUsuarioRegistro("USR_TMP");
        funcionariosEntity.setCargoId(302l);
        funcionariosEntity.setNombreCargo("Super usuario");

        return funcionariosEntity;
    }

    private MotivoCartaRechazoEntity obtenerMotivoCartaRechazoEntity() {
        MotivoCartaRechazoEntity motivoCartaRechazoEntity = new MotivoCartaRechazoEntity();
        motivoCartaRechazoEntity.setSolicitudAdmisionEntity(datosSolicitudAdmisionEntity());
        motivoCartaRechazoEntity.setFechaRegistro(LocalDateTime.now());
        motivoCartaRechazoEntity.setUsuarioRegistro("USR_TMP");
        motivoCartaRechazoEntity.setMotivoCartarechazoId(1l);
        motivoCartaRechazoEntity.setDescripcion("MOrosoidad reportado por Dicom");

        return motivoCartaRechazoEntity;
    }

    private CargoAutorizadorExcepcionEntity obtenerCargoAutorizadorExceptionEntity() {
        CargoAutorizadorExcepcionEntity cargoAutorizadorExcepcionEntity = new CargoAutorizadorExcepcionEntity();
        cargoAutorizadorExcepcionEntity.setCargoId(1l);
        return cargoAutorizadorExcepcionEntity;
    }

    private FuncionariosEntity obtenerFuncionarioEntity() {
        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("999999999");
        funcionariosEntity.setCargoId(1l);

        return funcionariosEntity;
    }

    private ActivarDispositivoResponseDTO respuestaActivacionDispositivo(String rut) {
        ActivarDispositivoResponseDTO response = new ActivarDispositivoResponseDTO();
        response.setRut(rut);
        response.setType("POST");
        response.setUrl("http://localhost:1760/bio");

        return response;
    }

    private ActivarDispositivoRequestDTO obtieneActivarDispositivoRequestDTO() {
        ActivarDispositivoRequestDTO request = new ActivarDispositivoRequestDTO();
        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);
        return request;

    }

    private SolicitudAdmisionEntity datosSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());


        return solicitudAdmisionEntity;
    }

    private ParFaseEntity datosFasesEntity() {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1);
        parFaseEntity.setDescripcion("Iniciado");

        return parFaseEntity;
    }

    private AdmisionFaseEntity datosAdmisionFaseEntity() {
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1);
        admisionFaseEntity.setVigencia(true);
        admisionFaseEntity.setFechaRegistro(LocalDateTime.now());
        admisionFaseEntity.setParFaseEntity(datosFasesEntity());

        return admisionFaseEntity;
    }
}
