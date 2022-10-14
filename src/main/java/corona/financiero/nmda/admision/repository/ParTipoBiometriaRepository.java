package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParTipoBiometriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParTipoBiometriaRepository extends JpaRepository<ParTipoBiometriaEntity, Long> {

}
