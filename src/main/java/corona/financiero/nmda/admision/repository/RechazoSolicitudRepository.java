package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.RechazoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechazoSolicitudRepository extends JpaRepository<RechazoSolicitudEntity, Long> {
}
