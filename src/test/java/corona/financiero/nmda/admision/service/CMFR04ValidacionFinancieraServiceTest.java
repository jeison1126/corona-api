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
class CMFR04ValidacionFinancieraServiceTest {

    @InjectMocks
    private CMFR04ValidacionFinancieraService CMFR04ValidacionFinancieraService;

    @Mock
    private CMFR04Repository cmfr04Repository;

    private ValidacionMorosidadDTO responseSinDeudas = new ValidacionMorosidadDTO();
    private Long ZERO = 0L;
    private String RUT = "1234567";

    @BeforeEach
    private void init() {
        responseSinDeudas.setCreditosDirectosImpagos30a90Dias(ZERO);
        responseSinDeudas.setCreditosDirectosImpagos90a180dias(ZERO);
        responseSinDeudas.setCreditosDirectosImpagos180a3anios(ZERO);
        responseSinDeudas.setCreditosDirectosImpagosIgualMayor3anios(ZERO);
    }

    @Test
    void whenIsClienteMoroso_thenReturnFalse() {
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(responseSinDeudas);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void whenGetDebts_thenReturnNull() {
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(null);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(null));
    }

    @Test
    void whenIsClienteMoroso_30_90_thenReturnTrue() {
        var responseConDeudas = responseSinDeudas;
        responseConDeudas.setCreditosDirectosImpagos30a90Dias(100L);
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(responseConDeudas);
        Assertions.assertTrue(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));

    }

    @Test
    void whenIsClienteMoroso_90_180_thenReturnTrue() {
        var responseConDeudas = responseSinDeudas;
        responseConDeudas.setCreditosDirectosImpagos90a180dias(100L);
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(responseConDeudas);
        Assertions.assertTrue(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void whenIsClienteMoroso_180_3_thenReturnTrue() {
        var responseConDeudas = responseSinDeudas;
        responseConDeudas.setCreditosDirectosImpagos180a3anios(100L);
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(responseConDeudas);
        Assertions.assertTrue(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void whenIsClienteMoroso_mayor_3_thenReturnTrue() {
        var responseConDeudas = responseSinDeudas;
        responseConDeudas.setCreditosDirectosImpagosIgualMayor3anios(100L);
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(responseConDeudas);
        Assertions.assertTrue(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void atributosNulosTest() {
        ValidacionMorosidadDTO respuesta = new ValidacionMorosidadDTO();
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(respuesta);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }

    @Test
    void respuestaBDNulaTest() {
        Mockito.when(cmfr04Repository.findCmf_r04ByRut(RUT)).thenReturn(null);
        Assertions.assertFalse(CMFR04ValidacionFinancieraService.isClienteMoroso(RUT));
    }
}
