package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.TarjetaActivacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaActivacionRepository extends JpaRepository<TarjetaActivacionEntity, Long> {

    @Query("select ta from TarjetaActivacionEntity ta join ta.tarjetaEntity t join t.clienteEntity c where ta.vigencia = true and t.vigencia = true and c.vigencia = true and lower(c.rut) = lower(:rutCliente) and c.vigencia = true")
    Optional<TarjetaActivacionEntity> validarClienteTarjetaActiva(String rutCliente);
}
