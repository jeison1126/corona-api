package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.RolEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long> {
    @Query("SELECT r FROM RolEntity r where lower(r.nombre) LIKE %:filtro%")
    Page<RolEntity> listarRoles(String filtro, Pageable pageable);

    @Query("SELECT r FROM RolEntity r where lower(r.nombre) = lower(:nombre)")
    RolEntity verificaRolPreexistente(String nombre);

    @Query("select count(r.rolId) from RolEntity r where lower(r.nombre) = lower(:nombreRol) and r.rolId <> :rolId")
    Long validarRolMismoNombreDistintoId(String nombreRol, Long rolId);

    Page<RolEntity> findAllByVigenciaIsTrue(Pageable pageable);

    @Query("SELECT r FROM RolEntity r where lower(r.nombre) LIKE %:filtro% and vigencia = true")
    Page<RolEntity> listarRolesVigentes(String filtro, Pageable pageable);
}
