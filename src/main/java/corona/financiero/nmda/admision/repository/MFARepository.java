package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.MFAEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MFARepository extends JpaRepository<MFAEntity, Long> {
    Optional<MFAEntity> findBySolicitudAdmisionEntityAndValidadoIsTrueAndVigenciaIsTrue(SolicitudAdmisionEntity solicitudAdmisionEntity);

    List<MFAEntity> findAllBySolicitudAdmisionEntityProspectoEntityRut(String rutFormateado);

    @Query("select m from MFAEntity m join m.solicitudAdmisionEntity s join s.prospectoEntity p where lower(p.rut) = (:rut) and m.vigencia = true and s.vigencia = true and m.codigo = :codigo")
    Optional<MFAEntity> validarCodigoSMS(String rut, String codigo);

    @Query("select m from MFAEntity m join m.solicitudAdmisionEntity s join s.prospectoEntity p where lower(p.rut) = (:rut) and m.vigencia = true and m.validado = true")
    Optional<MFAEntity> verificaValidacionMFA(String rut);
}
