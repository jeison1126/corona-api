package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.ProspectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectoRepository extends JpaRepository<ProspectoEntity, String> {

    ProspectoEntity findByRutAndVigenciaIsTrue(String rut);

    ProspectoEntity findByRut(String rutFormateado);
}
