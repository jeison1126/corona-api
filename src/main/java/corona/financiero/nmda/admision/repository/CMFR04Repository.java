package corona.financiero.nmda.admision.repository;

import corona.financiero.nmda.admision.dto.ValidacionMorosidadDTO;
import corona.financiero.nmda.admision.entity.CMFR04Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CMFR04Repository extends JpaRepository<CMFR04Entity, String> {

    @Query("SELECT new corona.financiero.nmda.admision.dto.ValidacionMorosidadDTO(c.creditosDirectosImpagos30a90Dias, c.creditosDirectosImpagos180a3anios, c.creditosDirectosImpagosIgualMayor3anios, c.creditosDirectosImpagos90a180dias) from CMFR04Entity c WHERE rut = ?1")
    ValidacionMorosidadDTO findCmf_r04ByRut(String rut);

}
