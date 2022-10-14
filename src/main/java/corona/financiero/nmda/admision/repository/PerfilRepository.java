package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.PerfilEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<PerfilEntity, Long> {
    @Query("SELECT p FROM PerfilEntity p where lower(p.nombre) LIKE %:filtro%")
    Page<PerfilEntity> listarPerfiles(String filtro, Pageable pageable);

    @Query("select (count(p.perfilId) > 0) from PerfilEntity p where UPPER(p.nombre) = UPPER(:nombre)")
    Boolean existePerfilConMismoNombre(String nombre);

    @Query("select (count(p.perfilId) > 0) from PerfilEntity p where UPPER(p.nombre) = UPPER(:nombre) and p.perfilId <> :perfilId")
    Boolean existePerfilMismoNombreDistintoPerfil(String nombre, Long perfilId);
	
	@Query("SELECT p FROM PerfilEntity p where p.vigencia is true")
    Page<PerfilEntity> listarPerfilesVigentes(Pageable pageable);
	
	@Query("SELECT p FROM PerfilEntity p where lower(p.nombre) LIKE %:filtro% and p.vigencia is true")
    Page<PerfilEntity> listarPerfilesVigentesPorFiltro(String filtro, Pageable pageable);
	
}
