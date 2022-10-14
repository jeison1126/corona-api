package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ClienteEntity;
import corona.financiero.nmda.admision.entity.EvaluacionProductoEntity;
import corona.financiero.nmda.admision.entity.TarjetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<TarjetaEntity, Long> {
    List<TarjetaEntity> findByClienteEntityAndEvaluacionProductoEntity(ClienteEntity clienteEntity, EvaluacionProductoEntity evaluacionProductoEntity);

    Optional<TarjetaEntity> findByClienteEntityAndVigenciaIsTrue(ClienteEntity clienteEntity);
}
