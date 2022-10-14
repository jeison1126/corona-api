package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.AdmisionFaseEntity;
import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.repository.AdmisionFaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdmisionFaseServiceTest {


    @InjectMocks
    private AdmisionFaseService admisionFaseService;
    @Mock
    private AdmisionFaseRepository admisionFaseRepository;


    @Test
    void actualizarFaseSolicitudTest() {
        AdmisionFaseEntity admisionFaseEntity = obtieneAdmisionFaseEntity();
        when(admisionFaseRepository.save(any(AdmisionFaseEntity.class))).thenReturn(admisionFaseEntity);


        admisionFaseService.actualizarFaseSolicitud(obtieneSolicitudAdmisionEntity(), 1l);

    }

    private AdmisionFaseEntity obtieneAdmisionFaseEntity() {
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setParFaseEntity(obtieneParFaseEntity());
        admisionFaseEntity.setAdmisionFaseId(1l);
        admisionFaseEntity.setFechaRegistro(LocalDateTime.now());
        admisionFaseEntity.setSolicitudAdmisionEntity(obtieneSolicitudAdmisionEntity());
        return admisionFaseEntity;
    }

    private SolicitudAdmisionEntity obtieneSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        return solicitudAdmisionEntity;
    }

    private ParFaseEntity obtieneParFaseEntity() {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1l);
        parFaseEntity.setDescripcion("Evaluacion Cliente");
        parFaseEntity.setVigencia(true);
        return parFaseEntity;
    }
}
