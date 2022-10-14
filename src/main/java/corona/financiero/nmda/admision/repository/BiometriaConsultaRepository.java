package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.BiometriaConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiometriaConsultaRepository extends JpaRepository<BiometriaConsultaEntity, Long> {
    @Query("select bc from BiometriaConsultaEntity bc join bc.solicitudAdmisionEntity s where s.vigencia = true and s.solicitudId = :solicitudAdmisionId and bc.excepcion = true")
    Optional<BiometriaConsultaEntity> verificaAprobacionValidacionExcepcion(Long solicitudAdmisionId);
}
