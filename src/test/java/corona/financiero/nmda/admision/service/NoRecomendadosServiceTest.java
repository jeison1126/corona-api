package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.NoRecomendadosRequestDTO;
import corona.financiero.nmda.admision.entity.NoRecomendadosEntity;
import corona.financiero.nmda.admision.entity.SucursalesEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoRecomendadosNotFoundException;
import corona.financiero.nmda.admision.repository.NoRecomendadosRepository;
import corona.financiero.nmda.admision.repository.SucursalesRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoRecomendadosServiceTest {

    @Mock
    private NoRecomendadosRepository noRecomendadosRepository;

    @InjectMocks
    private NoRecomendadosService noRecomendadosService;

    @Mock
    private SucursalesRepository sucursalesRepository;

    @Mock
    private Validaciones validaciones;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(noRecomendadosService, "paginacion", 15);
    }

    @Test
    void listarNoRecomendadosFiltroNullTest() {
        int numPagina = 1;
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<NoRecomendadosEntity> page = Mockito.mock(Page.class);
        when(noRecomendadosRepository.findAll(pageable)).thenReturn(page);

        noRecomendadosService.listarNoRecomendados(numPagina, null);
    }

    @Test
    void listarNoRecomendadosFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<NoRecomendadosEntity> page = Mockito.mock(Page.class);
        when(noRecomendadosRepository.findAll(pageable)).thenReturn(page);

        noRecomendadosService.listarNoRecomendados(numPagina, filtro);
    }

    @Test
    void listarNoRecomendadosConFiltroTest() {
        int numPagina = 1;
        String filtro = "concepcion";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();
        noRecomendadosEntity.setSucursalOrigen(1l);
        List<NoRecomendadosEntity> lista = Arrays.asList(noRecomendadosEntity);
        Page<NoRecomendadosEntity> pagedResponse = new PageImpl(lista);
        when(noRecomendadosRepository.listarNoRecomendados(filtro, pageable)).thenReturn(pagedResponse);


        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setDescripcionSucursal("Santiago");
        sucursalesEntity.setCodigoSucursal(1);
        when(sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc()).thenReturn(Arrays.asList(sucursalesEntity));
        noRecomendadosService.listarNoRecomendados(numPagina, filtro);
    }

    @Test
    void listarNoRecomendadosSinSucursalTest() {
        int numPagina = 1;

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        List<NoRecomendadosEntity> lista = Arrays.asList(noRecomendadosEntity);
        Page<NoRecomendadosEntity> pagedResponse = new PageImpl(lista);
        when(noRecomendadosRepository.findAll(pageable)).thenReturn(pagedResponse);


        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        when(sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc()).thenReturn(Arrays.asList(sucursalesEntity));
        noRecomendadosService.listarNoRecomendados(numPagina, null);
    }

    @Test
    void listarNoRecomendadosBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.listarNoRecomendados(-1, null))
                .withNoCause();

    }

    @Test
    void obtenerNoRecomendadoTest() {

        String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);
        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));

        noRecomendadosService.obtenerNoRecomendable(rut);
    }

    @Test
    void obtenerNoRecomendadoSinSucursalTest() {

        String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);
        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();
        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(noRecomendadosEntity));

        SucursalesEntity sucursalesEntity = new SucursalesEntity();

        when(sucursalesRepository.findById(noRecomendadosEntity.getSucursalOrigen())).thenReturn(Optional.of(sucursalesEntity));

        noRecomendadosService.obtenerNoRecomendable(rut);
    }


    @Test
    void eliminarNoRecomendadosTest() {

        String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));

        noRecomendadosService.eliminarNoRecomendado(rut);

    }

    @Test
    void eliminarNoRecomendadosNullBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.eliminarNoRecomendado(null))
                .withNoCause();
    }

    @Test
    void eliminarNoRecomendadosEmptyBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.eliminarNoRecomendado(""))
                .withNoCause();
    }

    @Test
    void eliminarMensajeRechazNotFoundTest() {
        String rut = "11111111-1";
        when(validaciones.validaRut(rut)).thenReturn(true);
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);
        assertThatExceptionOfType(NoRecomendadosNotFoundException.class)
                .isThrownBy(() -> noRecomendadosService.eliminarNoRecomendado(rut))
                .withNoCause();
    }


    @Test
    void registrarNoRecomendadoTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);


        noRecomendadosService.registrarNoRecomendado(request);
    }

    @Test
    void registrarNoRecomendadoYaExistenteTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(noRecomendadosEntity));
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoNullErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(null))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoRutNullErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setRut(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoInvalidRutErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setRut("11111111-2");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoRutEmptyErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setRut("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }


    @Test
    void registrarNoRecomendadoNombreNullErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setNombre(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoNombreEmptyErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setNombre("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoApellidoPaternoNullErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setApellidoPaterno(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }

    @Test
    void registrarNoRecomendadoApellidoPaternoEmptyErrorTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setApellidoPaterno("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> noRecomendadosService.registrarNoRecomendado(request))
                .withNoCause();
    }


    @Test
    void actualizarNoRecomendadoTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));


        noRecomendadosService.actualizarNoRecomendado(request);
    }

    @Test
    void actualizarNoRecomendadoMotivoNullTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setMotivo(null);
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));


        noRecomendadosService.actualizarNoRecomendado(request);
    }

    @Test
    void actualizarNoRecomendadoSucursalOrigenNullTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setSucursalOrigen(null);
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));


        noRecomendadosService.actualizarNoRecomendado(request);
    }

    @Test
    void actualizarNoRecomendadoApellidoMaternoNullTest() {
        NoRecomendadosRequestDTO request = datosNoRecomendadosDTO();
        request.setApellidoMaterno(null);
        when(validaciones.validaRut(request.getRut())).thenReturn(true);

        NoRecomendadosEntity noRecomendadosEntity = datosNoRecomendadosEntity();

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        when(noRecomendadosRepository.findById(rutFormateado)).thenReturn(Optional.of(datosNoRecomendadosEntity()));


        noRecomendadosService.actualizarNoRecomendado(request);
    }

    private NoRecomendadosEntity datosNoRecomendadosEntity() {
        NoRecomendadosEntity noRecomendadosEntity = new NoRecomendadosEntity();
        noRecomendadosEntity.setMotivoNoRecomendado("No paga");
        noRecomendadosEntity.setApellidoMaterno("Soto");
        noRecomendadosEntity.setApellidoPaterno("Perez");
        noRecomendadosEntity.setNombre("Juan");
        noRecomendadosEntity.setRut("111111111");
        noRecomendadosEntity.setUsuarioRegistro("USR_TMP");

        return noRecomendadosEntity;
    }

    private NoRecomendadosRequestDTO datosNoRecomendadosDTO() {
        NoRecomendadosRequestDTO request = new NoRecomendadosRequestDTO();
        request.setMotivo("No Paga");

        request.setSucursalOrigen(1l);
        request.setRut("11111111-1");
        request.setNombre("Juan");
        request.setApellidoPaterno("Perez");
        request.setApellidoMaterno("Soto");
        return request;
    }


}
