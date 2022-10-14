package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParFaseRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FaseServiceTest {

    @Mock
    private ParFaseRepository parFaseRepository;

    @InjectMocks
    private FaseService faseService;

    @Test
    void listarfasesVigentesTest() {

        var parFase = new ParFaseEntity();
        parFase.setFaseId(1l);
        parFase.setDescripcion("Captura de datos cliente");
        parFase.setVigencia(true);
        parFase.setFechaRegistro(LocalDateTime.now());
        parFase.setUsuarioRegistro("USR_TMP");

        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId()).thenReturn(Arrays.asList(parFase));


        var parFaseHija = new ParFaseEntity();
        parFaseHija.setFaseId(5l);
        parFaseHija.setFasePadreId(1l);
        parFaseHija.setDescripcion("Sub fase");
        parFaseHija.setVigencia(true);
        parFaseHija.setFechaRegistro(LocalDateTime.now());
        parFaseHija.setUsuarioRegistro("USR_TMP");

        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdOrderByFaseId(parFase.getFaseId())).thenReturn(Arrays.asList(parFaseHija));

        faseService.listarFasesVigentes();

    }

    @Test
    void listarfasesVigentesSinHijasTest() {

        var parFase = new ParFaseEntity();
        parFase.setFaseId(2l);
        parFase.setDescripcion("Captura de datos cliente");
        parFase.setVigencia(true);
        parFase.setFechaRegistro(LocalDateTime.now());
        parFase.setUsuarioRegistro("USR_TMP");

        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId()).thenReturn(Arrays.asList(parFase));


        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdOrderByFaseId(parFase.getFaseId())).thenReturn(new ArrayList<>());

        faseService.listarFasesVigentes();

    }

    @Test
    void listarFasesVigentesNullNoContentTest() {
        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> faseService.listarFasesVigentes())
                .withNoCause();

    }

    @Test
    void listarFasesVigentesEmptyNoContentTest() {
        List<ParFaseEntity> listaVacia = new ArrayList<>();
        when(parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId()).thenReturn(listaVacia);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> faseService.listarFasesVigentes())
                .withNoCause();

    }
}
