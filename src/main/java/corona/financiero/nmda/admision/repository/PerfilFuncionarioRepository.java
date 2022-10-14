package corona.financiero.nmda.admision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import corona.financiero.nmda.admision.entity.PerfilFuncionarioEntity;

@Repository
public interface PerfilFuncionarioRepository extends JpaRepository<PerfilFuncionarioEntity, Long> {
    List<PerfilFuncionarioEntity> findAllByPerfilEntityPerfilId(Long perfilId);
	
	List<PerfilFuncionarioEntity> findAllByFuncionariosEntityRut(String rut);
	
}
