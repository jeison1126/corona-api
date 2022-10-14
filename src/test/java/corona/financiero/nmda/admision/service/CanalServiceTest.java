package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParCanalEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParCanalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CanalServiceTest {

    @Mock
    private ParCanalRepository parCanalRepository;

    @InjectMocks
    private CanalService canalService;

    @Test
    void listarfasesVigentesTest() {

        var parCanal = new ParCanalEntity();
        parCanal.setCanalId(1l);
        parCanal.setDescripcion("Tienda");
        parCanal.setVigencia(true);
        parCanal.setFechaRegistro(LocalDateTime.now());
        parCanal.setUsuarioRegistro("USR_TMP");

        when(parCanalRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(Arrays.asList(parCanal));

        canalService.listarCanales();

        verify(parCanalRepository, times(1)).findAllByVigenciaIsTrueOrderByDescripcion();

    }

    @Test
    void listarFasesVigentesNullNoContentTest() {
        when(parCanalRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> canalService.listarCanales())
                .withNoCause();

    }

    @Test
    void listarFasesVigentesEmptyNoContentTest() {
        List<ParCanalEntity> listaVacia = new ArrayList<>();
        when(parCanalRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(listaVacia);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> canalService.listarCanales())
                .withNoCause();

    }
}
