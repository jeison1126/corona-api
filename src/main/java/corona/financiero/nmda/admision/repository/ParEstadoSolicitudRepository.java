package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParEstadoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParEstadoSolicitudRepository extends JpaRepository<ParEstadoSolicitudEntity, Long> {
    List<ParEstadoSolicitudEntity> findAllByVigenciaIsTrueOrderByDescripcion();
}
