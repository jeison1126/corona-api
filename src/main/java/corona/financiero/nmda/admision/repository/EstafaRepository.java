package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.EstafaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstafaRepository extends JpaRepository<EstafaEntity, String> {
}
