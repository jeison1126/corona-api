package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ConsultaScoringEntity;
import corona.financiero.nmda.admision.entity.ConsultaScoringRegistroMorosidadProtestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaScoringRegistroMorosidadProtestoRepository extends JpaRepository<ConsultaScoringRegistroMorosidadProtestoEntity, Long> {
    ConsultaScoringRegistroMorosidadProtestoEntity findByConsultaScoringEntity(ConsultaScoringEntity consultaScoringEntity);
}
