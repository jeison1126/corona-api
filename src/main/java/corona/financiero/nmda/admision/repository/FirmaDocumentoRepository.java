package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.entity.FirmaDocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FirmaDocumentoRepository extends JpaRepository<FirmaDocumentoEntity, Long> {

    List<FirmaDocumentoEntity> findAllByClienteEntityRutAndUsuarioEcertTmpId(String rut, int usuarioEcertId);

    Optional<FirmaDocumentoEntity> findByDocumentoEcertId(int doctoId);

    Optional<FirmaDocumentoEntity> findByClienteEntityRutAndDocumentoEcertIdAndDocumentoFirmadoIsNotNull(String rutFormateado, int documentoId);

    List<FirmaDocumentoEntity> findAllByClienteEntityRutAndUsuarioEcertTmpIdAndDocumentoFirmadoIsNull(String rut, int usuarioEcertId);

    Optional<FirmaDocumentoEntity> findByClienteEntityRutAndDocumentoEcertIdAndDocumentoFirmadoIsNull(String rutFormateado, int documentoId);
}
