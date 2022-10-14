package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParNacionalidadEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParNacionalidadRepository;
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
class NacionalidadServiceTest {

    @Mock
    private ParNacionalidadRepository parNacionalidadRepository;

    @InjectMocks
    private NacionalidadService nacionalidadService;

    @Test
    void listarNacionalidaesVigentesTest() {

        var response = new ParNacionalidadEntity();
        response.setDescripcion("Chilena");
        response.setNacionalidadId(1l);
        response.setVigencia(true);
        response.setFechaRegistro(LocalDateTime.now());
        response.setUsuarioRegistro("USR_TMP");

        when(parNacionalidadRepository.findAllByVigenciaOrderByDescripcion(true)).thenReturn(Arrays.asList(response));

        nacionalidadService.listarNacionalidades();

        verify(parNacionalidadRepository, times(1)).findAllByVigenciaOrderByDescripcion(true);

    }


    @Test
    void listarFasesVigentesEmptyNoContentTest() {
        List<ParNacionalidadEntity> listaVacia = new ArrayList<>();
        when(parNacionalidadRepository.findAllByVigenciaOrderByDescripcion(true)).thenReturn(listaVacia);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> nacionalidadService.listarNacionalidades())
                .withNoCause();

    }

}
