package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParProfesionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParProfesionRepository extends JpaRepository<ParProfesionEntity, Long> {
    List<ParProfesionEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
