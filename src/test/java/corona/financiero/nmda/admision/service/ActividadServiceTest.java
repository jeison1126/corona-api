package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParActividadEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParActividadRepository;
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
class ActividadServiceTest {

    @Mock
    private ParActividadRepository parActividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    @Test
    void listarActividadTest() {
        ParActividadEntity parActividadEntity = actividadEntity();

        when(parActividadRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(Arrays.asList(parActividadEntity));

        actividadService.listarActividades();

    }

    @Test
    void listarActividadNoContentTest() {
        List<ParActividadEntity> listaVacia = new ArrayList<>();

        when(parActividadRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(listaVacia);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> actividadService.listarActividades())
                .withNoCause();
    }

    @Test
    void listarActividadNullTest() {
        
        when(parActividadRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> actividadService.listarActividades())
                .withNoCause();
    }

    private ParActividadEntity actividadEntity() {
        ParActividadEntity parActividadEntity = new ParActividadEntity();
        parActividadEntity.setActividadId(1l);
        parActividadEntity.setDescripcion("Dependiente");
        parActividadEntity.setVigencia(true);

        return parActividadEntity;
    }
}
