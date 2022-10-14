package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParTipoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParTipoSolicitudRepository extends JpaRepository<ParTipoSolicitudEntity, Long> {

    List<ParTipoSolicitudEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
