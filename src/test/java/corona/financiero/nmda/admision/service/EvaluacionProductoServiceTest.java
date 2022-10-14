package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.entity.EvaluacionProductoEntity;
import corona.financiero.nmda.admision.entity.ParEstadoSolicitudEntity;
import corona.financiero.nmda.admision.entity.ParTipoProductoEntity;
import corona.financiero.nmda.admision.entity.SolicitudAdmisionEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.EvaluacionProductoRepository;
import corona.financiero.nmda.admision.repository.ParTipoProductoRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluacionProductoServiceTest {
    @Mock
    private EvaluacionProductoRepository evaluacionProductoRepository;

    @InjectMocks
    private EvaluacionProductoService evaluacionProductoService;

    @Mock
    private ParTipoProductoRepository parTipoProductoRepository;

    @Mock
    private Validaciones validaciones;

    @BeforeEach
    public void initEach() {
    }


    @Test
    void listarProductosOfercidosBySolicitudIdAndRut() {
        String rut = "11111111-1";
        Long solicitudId = 1l;
        doNothing().when(validaciones).validacionGeneralRut(rut);

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        List<EvaluacionProductoEntity> lista = new ArrayList<>();
        lista.add(datosEvaluacionProductoEntity());

        when(evaluacionProductoRepository.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rutFormateado)).thenReturn(lista);

        evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rut);

    }

    @Test
    void listarProductosOfercidosBySolicitudIdAndRutCuandoNoExistenProductosPorOfrecer() {
        String rut = "11111111-1";
        Long solicitudId = 1l;
        doNothing().when(validaciones).validacionGeneralRut(rut);

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        List<EvaluacionProductoEntity> lista = new ArrayList<>();

        when(evaluacionProductoRepository.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rutFormateado)).thenReturn(lista);

        evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rut);

    }

    @Test
    void listarProductosOfercidosBySolicitudIdAndRutCuandoSolicitudIdEsNull() {
        String rut = "11111111-1";
        Long solicitudId = null;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rut))
                .withNoCause();

    }

    @Test
    void listarProductosOfercidosBySolicitudIdAndRutCuandoSolicitudIdEsInvalido() {
        String rut = "11111111-1";
        Long solicitudId = -1l;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rut))
                .withNoCause();

    }

    @Test
    void productosCortesiaTest() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        EvaluacionProductoEntity evaluacionProductoEntity = datosEvaluacionProductoEntity();
        List<EvaluacionProductoEntity> evaluacionProductoEntities = Arrays.asList(evaluacionProductoEntity);
        when(evaluacionProductoRepository.findAllBySolicitudAdmisionEntityAndVigenciaIsTrue(solicitudAdmisionEntity)).thenReturn(evaluacionProductoEntities);

        ParTipoProductoEntity parTipoProductoEntity = datosParTipoProductoEntity();
        ParTipoProductoEntity parTipoProductoEntity2 = datosParTipoProductoEntity();
        parTipoProductoEntity2.setTipoProductoId(2l);
        List<ParTipoProductoEntity> parTipoProductoEntities = Arrays.asList(parTipoProductoEntity, parTipoProductoEntity2);
        when(parTipoProductoRepository.findAllByCortesiaIsTrueAndVigenciaIsTrue()).thenReturn(parTipoProductoEntities);

        ParTipoProductoEntity parTipoProductoEntityRecomendado = datosParTipoProductoEntity();

        when(parTipoProductoRepository.obtenerProductoCortesiaRecomendado()).thenReturn(parTipoProductoEntityRecomendado);

        evaluacionProductoService.cargaProductoCortesia(solicitudAdmisionEntity);
    }

    @Test
    void productosPreEvaluadosTest() {
        long cupoAprobado = 350000l;
        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        EvaluacionProductoEntity evaluacionProductoEntity = datosEvaluacionProductoEntity();
        List<EvaluacionProductoEntity> evaluacionProductoEntities = Arrays.asList(evaluacionProductoEntity);
        when(evaluacionProductoRepository.findAllBySolicitudAdmisionEntityAndVigenciaIsTrue(solicitudAdmisionEntity)).thenReturn(evaluacionProductoEntities);

        ParTipoProductoEntity parTipoProductoEntity = datosParTipoProductoEntity();
        ParTipoProductoEntity parTipoProductoEntity2 = datosParTipoProductoEntity();
        parTipoProductoEntity2.setTipoProductoId(2l);
        List<ParTipoProductoEntity> parTipoProductoEntities = Arrays.asList(parTipoProductoEntity, parTipoProductoEntity2);
        when(parTipoProductoRepository.findAllByVigenciaIsTrue()).thenReturn(parTipoProductoEntities);

        ParTipoProductoEntity parTipoProductoEntityRecomendado = datosParTipoProductoEntity();
        when(parTipoProductoRepository.obtenerProductoRecomendado()).thenReturn(parTipoProductoEntityRecomendado);

        evaluacionProductoService.cargaProductosPreEvaluados(solicitudAdmisionEntity, cupoAprobado, null);
    }

    private EvaluacionProductoEntity datosEvaluacionProductoEntity() {
        EvaluacionProductoEntity evaluacionProductoEntity = new EvaluacionProductoEntity();
        evaluacionProductoEntity.setCupoAprobado(100000);
        evaluacionProductoEntity.setEvaluacionProductoId(1);
        evaluacionProductoEntity.setRecomendado(true);
        evaluacionProductoEntity.setSolicitudAdmisionEntity(datosSolicitudAdmisionEntity());
        evaluacionProductoEntity.setParTipoProductoEntity(datosParTipoProductoEntity());
        evaluacionProductoEntity.setVigencia(true);


        return evaluacionProductoEntity;
    }

    private SolicitudAdmisionEntity datosSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(datosEstadosEntity());

        return solicitudAdmisionEntity;
    }

    private ParEstadoSolicitudEntity datosEstadosEntity() {
        ParEstadoSolicitudEntity parEstadoSolicitudEntity = new ParEstadoSolicitudEntity();
        parEstadoSolicitudEntity.setEstadoSolicitudId(1);
        parEstadoSolicitudEntity.setDescripcion("Iniciado");

        return parEstadoSolicitudEntity;
    }

    private ParTipoProductoEntity datosParTipoProductoEntity() {
        ParTipoProductoEntity parTipoProductoEntity = new ParTipoProductoEntity();
        parTipoProductoEntity.setDescripcion("Tarjeta Corona");
        parTipoProductoEntity.setTipoProductoId(1);
        parTipoProductoEntity.setRangoMin(0);
        parTipoProductoEntity.setRangoMax(10);
        parTipoProductoEntity.setCortesia(true);
        parTipoProductoEntity.setCupoCortesia(80000l);

        return parTipoProductoEntity;
    }
}
