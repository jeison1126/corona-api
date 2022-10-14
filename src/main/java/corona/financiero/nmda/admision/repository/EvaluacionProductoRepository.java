package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.EvaluacionProductoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionProductoRepository extends JpaRepository<EvaluacionProductoEntity, Long> {
	@Query("SELECT ep FROM EvaluacionProductoEntity ep where ep.vigencia is true and ep.solicitudAdmisionEntity.solicitudId = :solicitudId and "
			+ " ep.solicitudAdmisionEntity.vigencia is true and ep.solicitudAdmisionEntity.prospectoEntity.rut = :rut and ep.solicitudAdmisionEntity.prospectoEntity.vigencia is true "
			+ " and ep.parTipoProductoEntity.vigencia is true")
    List<EvaluacionProductoEntity> listarProductosOfercidosBySolicitudIdAndRut(long solicitudId, String rut);
	
	@Query("SELECT ep FROM EvaluacionProductoEntity ep where ep.vigencia is true and ep.evaluacionProductoId = :evaluacionProductoId and "
			+ " ep.solicitudAdmisionEntity.vigencia is true and ep.solicitudAdmisionEntity.prospectoEntity.rut = :rut and ep.solicitudAdmisionEntity.prospectoEntity.vigencia is true ")
	Optional<EvaluacionProductoEntity> recuperarEvaluacionProductoByEvaluacionProductoIdAndRut(long evaluacionProductoId, String rut);

	List<EvaluacionProductoEntity> findAllBySolicitudAdmisionEntityAndVigenciaIsTrue(SolicitudAdmisionEntity solicitudAdmisionEntity);
}
