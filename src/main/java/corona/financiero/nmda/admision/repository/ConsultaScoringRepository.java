package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ConsultaScoringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaScoringRepository extends JpaRepository<ConsultaScoringEntity, Long> {
    @Query("select c FROM ConsultaScoringEntity c where c.prospectoEntity.rut = :rut and c.vigencia is true")
    List<ConsultaScoringEntity> buscarRegistroEquifaxVigente(String rut);
}
