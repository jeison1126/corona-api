package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParNacionalidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParNacionalidadRepository extends JpaRepository<ParNacionalidadEntity, Long> {
    List<ParNacionalidadEntity> findAllByVigenciaOrderByDescripcion(boolean vigencia);
}
