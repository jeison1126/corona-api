package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CamaraComercioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamaraComercioRepository extends JpaRepository<CamaraComercioEntity, String> {
}
