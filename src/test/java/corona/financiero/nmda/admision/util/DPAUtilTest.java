package corona.financiero.nmda.admision.util;

import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DPAUtilTest {

    @InjectMocks
    private DPAUtil dpaUtil;

    @Test
    void listarRegionesTest() throws IOException {

        List<DPAResponseDTO> dpaResponseDTOS = dpaUtil.regionesDesdeArchivo();
        assertThat(dpaResponseDTOS.get(0).getCodigo()).isEqualTo("15");

    }

    @Test
    void listarComunasDeRegionTest() throws IOException {
        List<DPAResponseDTO> dpaResponseDTOS = dpaUtil.comunasDesdeArchivo("01");
        assertThat(dpaResponseDTOS.get(0).getCodigo()).isEqualTo("01107");
    }
}
