package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.ParTipoSolicitudEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParTipoSolicitudRepository;
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
class EmisionTarjetaServiceTest {

    @Mock
    private ParTipoSolicitudRepository parTipoSolicitudRepository;

    @InjectMocks
    private EmisionTarjetaService emisionTarjetaService;

    @Test
    void listarMotivosOkTest() {

        var response = new ParTipoSolicitudEntity();
        response.setTipoSolicitudId(1l);
        response.setDescripcion(("Cliente Nuevo"));

        var response2 = new ParTipoSolicitudEntity();
        response2.setTipoSolicitudId(2l);
        response2.setDescripcion("Extravio");

        List<ParTipoSolicitudEntity> emisionMotivoResponseDTOS = Arrays.asList(response, response2);

        when(parTipoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(emisionMotivoResponseDTOS);

        emisionTarjetaService.listarMotivos();

    }

    @Test
    void listarMotivosNoContentNullErrorTest() {


        when(parTipoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> emisionTarjetaService.listarMotivos())
                .withNoCause();

    }

    @Test
    void listarMotivosNoContentEmptyErrorTest() {

        List<ParTipoSolicitudEntity> list = new ArrayList<>();

        when(parTipoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion()).thenReturn(list);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> emisionTarjetaService.listarMotivos())
                .withNoCause();

    }
}
