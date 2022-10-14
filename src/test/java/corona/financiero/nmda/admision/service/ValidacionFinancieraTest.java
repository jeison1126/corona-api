package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ValidacionMorosidadDTO;
import corona.financiero.nmda.admision.repository.CMFR04Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacionFinancieraTest {

    @InjectMocks
    CMFR04ValidacionFinancieraService CMFR04ValidacionFinancieraService;

    @Mock
    private CMFR04Repository cmfr04Repository;

    private ValidacionMorosidadDTO responseConDeudas = new ValidacionMorosidadDTO();
    private ValidacionMorosidadDTO responseSinDeudas = new ValidacionMorosidadDTO();
    private Long ZERO = 0L;
    private String RUT = "1234567";

    @BeforeEach
    private void init() {
        responseConDeudas.setCreditosDirectosImpagos30a90Dias(ZERO);
        responseConDeudas.setCreditosDirectosImpagos90a180dias(100L);
        responseConDeudas.setCreditosDirectosImpagos180a3anios(ZERO);
        responseConDeudas.setCreditosDirectosImpagosIgualMayor3anios(ZERO);

        responseSinDeudas.setCreditosDirectosImpagos30a90Dias(ZERO);
        responseSinDeudas.setCreditosDirectosImpagos90a180dias(ZERO);
        responseSinDeudas.setCreditosDirectosImpagos180a3anios(ZERO);
        responseSinDeudas.setCreditosDirectosImpagosIgualMayor3anios(ZERO);
    }

    @Test
    void whenGetDebts_thenReturnTrue() {

        var mockedRepository = cmfr04Repository;
        Mockito.when(mockedRepository.findCmf_r04ByRut(RUT)).thenReturn(responseConDeudas);
        mockedRepository.findCmf_r04ByRut(RUT);
        Assertions.assertTrue(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void whenGetDebts_thenReturnFalse() {
        var mockedRepository = cmfr04Repository;
        Mockito.when(mockedRepository.findCmf_r04ByRut(RUT)).thenReturn(responseSinDeudas);
        mockedRepository.findCmf_r04ByRut(RUT);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void whenGetDebts_thenReturnNull() {
        var mockedRepository = cmfr04Repository;
        Mockito.when(mockedRepository.findCmf_r04ByRut(RUT)).thenReturn(null);
        mockedRepository.findCmf_r04ByRut(null);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(null));
    }

}
