package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParEstadoSolicitudEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParEstadoSolicitudRepository;
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
class EstadoSolicitudServiceTest {

    @Mock
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    @InjectMocks
    private EstadoSolicitudService estadoSolicitudService;

    @Test
    void listarEstadosVigentesTest() {

        var parEstado = new ParEstadoSolicitudEntity();
        parEstado.setEstadoSolicitudId(1l);
        parEstado.setDescripcion("Iniciado");
        parEstado.setVigencia(true);
        parEstado.setFechaRegistro(LocalDateTime.now());
        parEstado.setUsuarioRegistro("USR_TMP");

        when(parEstadoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(Arrays.asList(parEstado));

        estadoSolicitudService.listarEstados();

        verify(parEstadoSolicitudRepository, times(1)).findAllByVigenciaIsTrueOrderByDescripcion();

    }

    @Test
    void listarEstadosVigentesNullNoContentTest() {
        when(parEstadoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class).isThrownBy(() -> estadoSolicitudService.listarEstados()).withNoCause();

    }

    @Test
    void listarEstadosVigentesEmptyNoContentTest() {
        List<ParEstadoSolicitudEntity> listaVacia = new ArrayList<>();
        when(parEstadoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(listaVacia);

        assertThatExceptionOfType(NoContentException.class).isThrownBy(() -> estadoSolicitudService.listarEstados()).withNoCause();

    }
}
