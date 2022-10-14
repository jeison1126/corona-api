package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ParMensajeRechazoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParMensajeRechazoRepository extends JpaRepository<ParMensajeRechazoEntity, Long> {
    @Query("select p from ParMensajeRechazoEntity p where lower(descripcion) = lower(:descripcion)")
    ParMensajeRechazoEntity verificaMensajePreExistente(String descripcion);

    @Query("SELECT p FROM ParMensajeRechazoEntity p where lower(p.descripcion) LIKE %:filtro% or lower(p.mensajeFuncional) LIKE %:filtro%")
    Page<ParMensajeRechazoEntity> listarMensajesRechazoConFiltro(String filtro, Pageable pageable);
}
