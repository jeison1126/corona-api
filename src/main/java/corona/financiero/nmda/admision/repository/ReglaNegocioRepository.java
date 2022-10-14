package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ReglaNegocioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReglaNegocioRepository extends JpaRepository<ReglaNegocioEntity, Long> {

    Page<ReglaNegocioEntity> findByDescripcionIgnoreCaseContaining(String descripcion, Pageable pageable);

    @Query("select rn from ReglaNegocioEntity rn where lower(rn.descripcion) = lower(:descripcion)")
    ReglaNegocioEntity verificarPreExistentePorDescripcion(String descripcion);

    @Query("SELECT rn FROM ReglaNegocioEntity rn where rn.editable = true and (lower(rn.descripcion) LIKE %:valorFiltro% or lower(rn.valor) LIKE %:valorFiltro%)")
    Page<ReglaNegocioEntity> listarEditablesConFiltroPaginado(String valorFiltro, Pageable pageable);

    @Query("SELECT rn FROM ReglaNegocioEntity rn where rn.editable = true ")
    Page<ReglaNegocioEntity> listarEditablesPaginado(Pageable pageable);
}
