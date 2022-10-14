package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParTipoProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParTipoProductoRepository extends JpaRepository<ParTipoProductoEntity, Long> {
    List<ParTipoProductoEntity> findAllByCortesiaIsTrueAndVigenciaIsTrue();

    @Query("select p from ParTipoProductoEntity p where p.vigencia = true and p.costoMantencion = (select max(pp.costoMantencion) from ParTipoProductoEntity pp where pp.vigencia = true)")
    ParTipoProductoEntity obtenerProductoRecomendado();

    @Query("select p from ParTipoProductoEntity p where p.cortesia = true and p.vigencia = true and p.costoMantencion = (select max(pp.costoMantencion) from ParTipoProductoEntity pp where pp.vigencia = true and pp.cortesia = true)")
    ParTipoProductoEntity obtenerProductoCortesiaRecomendado();

    List<ParTipoProductoEntity> findAllByVigenciaIsTrue();

    @Query("select p from ParTipoProductoEntity p where p.vigencia = true and lower(p.descripcion) = lower(:productoPredeterminado)")
    ParTipoProductoEntity buscarProductoPorDescripcion(String productoPredeterminado);
}
