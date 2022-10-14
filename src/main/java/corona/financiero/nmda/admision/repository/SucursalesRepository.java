package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.SucursalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalesRepository extends JpaRepository<SucursalesEntity, Long> {
    List<SucursalesEntity> findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc();

    @Query("select distinct s.descripcionZonaGeografica from SucursalesEntity s order by s.descripcionZonaGeografica ASC")
    List<String> listarZonasGeograficasSucursal();

    List<SucursalesEntity> findAllByOrderByDescripcionSucursalAsc();

    List<SucursalesEntity> findAllByVigenciaIsTrueAndDescripcionZonaGeograficaOrderByDescripcionSucursalAsc(String zona);
}
