package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.PerfilRolEntity;
import corona.financiero.nmda.admision.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRolRepository extends JpaRepository<PerfilRolEntity, Long> {
    List<PerfilRolEntity> findAllByPerfilEntityPerfilId(Long perfilId);

    List<PerfilRolEntity> findAllByRolEntity(RolEntity rolEntity);
}
