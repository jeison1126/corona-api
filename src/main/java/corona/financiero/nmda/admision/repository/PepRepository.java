package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.PepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PepRepository extends JpaRepository<PepEntity, String> {
}
