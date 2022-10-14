package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CampanaCoronaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampanaCoronaRepository extends JpaRepository<CampanaCoronaEntity, Long> {
    @Query("select c from CampanaCoronaEntity c where c.rut = :rut and c.vigencia = true and current_date between c.cabeceraCampanaEntity.fechaInicio and c.cabeceraCampanaEntity.fechaTermino")
    Optional<CampanaCoronaEntity> esCampanaVigente(String rut);

    List<CampanaCoronaEntity> findAllByCabeceraCampanaEntityCabeceraCampanaId(long cabeceraCampaniaId);
}
