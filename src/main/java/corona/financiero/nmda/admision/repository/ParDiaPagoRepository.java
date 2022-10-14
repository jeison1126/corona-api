package corona.financiero.nmda.admision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import corona.financiero.nmda.admision.entity.ParDiaPagoEntity;

@Repository
public interface ParDiaPagoRepository extends JpaRepository<ParDiaPagoEntity, Long> {
    List<ParDiaPagoEntity> findAllByVigenciaIsTrueOrderByDiaPagoId();

    @Query("SELECT dp FROM ParDiaPagoEntity dp where dp.vigencia is true and dp.diaPagoId = :diaPagoId ")
	Optional<ParDiaPagoEntity> recuperarDiaPagoVigenteById(long diaPagoId);
}
