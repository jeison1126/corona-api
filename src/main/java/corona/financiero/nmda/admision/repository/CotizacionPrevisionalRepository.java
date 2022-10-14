package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CotizacionPrevisionalEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionPrevisionalRepository extends JpaRepository<CotizacionPrevisionalEntity, Long> {
    List<CotizacionPrevisionalEntity> findAllByProspectoEntity(ProspectoEntity prospectoEntity);
}
