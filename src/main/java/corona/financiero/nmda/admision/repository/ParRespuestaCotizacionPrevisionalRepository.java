package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParRespuestaCotizacionPrevisionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParRespuestaCotizacionPrevisionalRepository extends JpaRepository<ParRespuestaCotizacionPrevisionalEntity, Long> {
}
