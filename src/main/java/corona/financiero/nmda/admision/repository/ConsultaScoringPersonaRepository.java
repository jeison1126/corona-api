package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ConsultaScoringEntity;
import corona.financiero.nmda.admision.entity.ConsultaScoringPersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaScoringPersonaRepository extends JpaRepository<ConsultaScoringPersonaEntity, Long> {
    ConsultaScoringPersonaEntity findByConsultaScoringEntity(ConsultaScoringEntity consultaScoringEntity);
}
