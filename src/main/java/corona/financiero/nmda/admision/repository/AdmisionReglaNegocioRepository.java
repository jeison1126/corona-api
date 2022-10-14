package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.AdmisionReglaNegocioEntity;
import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmisionReglaNegocioRepository extends JpaRepository<AdmisionReglaNegocioEntity, Long> {
    List<AdmisionReglaNegocioEntity> findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(SolicitudAdmisionEntity solicitudAdmisionEntity, ParFaseEntity parFaseEntity);
}
