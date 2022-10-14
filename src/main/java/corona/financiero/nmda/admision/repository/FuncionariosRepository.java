package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.FuncionariosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionariosRepository extends JpaRepository<FuncionariosEntity, String> {

    Optional<FuncionariosEntity> findByRut(String rut);

    Optional<FuncionariosEntity> findByRutAndVigenciaIsTrue(String rut);

    Optional<FuncionariosEntity> findByNombreUsuario(String usuarioRegistro);

    @Query("SELECT f FROM FuncionariosEntity f where f.vigencia is true")
    Page<FuncionariosEntity> listarFuncionariosVigentes(Pageable pageable);

    @Query("SELECT f FROM FuncionariosEntity f where f.vigencia is true and (lower(f.rut) LIKE %:filtro% or lower(f.nombreCompleto) LIKE %:filtro%  or lower(f.nombreUsuario) LIKE %:filtro% or lower(f.nombreSucursal) LIKE %:filtro% or lower(f.nombreCargo) LIKE %:filtro%)")
    Page<FuncionariosEntity> listarFuncionariosVigentesPorFiltro(String filtro, Pageable pageable);
}
