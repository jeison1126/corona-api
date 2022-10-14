package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.NoRecomendadosEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoRecomendadosRepository extends JpaRepository<NoRecomendadosEntity, String> {
    @Query("SELECT n FROM NoRecomendadosEntity n where lower(n.rut) LIKE %:filtro% or lower(n.nombre) LIKE %:filtro% or lower(n.apellidoPaterno) LIKE %:filtro% or lower(n.apellidoMaterno) LIKE %:filtro%")
    Page<NoRecomendadosEntity> listarNoRecomendados(String filtro, Pageable pageable);
}
