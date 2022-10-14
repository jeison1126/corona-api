package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.CargoAutorizadorExcepcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoAutorizadorExcepcionRepository extends JpaRepository<CargoAutorizadorExcepcionEntity, Long> {
    @Query("select c from CargoAutorizadorExcepcionEntity c where c.vigencia = true and :cargoId IN c.cargoId")
    Optional<CargoAutorizadorExcepcionEntity> validarCargoAutorizadorExcepcion(long cargoId);
}
