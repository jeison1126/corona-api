package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.CallIPSMSAdapter;
import corona.financiero.nmda.admision.dto.MFARequestDTO;
import corona.financiero.nmda.admision.dto.ValidarMFARequestDTO;
import corona.financiero.nmda.admision.entity.MFAEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.MFARepository;
import corona.financiero.nmda.admision.repository.ProspectoRepository;
import corona.financiero.nmda.admision.repository.SolicitudAdmisionRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MFAServiceTest {

    @InjectMocks
    private MFAService mfaService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private MFARepository mfaRepository;

    @Mock
    private ProspectoRepository prospectoRepository;

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Mock
    private CallIPSMSAdapter callIPSMSAdapter;

    @Test
    void enviarSMSOKTest() {

        var request = obtenerMFARequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        Optional<MFAEntity> mfaEntityOptional = Optional.ofNullable(null);
        when(mfaRepository.findBySolicitudAdmisionEntityAndValidadoIsTrueAndVigenciaIsTrue(any())).thenReturn(mfaEntityOptional);

        List<MFAEntity> mfaAnteriores = new ArrayList<>();
        when(mfaRepository.findAllBySolicitudAdmisionEntityProspectoEntityRut(any())).thenReturn(mfaAnteriores);

        doNothing().when(callIPSMSAdapter).enviarSMS(any());

        mfaService.validacionPreviaDesdeController(request);
    }

    @Test
    void enviarSMSErrorRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validacionPreviaDesdeController(null))
                .withNoCause();
    }

    @Test
    void enviarSMSRequestRutNullErrorTest() {
        var request = obtenerMFARequestDTO();
        request.setRut(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.enviarSMS(request))
                .withNoCause();
    }

    @Test
    void enviarSMSRequestRutEmptyErrorTest() {
        var request = obtenerMFARequestDTO();
        request.setRut("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.enviarSMS(request))
                .withNoCause();
    }

    @Test
    void enviarSMSRequestInvalidRutErrorTest() {
        var request = obtenerMFARequestDTO();
        request.setRut("1234-2");
        when(validaciones.validaRut(request.getRut())).thenReturn(false);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validacionPreviaDesdeController(request))
                .withNoCause();
    }

    @Test
    void enviarSMSProspectoErrorTest() {

        var request = obtenerMFARequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validacionPreviaDesdeController(request))
                .withNoCause();

    }

    @Test
    void enviarSMSMFAErrorTest() {

        var request = obtenerMFARequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        MFAEntity mfaEntity = obtieneMFAEntity();
        Optional<MFAEntity> mfaEntityOptional = Optional.of(mfaEntity);
        when(mfaRepository.findBySolicitudAdmisionEntityAndValidadoIsTrueAndVigenciaIsTrue(any())).thenReturn(mfaEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validacionPreviaDesdeController(request))
                .withNoCause();

    }


    @Test
    void enviarSMSInvalidarMFAAnterioresOKTest() {

        var request = obtenerMFARequestDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(prospectoEntity.getProspectoId())).thenReturn(solicitudAdmisionEntity);

        Optional<MFAEntity> mfaEntityOptional = Optional.ofNullable(null);
        when(mfaRepository.findBySolicitudAdmisionEntityAndValidadoIsTrueAndVigenciaIsTrue(any())).thenReturn(mfaEntityOptional);

        MFAEntity mfaEntity = obtieneMFAEntity();
        List<MFAEntity> mfaAnteriores = Arrays.asList(mfaEntity);

        when(mfaRepository.findAllBySolicitudAdmisionEntityProspectoEntityRut(any())).thenReturn(mfaAnteriores);

        doNothing().when(callIPSMSAdapter).enviarSMS(any());

        mfaService.validacionPreviaDesdeController(request);
    }


    @Test
    void validarSMSOKTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();

        when(validaciones.validaRut(validarMFARequestDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(validarMFARequestDTO.getRut())).thenReturn(rutFormateado);

        MFAEntity mfaEntity = obtieneMFAEntity();
        Optional<MFAEntity> mfaEntityOptional = Optional.of(mfaEntity);
        when(mfaRepository.validarCodigoSMS(rutFormateado, validarMFARequestDTO.getCodigo())).thenReturn(mfaEntityOptional);

        mfaService.validarSMS(validarMFARequestDTO);
    }

    @Test
    void validarSMSErrorRutNullTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();
        validarMFARequestDTO.setRut(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validarSMS(validarMFARequestDTO))
                .withNoCause();
    }

    @Test
    void validarSMSErrorRutEmptyTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();
        validarMFARequestDTO.setRut("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validarSMS(validarMFARequestDTO))
                .withNoCause();
    }

    @Test
    void validarSMSErrorCodigoNullTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();
        when(validaciones.validaRut(validarMFARequestDTO.getRut())).thenReturn(true);
        validarMFARequestDTO.setCodigo(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validarSMS(validarMFARequestDTO))
                .withNoCause();
    }

    @Test
    void validarSMSErrorCodigoEmptyTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();
        when(validaciones.validaRut(validarMFARequestDTO.getRut())).thenReturn(true);
        validarMFARequestDTO.setCodigo("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validarSMS(validarMFARequestDTO))
                .withNoCause();
    }

    @Test
    void validarSMSErrorMFANotExistTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();

        when(validaciones.validaRut(validarMFARequestDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(validarMFARequestDTO.getRut())).thenReturn(rutFormateado);

        Optional<MFAEntity> mfaEntityOptional = Optional.ofNullable(null);
        when(mfaRepository.validarCodigoSMS(rutFormateado, validarMFARequestDTO.getCodigo())).thenReturn(mfaEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.validarSMS(validarMFARequestDTO))
                .withNoCause();
    }

    @Test
    void validarSMSPreviamenteOKTest() {

        ValidarMFARequestDTO validarMFARequestDTO = obtieneValidarMFARequestDTO();

        when(validaciones.validaRut(validarMFARequestDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(validarMFARequestDTO.getRut())).thenReturn(rutFormateado);

        MFAEntity mfaEntity = obtieneMFAEntity();
        mfaEntity.setValidado(true);
        Optional<MFAEntity> mfaEntityOptional = Optional.of(mfaEntity);
        when(mfaRepository.validarCodigoSMS(rutFormateado, validarMFARequestDTO.getCodigo())).thenReturn(mfaEntityOptional);

        mfaService.validarSMS(validarMFARequestDTO);
    }

    @Test
    void verificaValidacionMFAOKTest() {
        MFARequestDTO requestDTO = obtenerMFARequestDTO();
        when(validaciones.validaRut(requestDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRut())).thenReturn(rutFormateado);

        MFAEntity mfaEntity = new MFAEntity();
        mfaEntity.setMfaId(1l);

        Optional<MFAEntity> mfaEntityOptional = Optional.of(mfaEntity);
        when(mfaRepository.verificaValidacionMFA(rutFormateado)).thenReturn(mfaEntityOptional);

        mfaService.verificaValidacionMFA(requestDTO);
    }

    @Test
    void verificaValidacionMFAErrorTest() {
        MFARequestDTO requestDTO = obtenerMFARequestDTO();
        when(validaciones.validaRut(requestDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRut())).thenReturn(rutFormateado);

        Optional<MFAEntity> mfaEntityOptional = Optional.ofNullable(null);
        when(mfaRepository.verificaValidacionMFA(rutFormateado)).thenReturn(mfaEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mfaService.verificaValidacionMFA(requestDTO))
                .withNoCause();
    }

    private ValidarMFARequestDTO obtieneValidarMFARequestDTO() {
        ValidarMFARequestDTO validarMFARequestDTO = new ValidarMFARequestDTO();
        validarMFARequestDTO.setCodigo("12sF4");
        validarMFARequestDTO.setRut("11111111-1");
        return validarMFARequestDTO;
    }


    private MFARequestDTO obtenerMFARequestDTO() {
        MFARequestDTO mfaRequestDTO = new MFARequestDTO();
        mfaRequestDTO.setRut("11111111-1");

        return mfaRequestDTO;
    }

    private SolicitudAdmisionEntity obtieneSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        solicitudAdmisionEntity.setProspectoEntity(obtieneProspectoEntity());
        return solicitudAdmisionEntity;
    }

    private ProspectoEntity obtieneProspectoEntity() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setVigencia(true);
        return prospectoEntity;
    }

    private MFAEntity obtieneMFAEntity() {
        MFAEntity mfaEntity = new MFAEntity();
        mfaEntity.setMfaId(1l);
        mfaEntity.setSolicitudAdmisionEntity(obtieneSolicitudAdmisionEntity());
        mfaEntity.setValidado(false);
        mfaEntity.setFechaRegistro(LocalDateTime.now());
        mfaEntity.setVigencia(true);
        return mfaEntity;
    }
}