package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParActividadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParActividadRepository extends JpaRepository<ParActividadEntity, Long> {
    List<ParActividadEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
