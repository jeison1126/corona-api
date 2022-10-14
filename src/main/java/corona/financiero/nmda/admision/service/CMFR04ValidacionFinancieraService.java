package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ValidacionMorosidadDTO;
import corona.financiero.nmda.admision.repository.CMFR04Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CMFR04ValidacionFinancieraService {

    @Autowired
    private CMFR04Repository cmfr04Repository;


    private static final Long ZERO = 0L;


    public Boolean isClienteMoroso(String rut) {

        ValidacionMorosidadDTO deudasCliente = null;
        boolean retorno = false;
        long resultado = 0;

        try {
            deudasCliente = cmfr04Repository.findCmf_r04ByRut(rut);

            if (deudasCliente != null) {

                if (deudasCliente.getCreditosDirectosImpagos90a180dias() != null) {
                    log.debug("1 deudasCliente.getCreditosDirectosImpagos90a180dias(): {}",deudasCliente.getCreditosDirectosImpagos90a180dias());
                    resultado = resultado + deudasCliente.getCreditosDirectosImpagos90a180dias();
                }

                if (deudasCliente.getCreditosDirectosImpagos30a90Dias() != null) {
                    log.debug("2 deudasCliente.getCreditosDirectosImpagos30a90Dias(): {}",deudasCliente.getCreditosDirectosImpagos30a90Dias());
                    resultado = resultado + deudasCliente.getCreditosDirectosImpagos30a90Dias();
                }

                if (deudasCliente.getCreditosDirectosImpagos180a3anios() != null) {
                    log.debug("2 deudasCliente.getCreditosDirectosImpagos180a3anios(): {}",deudasCliente.getCreditosDirectosImpagos180a3anios());
                    resultado = resultado + deudasCliente.getCreditosDirectosImpagos180a3anios();
                }

                if (deudasCliente.getCreditosDirectosImpagosIgualMayor3anios() != null) {
                    log.debug("3 deudasCliente.getCreditosDirectosImpagosIgualMayor3anios(): {}",deudasCliente.getCreditosDirectosImpagosIgualMayor3anios());
                    resultado = resultado + deudasCliente.getCreditosDirectosImpagosIgualMayor3anios();
                }
            }

            retorno = resultado > ZERO;
            log.debug("Resultado a evaluar: {}",resultado);


        } catch (Exception e) {
            retorno = false;
            log.error(e.getMessage());
        }


        return retorno;
    }
}
