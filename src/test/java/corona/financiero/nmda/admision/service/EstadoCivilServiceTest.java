package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParEstadoCivilEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParEstadoCivilRepository;
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
class EstadoCivilServiceTest {

    @Mock
    private ParEstadoCivilRepository parEstadoCivilRepository;

    @InjectMocks
    private EstadoCivilService estadoCivilService;

    @Test
    void listarEstadoCivilTest() {
        ParEstadoCivilEntity parEstadoCivilEntity = estadoCivilEntity();

        when(parEstadoCivilRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(Arrays.asList(parEstadoCivilEntity));

        estadoCivilService.listarEstadoCivil();
    }

    @Test
    void listarEstadoCivilEmptyTest() {
        List<ParEstadoCivilEntity> lista = new ArrayList<>();

        when(parEstadoCivilRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(lista);


        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> estadoCivilService.listarEstadoCivil())
                .withNoCause();
    }

    @Test
    void listarEstadoCivilNullTest() {

        when(parEstadoCivilRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> estadoCivilService.listarEstadoCivil())
                .withNoCause();
    }

    private ParEstadoCivilEntity estadoCivilEntity() {
        ParEstadoCivilEntity parEstadoCivilEntity = new ParEstadoCivilEntity();
        parEstadoCivilEntity.setEstadoCivilId(1);
        parEstadoCivilEntity.setDescripcion("Soltero");
        parEstadoCivilEntity.setVigencia(true);

        return parEstadoCivilEntity;
    }
}
