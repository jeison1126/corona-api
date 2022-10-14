package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, String> {

    @Query("select c from ClienteEntity c join c.prospectoEntity p where lower(c.rut) = lower(:rut) and lower(p.rut) = lower(:rut) and c.vigencia = true and p.vigencia = true")
    Optional<ClienteEntity> validarClienteProspecto(String rut);
}
