package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuloRepository extends JpaRepository<ModuloEntity, Long> {
    Optional<ModuloEntity> findByModuloId(Long modulo);
}
