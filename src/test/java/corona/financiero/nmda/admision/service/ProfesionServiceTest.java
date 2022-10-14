package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParProfesionEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParProfesionRepository;
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
class ProfesionServiceTest {

    @Mock
    private ParProfesionRepository parProfesionRepository;

    @InjectMocks
    private ProfesionService profesionService;

    @Test
    void listarProfesionesTest() {

        ParProfesionEntity parProfesionEntity = profesionEntity();

        when(parProfesionRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(Arrays.asList(parProfesionEntity));

        profesionService.listarProfesion();
    }

    @Test
    void listarProfesionesEmptyTest() {

        List<ParProfesionEntity> lista = new ArrayList<>();

        when(parProfesionRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(lista);


        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> profesionService.listarProfesion())
                .withNoCause();
    }

    @Test
    void listarProfesionesNullTest() {

        when(parProfesionRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> profesionService.listarProfesion())
                .withNoCause();
    }


    private ParProfesionEntity profesionEntity() {
        ParProfesionEntity parProfesionEntity = new ParProfesionEntity();
        parProfesionEntity.setProfesionId(1l);
        parProfesionEntity.setDescripcion("Arquitecto");
        parProfesionEntity.setVigencia(true);

        return parProfesionEntity;

    }
}
