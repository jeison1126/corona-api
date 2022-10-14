package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CabeceraCampanaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CabeceraCampanaRepository extends JpaRepository<CabeceraCampanaEntity, Long> {
}
