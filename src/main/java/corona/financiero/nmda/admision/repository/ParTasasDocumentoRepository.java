package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParTasasDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParTasasDocumentoRepository extends JpaRepository<ParTasasDocumentoEntity, Long> {
    Optional<ParTasasDocumentoEntity> findTopByVigenciaIsTrueOrderByFechaRegistroAsc();
}
