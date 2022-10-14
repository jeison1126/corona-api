package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.CallIPSMSAdapter;
import corona.financiero.nmda.admision.dto.MFARequestDTO;
import corona.financiero.nmda.admision.dto.ValidarMFARequestDTO;
import corona.financiero.nmda.admision.dto.sms.CallIPSMSRequestDTO;
import corona.financiero.nmda.admision.entity.MFAEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.MFARepository;
import corona.financiero.nmda.admision.repository.ProspectoRepository;
import corona.financiero.nmda.admision.repository.SolicitudAdmisionRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MFAService {

    @Autowired
    private MFARepository mfaRepository;

    @Autowired
    private ProspectoRepository prospectoRepository;

    @Autowired
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Autowired
    private Validaciones validaciones;

    @Autowired
    private CallIPSMSAdapter callIPSMSAdapter;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public void validacionPreviaDesdeController(MFARequestDTO request) {
        validarRequest(request);
        enviarSMS(request);
    }

    @Async
    public void enviarSMS(MFARequestDTO request) {

        log.debug("Notificacion SMS...");

        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        ProspectoEntity prospectoEntity = prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado);

        if (prospectoEntity == null) {
            throw new BadRequestException("No existen registros");
        }

        SolicitudAdmisionEntity solicitudAdmisionEntity = solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(prospectoEntity.getProspectoId());

        Optional<MFAEntity> mfaValidado = mfaRepository.findBySolicitudAdmisionEntityAndValidadoIsTrueAndVigenciaIsTrue(solicitudAdmisionEntity);
        if (mfaValidado.isPresent()) {
            throw new BadRequestException("Ya se encuentra validado");
        }

        List<MFAEntity> mfaAnteriores = mfaRepository.findAllBySolicitudAdmisionEntityProspectoEntityRut(rutFormateado);

        if (!mfaAnteriores.isEmpty()) {
            mfaAnteriores.stream().forEach(m -> {
                m.setVigencia(false);
                m.setFechaModificacion(LocalDateTime.now());
                m.setUsuarioModificacion(USUARIO_TEMPORAL);
            });

            log.debug("Invalidando MFA anteriores...");
            mfaRepository.saveAll(mfaAnteriores);
            mfaRepository.flush();
        }

        String codigoGenerado = RandomStringUtils.randomAlphanumeric(5, 5);
        MFAEntity mfaEntity = new MFAEntity();
        mfaEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        mfaEntity.setFechaRegistro(LocalDateTime.now());
        mfaEntity.setUsuarioRegistro(USUARIO_TEMPORAL);
        mfaEntity.setCodigo(codigoGenerado);
        mfaEntity.setVigencia(true);

        log.debug("Guardando codigo MFA generado");
        mfaRepository.save(mfaEntity);

        log.debug("Notificando via SMS...");
        CallIPSMSRequestDTO callIPSMSRequestDTO = new CallIPSMSRequestDTO();
        callIPSMSRequestDTO.setDni(String.valueOf(prospectoEntity.getMovil()));
        String mensaje = "SMS, Autorizado, codigo de activacion: " + codigoGenerado;
        callIPSMSRequestDTO.setMessage(mensaje);
        callIPSMSAdapter.enviarSMS(callIPSMSRequestDTO);


    }

    private void validarRequest(MFARequestDTO request) {
        if (request == null) {
            throw new BadRequestException();
        }

        if (request.getRut() == null || request.getRut().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar un rut");
        }
        if (!validaciones.validaRut(request.getRut())) {
            throw new BadRequestException("El rut ingresado no es valido");
        }
    }


    public void validarSMS(ValidarMFARequestDTO request) {
        log.debug("Validar SMS...");
        validarRequest(request);
        if (request.getCodigo() == null || request.getCodigo().trim().isEmpty()) {
            throw new BadRequestException("Debe ingresar el codigo enviado por SMS");
        }
        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());

        Optional<MFAEntity> mfaEntityOptional = mfaRepository.validarCodigoSMS(rutFormateado, request.getCodigo());

        if (mfaEntityOptional.isEmpty()) {
            log.error("No se encontro MFA para validar");
            throw new BadRequestException("No se encontro MFA para validar");
        }
        MFAEntity mfaEntity = mfaEntityOptional.get();

        if (mfaEntity.isValidado()) {
            log.debug("MFA ya fue validado");
            return;
        }
        mfaEntity.setValidado(true);
        mfaEntity.setUsuarioModificacion(USUARIO_TEMPORAL);
        mfaEntity.setFechaModificacion(LocalDateTime.now());

        mfaRepository.save(mfaEntity);
        log.debug("Codigo Validado!");
    }

    public void verificaValidacionMFA(MFARequestDTO request) {
        validarRequest(request);
        String rutFormateado = validaciones.formateaRutHaciaBD(request.getRut());
        Optional<MFAEntity> mfaEntityOptional = mfaRepository.verificaValidacionMFA(rutFormateado);

        if (mfaEntityOptional.isEmpty()) {
            throw new BadRequestException("No ha validado su numero de celular");
        }
    }
}
