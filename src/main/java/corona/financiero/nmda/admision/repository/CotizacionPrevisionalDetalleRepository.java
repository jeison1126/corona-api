package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CotizacionPrevisionalDetalleEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionPrevisionalDetalleRepository extends JpaRepository<CotizacionPrevisionalDetalleEntity, Long> {
    List<CotizacionPrevisionalDetalleEntity> findAllByCotizacionPrevisionalEntityProspectoEntityAndCotizacionPrevisionalEntityVigenciaIsTrue(ProspectoEntity prospectoEntity);
}
