package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParFaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParFaseRepository extends JpaRepository<ParFaseEntity, Long> {
    List<ParFaseEntity> findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId();

    List<ParFaseEntity> findAllByVigenciaIsTrueAndFasePadreIdOrderByFaseId(long faseId);
}
