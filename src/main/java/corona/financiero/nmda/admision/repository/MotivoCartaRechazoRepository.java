package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.MotivoCartaRechazoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotivoCartaRechazoRepository extends JpaRepository<MotivoCartaRechazoEntity, Long> {

    Optional<MotivoCartaRechazoEntity> findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(long solicitudAdmisionId, String rutFormateado);

    Optional<MotivoCartaRechazoEntity> findBySolicitudAdmisionEntitySolicitudId(Long solicitudAdmisionId);
}
