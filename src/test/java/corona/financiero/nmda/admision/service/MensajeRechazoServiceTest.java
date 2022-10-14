package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.MensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.NuevoMensajeRechazoDTO;
import corona.financiero.nmda.admision.entity.ParMensajeRechazoEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.MensajeRechazoNotFoundException;
import corona.financiero.nmda.admision.repository.ParMensajeRechazoRepository;
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
class MensajeRechazoServiceTest {

    @Mock
    private ParMensajeRechazoRepository parMensajeRechazoRepository;

    @InjectMocks
    private MensajeRechazoService mensajeRechazoService;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(mensajeRechazoService, "paginacion", 15);
    }

    @Test
    void listarMensajesRechazosFiltroNullTest() {
        int numPagina = 1;
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<ParMensajeRechazoEntity> page = Mockito.mock(Page.class);
        when(parMensajeRechazoRepository.findAll(pageable)).thenReturn(page);

        mensajeRechazoService.listarMensajesRechazo(numPagina, null);
    }

    @Test
    void listarMensajesRechazosFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<ParMensajeRechazoEntity> page = Mockito.mock(Page.class);
        when(parMensajeRechazoRepository.findAll(pageable)).thenReturn(page);

        mensajeRechazoService.listarMensajesRechazo(numPagina, filtro);
    }

    @Test
    void listarMensajesRechazosConFiltroTest() {
        int numPagina = 1;
        String filtro = "corona";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setMensajeRechazoId(1l);
        parMensajeRechazoEntity.setMensajeFuncional("Descripcion Funcional");
        parMensajeRechazoEntity.setDescripcion("Descripcion interna");
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        List<ParMensajeRechazoEntity> lista = Arrays.asList(parMensajeRechazoEntity);
        Page<ParMensajeRechazoEntity> pagedResponse = new PageImpl(lista);
        when(parMensajeRechazoRepository.listarMensajesRechazoConFiltro(filtro, pageable)).thenReturn(pagedResponse);

        mensajeRechazoService.listarMensajesRechazo(numPagina, filtro);
    }

    @Test
    void listarMensajesBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.listarMensajesRechazo(-1, null))
                .withNoCause();
    }

    @Test
    void eliminarMensajeRechazoTest() {
        var parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setMensajeRechazoId(1l);
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setDescripcion("Descripcion");
        parMensajeRechazoEntity.setMensajeFuncional("Mensaje funcional");
        parMensajeRechazoEntity.setVigencia(false);
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));

        mensajeRechazoService.eliminarMensajeRechazo(1l);

    }

    @Test
    void eliminarMensajeRechazoBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.eliminarMensajeRechazo(0l))
                .withNoCause();
    }

    @Test
    void eliminarMensajeRechazNotFoundTest() {
        assertThatExceptionOfType(MensajeRechazoNotFoundException.class)
                .isThrownBy(() -> mensajeRechazoService.eliminarMensajeRechazo(1l))
                .withNoCause();
    }

    @Test
    void actualizarMensajeRechazoTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(1l);
        request.setVigencia(true);
        request.setMensajeFuncional("Mensaje Funcional");
        request.setDescripcion("Descripcion");

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());
        parMensajeRechazoEntity.setDescripcion(request.getDescripcion());
        parMensajeRechazoEntity.setVigencia(request.isVigencia());
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));
        mensajeRechazoService.actualizarMensajeRechazo(request);

    }

    @Test
    void actualizarMensajeRechazoMensajeFuncionalTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(1l);
        request.setVigencia(true);
        request.setMensajeFuncional("Mensaje Funcional");

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());
        parMensajeRechazoEntity.setVigencia(request.isVigencia());
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));
        mensajeRechazoService.actualizarMensajeRechazo(request);

    }

    @Test
    void actualizarMensajeRechazoDescripcionTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(1l);
        request.setVigencia(true);
        request.setDescripcion("Descripcion");

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setDescripcion(request.getDescripcion());
        parMensajeRechazoEntity.setVigencia(request.isVigencia());
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        when(parMensajeRechazoRepository.findById(any())).thenReturn(Optional.of(parMensajeRechazoEntity));
        mensajeRechazoService.actualizarMensajeRechazo(request);

    }

    @Test
    void actualizarMensajeRechazoBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.actualizarMensajeRechazo(null))
                .withNoCause();
    }

    @Test
    void actualizarMensajeRechazoBadRequestInvalidIdTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(0l);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.actualizarMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void actualizarMensajeRechazoBadRequestDescripcionMensajeFuncionalNullTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(1l);
        request.setDescripcion(null);
        request.setMensajeFuncional(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.actualizarMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void actualizarMensajeRechazoBadRequestDescripcionMensajeFuncionalEmptyTest() {
        MensajeRechazoDTO request = new MensajeRechazoDTO();
        request.setCausalRechazoId(1l);
        request.setDescripcion("");
        request.setMensajeFuncional("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.actualizarMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setMensajeFuncional("Mensaje Funcional");
        request.setDescripcion("Descripcion");

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());
        parMensajeRechazoEntity.setDescripcion(request.getDescripcion());
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        when(parMensajeRechazoRepository.verificaMensajePreExistente(any())).thenReturn(null);

        mensajeRechazoService.crearMensajeRechazo(request);
    }

    @Test
    void crearMensajeRechazoPreexistenteErrorTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setMensajeFuncional("Mensaje Funcional");
        request.setDescripcion("Descripcion");

        ParMensajeRechazoEntity parMensajeRechazoEntity = new ParMensajeRechazoEntity();
        parMensajeRechazoEntity.setFechaRegistro(LocalDateTime.now());
        parMensajeRechazoEntity.setMensajeFuncional(request.getMensajeFuncional());
        parMensajeRechazoEntity.setDescripcion(request.getDescripcion());
        parMensajeRechazoEntity.setFechaModificacion(LocalDateTime.now());
        parMensajeRechazoEntity.setUsuarioRegistro("USR_TMP");
        parMensajeRechazoEntity.setUsuarioModificacion("USR_TMP");
        when(parMensajeRechazoRepository.verificaMensajePreExistente(any())).thenReturn(parMensajeRechazoEntity);


        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoDescripcionNullErrorTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setMensajeFuncional("Mensaje Funcional");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoDescripcionVaciaErrorTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setMensajeFuncional("Mensaje Funcional");
        request.setDescripcion("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoMensajeNullErrorTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setDescripcion("Descripcion");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoMensajeVacioErrorTest() {
        NuevoMensajeRechazoDTO request = new NuevoMensajeRechazoDTO();
        request.setMensajeFuncional("");
        request.setDescripcion("Descripcion");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(request))
                .withNoCause();
    }

    @Test
    void crearMensajeRechazoBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> mensajeRechazoService.crearMensajeRechazo(null))
                .withNoCause();
    }
}
