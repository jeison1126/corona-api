package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ResumenMorosidadesProtestosImpagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumenMorosidadesProtestosImpagosRepository extends JpaRepository<ResumenMorosidadesProtestosImpagosEntity, Long> {
}
