package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParEstadoCivilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParEstadoCivilRepository extends JpaRepository<ParEstadoCivilEntity, Long> {
    List<ParEstadoCivilEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
