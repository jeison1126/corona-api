package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.AdmisionFaseEntity;
import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.repository.AdmisionFaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AdmisionFaseService {
    @Autowired
    private AdmisionFaseRepository admisionFaseRepository;

    private static final String USUARIO_TEMPORAL = "USR_TMP";

    public void actualizarFaseSolicitud(SolicitudAdmisionEntity solicitudAdmisionEntity, long fase) {
        log.debug("Se registra fase : {}", fase);
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(fase);

        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setParFaseEntity(parFaseEntity);
        admisionFaseEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity);
        admisionFaseEntity.setVigencia(true);
        admisionFaseEntity.setFechaRegistro(LocalDateTime.now());
        admisionFaseEntity.setUsuarioRegistro(USUARIO_TEMPORAL);

        admisionFaseRepository.save(admisionFaseEntity);
        admisionFaseRepository.flush();

    }
}
