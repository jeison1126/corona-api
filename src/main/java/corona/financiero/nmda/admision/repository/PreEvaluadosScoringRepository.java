package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.PreEvaluadosScoringEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreEvaluadosScoringRepository extends JpaRepository<PreEvaluadosScoringEntity, String> {
    @Query("select p from PreEvaluadosScoringEntity p where lower(p.rut) = lower(:rut) and p.vigencia = true")
    Optional<PreEvaluadosScoringEntity> esCampaniaVigente(String rut);
}
