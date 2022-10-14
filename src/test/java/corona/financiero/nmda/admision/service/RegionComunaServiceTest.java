package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.RegionComunaAdapter;
import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionComunaServiceTest {

    @Mock
    private RegionComunaAdapter regionComunaAdapter;

    @InjectMocks
    private RegionComunaService regionComunaService;

    @Test
    void listarRegionesTest() {

        DPAResponseDTO regiones = regiones();

        when(regionComunaAdapter.regiones()).thenReturn(Arrays.asList(regiones));

        regionComunaService.listarRegiones();
    }

    @Test
    void listarRegionesEmptyTest() {

        List<DPAResponseDTO> regiones = new ArrayList<>();

        when(regionComunaAdapter.regiones()).thenReturn(regiones);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> regionComunaService.listarRegiones())
                .withNoCause();
    }

    @Test
    void listarRegionesNullTest() {
        when(regionComunaAdapter.regiones()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> regionComunaService.listarRegiones())
                .withNoCause();
    }

    @Test
    void listarComunasTest() {

        DPAResponseDTO comunas = comunas();

        String codigoRegion = "01";
        when(regionComunaAdapter.comunas(codigoRegion)).thenReturn(Arrays.asList(comunas));

        regionComunaService.listarComunas(codigoRegion);
    }

    @Test
    void listarComunasErrorCodigoRegionNullTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> regionComunaService.listarComunas(null))
                .withNoCause();
    }

    @Test
    void listarComunasErrorCodigoRegionEmptyTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> regionComunaService.listarComunas(""))
                .withNoCause();
    }

    @Test
    void listarComunasEmptyTest() {

        List<DPAResponseDTO> lista = new ArrayList<>();

        String codigoRegion = "01";
        when(regionComunaAdapter.comunas(codigoRegion)).thenReturn(lista);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> regionComunaService.listarComunas(codigoRegion))
                .withNoCause();
    }

    @Test
    void listarComunasNullTest() {

        String codigoRegion = "01";
        when(regionComunaAdapter.comunas(codigoRegion)).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> regionComunaService.listarComunas(codigoRegion))
                .withNoCause();
    }

    private DPAResponseDTO regiones() {
        DPAResponseDTO dpaResponseDTO = new DPAResponseDTO();
        dpaResponseDTO.setCodigo("01");
        dpaResponseDTO.setNombre("Arica y Parinacota");

        return dpaResponseDTO;
    }

    private DPAResponseDTO comunas() {
        DPAResponseDTO dpaResponseDTO = new DPAResponseDTO();
        dpaResponseDTO.setCodigo("01001");
        dpaResponseDTO.setNombre("Comuna 1");

        return dpaResponseDTO;
    }
}
