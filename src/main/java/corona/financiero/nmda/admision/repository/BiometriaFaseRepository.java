package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.BiometriaFaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiometriaFaseRepository extends JpaRepository<BiometriaFaseEntity, Long> {

    @Query("select bf from BiometriaFaseEntity bf join bf.biometriaConsultaEntity bc join bc.solicitudAdmisionEntity s join bf.prospectoEntity p where s.solicitudId = :solicitudAdmisionId and " +
            "s.vigencia = true and lower(p.rut) = lower(:rut) and bf.vigencia = true and (bc.codigo = null or upper(bc.codigo) = upper('LCB-004-B')) and bc.excepcion = false")
//LCB-004-B = codigo cedula vencida, pendiente validacion
    Optional<BiometriaFaseEntity> verificaAprobacionValidacionBiometrica(String rut, Long solicitudAdmisionId);
}
