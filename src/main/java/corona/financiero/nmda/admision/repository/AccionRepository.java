package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.AccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionRepository extends JpaRepository<AccionEntity, Long> {
}
