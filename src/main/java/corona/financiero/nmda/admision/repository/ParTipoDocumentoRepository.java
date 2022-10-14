package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParTipoDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParTipoDocumentoRepository extends JpaRepository<ParTipoDocumentoEntity, Long> {
}
