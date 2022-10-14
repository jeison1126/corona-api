package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParOrigenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParOrigenRepository extends JpaRepository<ParOrigenEntity, Long> {
}
