package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParCanalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParCanalRepository extends JpaRepository<ParCanalEntity, Long> {
    List<ParCanalEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
