package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ReglaNegocioBasicoDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioCompletoDTO;
import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.entity.ParMensajeRechazoEntity;
import corona.financiero.nmda.admision.entity.ReglaNegocioEntity;
import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.ReglaNegocioNotFoundException;
import corona.financiero.nmda.admision.repository.ParFaseRepository;
import corona.financiero.nmda.admision.repository.ParMensajeRechazoRepository;
import corona.financiero.nmda.admision.repository.ReglaNegocioRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReglaNegocioServiceTest {

    @Mock
    private ReglaNegocioRepository reglaNegocioRepository;

    @InjectMocks
    private ReglaNegocioService reglaNegocioService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private ParMensajeRechazoRepository parMensajeRechazoRepository;

    @Mock
    private ParFaseRepository parFaseRepository;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(reglaNegocioService, "paginacion", 15);
    }

    @Test
    void listarPaginadoFiltroNullTest() {
        int numPagina = 1;
        String direccionOrdena = DireccionOrdenaEnum.ASC.name();
        String campoOrdena = "descripcion";

        when(validaciones.validarDireccionOrdenamiento(any())).thenReturn(true);

        Sort sort = Sort.by(campoOrdena).ascending();

        Pageable pageable = PageRequest.of(numPagina, 15, sort);

        Page<ReglaNegocioEntity> page = Mockito.mock(Page.class);
        when(reglaNegocioRepository.listarEditablesPaginado(pageable)).thenReturn(page);

        reglaNegocioService.listarPaginado(numPagina, campoOrdena, direccionOrdena, null);
    }

    @Test
    void listarPaginadoFiltroEmptyTest() {

        int numPagina = 1;
        String direccionOrdena = DireccionOrdenaEnum.ASC.name();
        String campoOrdena = "descripcion";
        String filtro = "";

        when(validaciones.validarDireccionOrdenamiento(any())).thenReturn(true);

        Sort sort = Sort.by(campoOrdena).ascending();

        Pageable pageable = PageRequest.of(numPagina, 15, sort);

        Page<ReglaNegocioEntity> page = Mockito.mock(Page.class);
        when(reglaNegocioRepository.listarEditablesPaginado(pageable)).thenReturn(page);

        reglaNegocioService.listarPaginado(numPagina, campoOrdena, direccionOrdena, filtro);
    }

    @Test
    void listarPaginadoConFiltroTest() {

        int numPagina = 1;
        String direccionOrdena = DireccionOrdenaEnum.ASC.name();
        String campoOrdena = "descripcion";
        String filtro = "act";
        when(validaciones.validarDireccionOrdenamiento(any())).thenReturn(true);

        Sort sort = Sort.by(campoOrdena).ascending();

        Pageable pageable = PageRequest.of(numPagina, 15, sort);

        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setValor("Valor");
        reglaNegocioEntity.setDescripcion("Descripcion");
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        List<ReglaNegocioEntity> lista = Arrays.asList(reglaNegocioEntity);
        Page<ReglaNegocioEntity> pagedResponse = new PageImpl(lista);
        when(reglaNegocioRepository.listarEditablesConFiltroPaginado(filtro, pageable)).thenReturn(pagedResponse);

        reglaNegocioService.listarPaginado(numPagina, campoOrdena, direccionOrdena, filtro);

    }

    @Test
    void listarPaginadoDescendente() {

        int numPagina = 1;
        String direccionOrdena = DireccionOrdenaEnum.DESC.name();
        String campoOrdena = "descripcion";
        String filtro = "act";
        when(validaciones.validarDireccionOrdenamiento(any())).thenReturn(true);

        Sort sort = Sort.by(campoOrdena).descending();

        Pageable pageable = PageRequest.of(numPagina, 15, sort);

        Page<ReglaNegocioEntity> page = Mockito.mock(Page.class);
        when(reglaNegocioRepository.listarEditablesConFiltroPaginado(filtro, pageable)).thenReturn(page);

        reglaNegocioService.listarPaginado(numPagina, campoOrdena, direccionOrdena, filtro);

    }

    @Test
    void listarPaginadoDireccionOrdenaNullTest() {
        int numPagina = 1;
        String direccionOrdena = null;
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.listarPaginado(numPagina, null, direccionOrdena, null))
                .withNoCause();
    }

    @Test
    void listarPaginadoDireccionOrdenaEmptyTest() {
        int numPagina = 1;
        String direccionOrdena = "";

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.listarPaginado(numPagina, null, direccionOrdena, null))
                .withNoCause();
    }

    @Test
    void listarPaginadoDireccionOrdenaOtroTest() {
        int numPagina = 1;
        String direccionOrdena = "AC";

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.listarPaginado(numPagina, null, direccionOrdena, null))
                .withNoCause();
    }

    @Test
    void listarPaginadoBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.listarPaginado(-1, DireccionOrdenaEnum.ASC.name(), "descripcion", null))
                .withNoCause();

    }

    @Test
    void eliminarPorIdTest() {
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setUsuarioModificacion("USR_TMP");
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setDescripcion("Descripcion");
        reglaNegocioEntity.setValor("Valor");
        reglaNegocioEntity.setVigencia(false);

        when(validaciones.validarId(any())).thenReturn(true);

        when(reglaNegocioRepository.findById(any())).thenReturn(Optional.of(new ReglaNegocioEntity()));

        reglaNegocioService.eliminarPorId("1");

    }

    @Test
    void eliminarPorIdBadRequestTest() {
        when(validaciones.validarId(any())).thenReturn(false);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.eliminarPorId("0"))
                .withNoCause();
    }

    @Test
    void eliminarPorIdNotFoundTest() {
        when(validaciones.validarId(any())).thenReturn(true);

        assertThatExceptionOfType(ReglaNegocioNotFoundException.class)
                .isThrownBy(() -> reglaNegocioService.eliminarPorId("1"))
                .withNoCause();
    }

    @Test
    void eliminarPorIdEmptyTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.eliminarPorId(""))
                .withNoCause();
    }

    @Test
    void eliminarPorIdNullTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.eliminarPorId(null))
                .withNoCause();
    }

    @Test
    void recuperarPorIdTest() {
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setUsuarioModificacion("USR_TMP");
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setDescripcion("Descripcion");
        reglaNegocioEntity.setValor("Valor");
        reglaNegocioEntity.setVigencia(false);

        when(validaciones.validarId(any())).thenReturn(true);

        when(reglaNegocioRepository.findById(any())).thenReturn(Optional.of(new ReglaNegocioEntity()));

        reglaNegocioService.recuperarPorId("1");

    }

    @Test
    void recuperarPorIdBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.recuperarPorId("0"))
                .withNoCause();
    }

    @Test
    void recuperarPorIdNotFoundTest() {
        when(validaciones.validarId(any())).thenReturn(true);
        assertThatExceptionOfType(ReglaNegocioNotFoundException.class)
                .isThrownBy(() -> reglaNegocioService.recuperarPorId("1"))
                .withNoCause();
    }

    @Test
    void recuperarPorIdEmptyTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.recuperarPorId(""))
                .withNoCause();
    }

    @Test
    void recuperarPorIdNullTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.recuperarPorId(null))
                .withNoCause();

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.recuperarPorId(null))
                .withNoCause();
    }

    @Test
    void actualizar() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(1l);
        request.setVigencia(true);
        request.setValor("Valor");
        request.setDescripcion("Descripcion");

        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setValor(request.getValor());
        reglaNegocioEntity.setDescripcion(request.getDescripcion());
        reglaNegocioEntity.setVigencia(request.isVigencia());
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setUsuarioModificacion("USR_TMP");
        when(reglaNegocioRepository.findById(any())).thenReturn(Optional.of(reglaNegocioEntity));

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setMensajeRechazoId(1l);
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));

        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1l);
        when(parFaseRepository.findById(any())).thenReturn(Optional.of(parFaseEntity));
        reglaNegocioService.actualizar(request);

    }

    @Test
    void actualizarBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(null))
                .withNoCause();
    }

    @Test
    void actualizarBadRequestInvalidIdTest() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(0l);
        request.setDescripcion("Descripcion");
        request.setValor("Valor");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(request))
                .withNoCause();
    }

    @Test
    void actualizarBadRequestInvalidIdNullTest() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(null);
        request.setDescripcion("Descripcion");
        request.setValor("Valor");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(request))
                .withNoCause();
    }

    @Test
    void actualizarBadRequestInvalidIdNegativoTest() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(-1L);
        request.setDescripcion("Descripcion");
        request.setValor("Valor");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(request))
                .withNoCause();
    }

    @Test
    void actualizarBadRequestDescripcionValorNullTest() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(1l);
        request.setDescripcion(null);
        request.setValor(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(request))
                .withNoCause();
    }

    @Test
    void actualizarBadRequestDescripcionValorEmptyTest() {
        ReglaNegocioCompletoDTO request = new ReglaNegocioCompletoDTO();
        request.setId(1l);
        request.setDescripcion("");
        request.setValor("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.actualizar(request))
                .withNoCause();
    }

    @Test
    void registrarTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setValor("Valor");
        request.setDescripcion("Descripcion");

        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setValor(request.getValor());
        reglaNegocioEntity.setDescripcion(request.getDescripcion());
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setUsuarioModificacion("USR_TMP");
        when(reglaNegocioRepository.verificarPreExistentePorDescripcion(any())).thenReturn(null);

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setMensajeRechazoId(1l);
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));

        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1l);
        when(parFaseRepository.findById(any())).thenReturn(Optional.of(parFaseEntity));


        reglaNegocioService.registrar(request);

    }

    @Test
    void registrarPreexistenteErrorTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setValor("Valor");
        request.setDescripcion("Descripcion");

        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setFechaIngreso(LocalDateTime.now());
        reglaNegocioEntity.setValor(request.getValor());
        reglaNegocioEntity.setDescripcion(request.getDescripcion());
        reglaNegocioEntity.setFechaModificacion(LocalDateTime.now());
        reglaNegocioEntity.setUsuarioIngreso("USR_TMP");
        reglaNegocioEntity.setUsuarioModificacion("USR_TMP");
        when(reglaNegocioRepository.verificarPreExistentePorDescripcion(any())).thenReturn(reglaNegocioEntity);


        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(request))
                .withNoCause();

    }

    @Test
    void registrarDescripcionNullErrorTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setValor("Valor");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(request))
                .withNoCause();

    }

    @Test
    void registrarDescripcionEmptyErrorTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setValor("Valor");
        request.setDescripcion("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(request))
                .withNoCause();

    }

    @Test
    void registrarValorNullErrorTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setDescripcion("Descripcion");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(request))
                .withNoCause();

    }

    @Test
    void registrarValorVacioErrorTest() {
        ReglaNegocioBasicoDTO request = new ReglaNegocioBasicoDTO();
        request.setValor("");
        request.setDescripcion("Descripcion");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(request))
                .withNoCause();

    }

    @Test
    void registrarBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> reglaNegocioService.registrar(null))
                .withNoCause();

    }
}
