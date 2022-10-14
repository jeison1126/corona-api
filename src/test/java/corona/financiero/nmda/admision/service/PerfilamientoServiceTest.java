package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilamientoServiceTest {
    @Mock
    private AccionRepository accionRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private ModuloRepository moduloRepository;

    @Value("${cantidad.elementos.paginacion}")
    private int paginacion;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private RolAccionRepository rolAccionRepository;

    @Mock
    private PerfilRolRepository perfilRolRepository;

    @Mock
    private PerfilFuncionarioRepository perfilFuncionarioRepository;

    @Mock
    private FuncionariosRepository funcionariosRepository;

    @InjectMocks
    private PerfilamientoService perfilamientoService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private FuncionariosService funcionariosService;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(perfilamientoService, "paginacion", 15);
    }

    @Test
    void listarAcciones() {

        AccionEntity accionEntity = getAccionEntity();

        List<AccionEntity> accionEntities = Arrays.asList(accionEntity);

        when(accionRepository.findAll()).thenReturn(accionEntities);

        perfilamientoService.listarAcciones();
    }

    @Test
    void listarAccionesNoContentNull() {

        when(accionRepository.findAll()).thenReturn(null);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.listarAcciones())
                .withNoCause();
    }

    @Test
    void listarAccionesNoContentEmpty() {

        List<AccionEntity> acciones = new ArrayList<>();

        when(accionRepository.findAll()).thenReturn(acciones);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.listarAcciones())
                .withNoCause();
    }

    @Test
    void listarModulos() {

        ModuloEntity modulo = getModuloEntity();

        List<ModuloEntity> moduloEntities = Arrays.asList(modulo);


        when(moduloRepository.findAll()).thenReturn(moduloEntities);

        perfilamientoService.listarModulos();
    }

    @Test
    void listarModulosNoContentNull() {

        when(moduloRepository.findAll()).thenReturn(null);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.listarModulos())
                .withNoCause();
    }

    @Test
    void listarModulosNoContentEmpty() {

        List<ModuloEntity> modulos = new ArrayList<>();

        when(moduloRepository.findAll()).thenReturn(modulos);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.listarModulos())
                .withNoCause();
    }

    @Test
    void listarRoles() {
        int numPagina = 0;
        String filtro = null;

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<RolEntity> page = Mockito.mock(Page.class);
        when(rolRepository.findAll(pageable)).thenReturn(page);


        perfilamientoService.listarRoles(numPagina, filtro);
    }

    @Test
    void listarRolesVigentes() {
        int numPagina = 0;
        String filtro = null;

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<RolEntity> page = Mockito.mock(Page.class);
        when(rolRepository.findAllByVigenciaIsTrue(pageable)).thenReturn(page);


        perfilamientoService.listarRolesVigentes(numPagina, filtro);
    }

    @Test
    void listaRolesFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<RolEntity> page = Mockito.mock(Page.class);
        when(rolRepository.findAll(pageable)).thenReturn(page);

        perfilamientoService.listarRoles(numPagina, filtro);
    }

    @Test
    void listaRolesVigentesFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<RolEntity> page = Mockito.mock(Page.class);
        when(rolRepository.findAllByVigenciaIsTrue(pageable)).thenReturn(page);

        perfilamientoService.listarRolesVigentes(numPagina, filtro);
    }

    @Test
    void listarRolesConFiltro() {
        int numPagina = 0;
        String filtro = "rol";

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        RolEntity rolEntity = getRolEntity();
        List<RolEntity> lista = Arrays.asList(rolEntity);
        Page<RolEntity> pagedResponse = new PageImpl(lista);
        when(rolRepository.listarRoles(filtro, pageable)).thenReturn(pagedResponse);

        perfilamientoService.listarRoles(numPagina, filtro);
    }

    @Test
    void listarRolesVigentesConFiltro() {
        int numPagina = 0;
        String filtro = "rol";

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        RolEntity rolEntity = getRolEntity();
        List<RolEntity> lista = Arrays.asList(rolEntity);
        Page<RolEntity> pagedResponse = new PageImpl(lista);
        when(rolRepository.listarRolesVigentes(filtro, pageable)).thenReturn(pagedResponse);

        perfilamientoService.listarRolesVigentes(numPagina, filtro);
    }

    @Test
    void listarRolesError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.listarRoles(-1, null))
                .withNoCause();
    }

    @Test
    void listarRolesVigentesError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.listarRolesVigentes(-1, null))
                .withNoCause();
    }

    @Test
    void eliminarRol() {

        RolEntity rolEntity = getRolEntity();
        rolEntity.setFechaModificacion(LocalDateTime.now());
        rolEntity.setUsuarioModificacion("USR_TMP");
        rolEntity.setVigencia(false);

        when(rolRepository.findById(any())).thenReturn(Optional.of(rolEntity));

        perfilamientoService.eliminarRol(1l);
    }

    @Test
    void eliminarRolNull() {
        when(rolRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarRol(1l))
                .withNoCause();
    }

    @Test
    void eliminarRoIdInvalidoError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarRol(-1l))
                .withNoCause();
    }

    @Test
    void eliminarRoIdINullError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarRol(null))
                .withNoCause();
    }

    @Test
    void eliminarRolErrorPorUso() {

        RolEntity rolEntity = getRolEntity();
        rolEntity.setFechaModificacion(LocalDateTime.now());
        rolEntity.setUsuarioModificacion("USR_TMP");
        rolEntity.setVigencia(false);

        when(rolRepository.findById(any())).thenReturn(Optional.of(rolEntity));

        PerfilRolEntity perfilRolEntity = getPerfilRolEntity();
        List<PerfilRolEntity> perfilRolEntities = Arrays.asList(perfilRolEntity);
        when(perfilRolRepository.findAllByRolEntity(rolEntity)).thenReturn(perfilRolEntities);


        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarRol(1l))
                .withNoCause();
    }

    @Test
    void actualizarRol() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();

        when(rolRepository.validarRolMismoNombreDistintoId(any(), any())).thenReturn(0l);

        RolEntity rolEntity = getRolEntity();
        Optional<RolEntity> rolEntityOptional = Optional.of(rolEntity);
        when(rolRepository.findById(any())).thenReturn(rolEntityOptional);


        ModuloEntity moduloEntity = getModuloEntity();
        Optional<ModuloEntity> moduloEntityOptional = Optional.of(moduloEntity);
        when(moduloRepository.findByModuloId(any())).thenReturn(moduloEntityOptional);

        List<AccionEntity> accionEntities = Arrays.asList(getAccionEntity());
        when(accionRepository.findAllById(actualizarRolRequestDTO.getAcciones())).thenReturn(accionEntities);

        perfilamientoService.actualizarRol(actualizarRolRequestDTO);
    }

    @Test
    void actualizarRolErrorNombreRepetido() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();
        when(rolRepository.validarRolMismoNombreDistintoId(any(), any())).thenReturn(1l);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarRol(actualizarRolRequestDTO))
                .withNoCause();
    }

    @Test
    void actualizarRolErrorNoExisteRol() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();
        when(rolRepository.validarRolMismoNombreDistintoId(any(), any())).thenReturn(0l);

        Optional<RolEntity> rolEntityOptional = Optional.ofNullable(null);
        when(rolRepository.findById(any())).thenReturn(rolEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarRol(actualizarRolRequestDTO))
                .withNoCause();
    }


    @Test
    void actualizarRolErrorModuloInvalido() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();
        when(rolRepository.validarRolMismoNombreDistintoId(any(), any())).thenReturn(0l);

        RolEntity rolEntity = getRolEntity();
        Optional<RolEntity> rolEntityOptional = Optional.of(rolEntity);
        when(rolRepository.findById(any())).thenReturn(rolEntityOptional);


        Optional<ModuloEntity> moduloEntityOptional = Optional.ofNullable(null);
        when(moduloRepository.findByModuloId(any())).thenReturn(moduloEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarRol(actualizarRolRequestDTO))
                .withNoCause();
    }

    @Test
    void actualizarRolIdRolNullError() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();
        actualizarRolRequestDTO.setRolId(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarRol(actualizarRolRequestDTO))
                .withNoCause();
    }

    @Test
    void actualizarRolIdRolINvalidoError() {

        ActualizarRolRequestDTO actualizarRolRequestDTO = actualizarRolDTO();
        actualizarRolRequestDTO.setRolId(-1l);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarRol(actualizarRolRequestDTO))
                .withNoCause();
    }


    @Test
    void eliminarPerfil() {

        PerfilEntity perfilEntity = getPerfilEntity();
        perfilEntity.setUsuarioModificacion("USR_TMP");
        perfilEntity.setFechaModificacion(LocalDateTime.now());
        perfilEntity.setVigencia(false);

        when(perfilRepository.findById(any())).thenReturn(Optional.of(perfilEntity));

        perfilamientoService.eliminarPerfil(1l);
    }

    @Test
    void eliminarPerfilNull() {
        when(perfilRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarPerfil(1l))
                .withNoCause();
    }

    @Test
    void eliminarPerfilIdPerfilInvalidoError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarPerfil(-1l))
                .withNoCause();
    }

    @Test
    void eliminarPerfilIdPerfilNullError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarPerfil(null))
                .withNoCause();
    }


    @Test
    void eliminarPerfilErrorPorUso() {

        PerfilEntity perfilEntity = getPerfilEntity();
        perfilEntity.setUsuarioModificacion("USR_TMP");
        perfilEntity.setFechaModificacion(LocalDateTime.now());
        perfilEntity.setVigencia(false);

        when(perfilRepository.findById(any())).thenReturn(Optional.of(perfilEntity));
        PerfilFuncionarioEntity perfilFuncionarioEntity = getPerfilFuncionarioEntity();

        when(perfilFuncionarioRepository.findAllByPerfilEntityPerfilId(any())).thenReturn(Arrays.asList(perfilFuncionarioEntity));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.eliminarPerfil(1l))
                .withNoCause();
    }

    @Test
    void crearRol() {

        CrearRolRequestDTO requestDTO = crearRolDTO();

        when(rolRepository.verificaRolPreexistente(requestDTO.getNombre())).thenReturn(null);

        ModuloEntity moduloEntity = getModuloEntity();
        Optional<ModuloEntity> moduloEntityOptional = Optional.of(moduloEntity);
        when(moduloRepository.findByModuloId(requestDTO.getModulo())).thenReturn(moduloEntityOptional);


        List<AccionEntity> accionEntities = Arrays.asList(getAccionEntity());
        when(accionRepository.findAllById(requestDTO.getAcciones())).thenReturn(accionEntities);

        perfilamientoService.crearRol(requestDTO);
    }

    @Test
    void crearRolNombreNullError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();
        requestDTO.setNombre(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }

    @Test
    void crearRolNombreEmptyError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();
        requestDTO.setNombre("");

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }

    @Test
    void crearRolAccionesNullError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();
        requestDTO.setAcciones(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }

    @Test
    void crearRolAccionesEmptyError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();
        List<Long> acciones = new ArrayList<>();
        requestDTO.setAcciones(acciones);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }

    @Test
    void crearRolRequestNull() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(null))
                .withNoCause();
    }

    @Test
    void crearRolExistenteError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();

        RolEntity rolEntity = getRolEntity();

        when(rolRepository.verificaRolPreexistente(requestDTO.getNombre())).thenReturn(rolEntity);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }

    @Test
    void crearRolModuloVacioError() {

        CrearRolRequestDTO requestDTO = crearRolDTO();

        when(rolRepository.verificaRolPreexistente(requestDTO.getNombre())).thenReturn(null);

        Optional<ModuloEntity> moduloEntityOptional = Optional.ofNullable(null);
        when(moduloRepository.findByModuloId(requestDTO.getModulo())).thenReturn(moduloEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearRol(requestDTO))
                .withNoCause();
    }


    @Test
    void crearPerfil() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        when(perfilRepository.existePerfilConMismoNombre(requestDTO.getNombre())).thenReturn(false);

        RolEntity rolEntity = getRolEntity();
        Optional<RolEntity> rolEntityOptional = Optional.ofNullable(rolEntity);
        when(rolRepository.findById(requestDTO.getRoles().get(0))).thenReturn(rolEntityOptional);

        perfilamientoService.crearPerfil(requestDTO);
    }

    @Test
    void crearPerfilExistenteError() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        when(perfilRepository.existePerfilConMismoNombre(requestDTO.getNombre())).thenReturn(true);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilRequestNullError() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(null))
                .withNoCause();
    }

    @Test
    void crearPerfilRolesNullError() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        requestDTO.setRoles(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilRolesEmptyError() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        List<Long> roles = new ArrayList<>();
        requestDTO.setRoles(roles);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilNombreEmptyError() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        requestDTO.setNombre("");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilNombreNullError() {

        CrearPerfilDTO requestDTO = crearPerfilDTO();
        requestDTO.setNombre(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfil(requestDTO))
                .withNoCause();
    }

    @Test
    void listarPerfiles() {
        int numPagina = 0;
        String filtro = null;

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<PerfilEntity> page = Mockito.mock(Page.class);
        when(perfilRepository.findAll(pageable)).thenReturn(page);


        perfilamientoService.listarPerfiles(numPagina, filtro);
    }

    @Test
    void listaPerfilesFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));
        Page<PerfilEntity> page = Mockito.mock(Page.class);
        when(perfilRepository.findAll(pageable)).thenReturn(page);

        perfilamientoService.listarPerfiles(numPagina, filtro);
    }

    @Test
    void listarPerfilesConFiltro() {
        int numPagina = 0;
        String filtro = "admin";

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        PerfilEntity perfilEntity = getPerfilEntity();
        List<PerfilEntity> lista = Arrays.asList(perfilEntity);
        Page<PerfilEntity> pagedResponse = new PageImpl(lista);
        when(perfilRepository.listarPerfiles(filtro, pageable)).thenReturn(pagedResponse);

        perfilamientoService.listarPerfiles(numPagina, filtro);
    }

    @Test
    void listarPerfilesError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.listarPerfiles(-1, null))
                .withNoCause();
    }


    @Test
    void actualizarPerfil() {

        ModificaPerfilDTO modificaPerfilDTO = modificarPerfilDTO();
        when(perfilRepository.existePerfilMismoNombreDistintoPerfil(any(), any())).thenReturn(false);

        PerfilEntity perfilEntity = getPerfilEntity();
        Optional<PerfilEntity> perfilEntityOptional = Optional.of(perfilEntity);
        when(perfilRepository.findById(any())).thenReturn(perfilEntityOptional);


        PerfilRolEntity perfilRolEntity = getPerfilRolEntity();
        when(perfilRolRepository.findAllByPerfilEntityPerfilId(any())).thenReturn(Arrays.asList(perfilRolEntity));


        RolEntity rolEntity = getRolEntity();
        when(rolRepository.findAllById(any())).thenReturn(Arrays.asList(rolEntity));


        perfilamientoService.modificarPerfil(modificaPerfilDTO);
    }

    @Test
    void actualizarPerfilIdPerfilNullError() {

        ModificaPerfilDTO modificaPerfilDTO = modificarPerfilDTO();
        modificaPerfilDTO.setPerfilId(null);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.modificarPerfil(modificaPerfilDTO))
                .withNoCause();
    }

    @Test
    void actualizarPerfilIdPerfilInvalidoError() {

        ModificaPerfilDTO modificaPerfilDTO = modificarPerfilDTO();
        modificaPerfilDTO.setPerfilId(-1l);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.modificarPerfil(modificaPerfilDTO))
                .withNoCause();
    }

    @Test
    void actualizarPerfilErrorNombreRepetido() {
        ModificaPerfilDTO modificaPerfilDTO = modificarPerfilDTO();
        when(perfilRepository.existePerfilMismoNombreDistintoPerfil(any(), any())).thenReturn(true);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.modificarPerfil(modificaPerfilDTO))
                .withNoCause();
    }

    @Test
    void actualizarPerfilErrorPerfilInexistente() {

        ModificaPerfilDTO modificaPerfilDTO = modificarPerfilDTO();
        when(perfilRepository.existePerfilMismoNombreDistintoPerfil(any(), any())).thenReturn(false);

        Optional<PerfilEntity> perfilEntityOptional = Optional.ofNullable(null);
        when(perfilRepository.findById(any())).thenReturn(perfilEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.modificarPerfil(modificaPerfilDTO))
                .withNoCause();
    }


    private CrearPerfilDTO crearPerfilDTO() {
        CrearPerfilDTO crearPerfilDTO = new CrearPerfilDTO();
        crearPerfilDTO.setNombre("Perfll Full");
        crearPerfilDTO.setRoles(Arrays.asList(1l));

        return crearPerfilDTO;
    }

    private CrearRolRequestDTO crearRolDTO() {
        CrearRolRequestDTO requestDTO = new CrearRolRequestDTO();
        requestDTO.setModulo(1l);
        requestDTO.setNombre("Rol 1");

        List<Long> acciones = Arrays.asList(1l, 2l);
        requestDTO.setAcciones(acciones);

        return requestDTO;
    }

    private RolEntity getRolEntity() {
        RolEntity rolEntity = new RolEntity();
        rolEntity.setRolId(1l);
        rolEntity.setNombre("Rol 1");
        rolEntity.setVigencia(true);
        rolEntity.setFechaRegistro(LocalDateTime.now());
        rolEntity.setUsuarioRegistro("USR_TMP");
        ModuloEntity moduloEntity = getModuloEntity();
        rolEntity.setModuloEntity(moduloEntity);


        return rolEntity;
    }

    private PerfilEntity getPerfilEntity() {
        PerfilEntity perfilEntity = new PerfilEntity();
        perfilEntity.setPerfilId(1l);
        perfilEntity.setNombre("Administrador");
        perfilEntity.setVigencia(true);
        perfilEntity.setFechaRegistro(LocalDateTime.now());
        perfilEntity.setUsuarioRegistro("USR_TMP");

        return perfilEntity;
    }


    private AccionEntity getAccionEntity() {
        AccionEntity accionEntity = new AccionEntity();
        accionEntity.setAccionId(1l);
        accionEntity.setNombre("CREAR");

        return accionEntity;
    }

    private ModuloEntity getModuloEntity() {
        ModuloEntity moduloEntity = new ModuloEntity();
        moduloEntity.setModuloId(1l);
        moduloEntity.setNombre("Solicitud Admision");
        moduloEntity.setVigencia(true);

        return moduloEntity;
    }


    private PerfilRolEntity getPerfilRolEntity() {
        PerfilRolEntity perfilRolEntity = new PerfilRolEntity();
        perfilRolEntity.setPerfilEntity(getPerfilEntity());
        perfilRolEntity.setRolEntity(getRolEntity());
        perfilRolEntity.setPerfilRolId(1l);

        return perfilRolEntity;
    }


    private RolAccionEntity getRolAccionEntity() {
        RolAccionEntity rolAccionEntity = new RolAccionEntity();
        rolAccionEntity.setRolEntity(getRolEntity());
        rolAccionEntity.setAccionEntity(getAccionEntity());
        rolAccionEntity.setRolAccionId(1l);

        return rolAccionEntity;

    }

    private PerfilFuncionarioEntity getPerfilFuncionarioEntity() {
        PerfilFuncionarioEntity perfilFuncionarioEntity = new PerfilFuncionarioEntity();
        perfilFuncionarioEntity.setPerfilFuncionarioId(1l);
        perfilFuncionarioEntity.setPerfilEntity(getPerfilEntity());
        perfilFuncionarioEntity.setFuncionariosEntity(getFuncionarioEntity());
        return perfilFuncionarioEntity;
    }

    private FuncionariosEntity getFuncionarioEntity() {
        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("11111111-1");
        funcionariosEntity.setNombreCompleto("Juan Perez");

        return funcionariosEntity;
    }


    private ActualizarRolRequestDTO actualizarRolDTO() {
        ActualizarRolRequestDTO actualizarRolRequestDTO = new ActualizarRolRequestDTO();
        actualizarRolRequestDTO.setRolId(1l);
        actualizarRolRequestDTO.setModulo(1l);
        actualizarRolRequestDTO.setVigencia(true);
        actualizarRolRequestDTO.setNombre("Rol Modificado");
        actualizarRolRequestDTO.setAcciones(Arrays.asList(1l, 2l));

        return actualizarRolRequestDTO;
    }

    private ModificaPerfilDTO modificarPerfilDTO() {
        ModificaPerfilDTO modificaPerfilDTO = new ModificaPerfilDTO();
        modificaPerfilDTO.setPerfilId(1l);
        modificaPerfilDTO.setNombre("Admin");
        modificaPerfilDTO.setVigencia(true);
        modificaPerfilDTO.setRoles(Arrays.asList(1l));

        return modificaPerfilDTO;
    }

    @Test
    void listarPerfilesVigentesPorFiltroConBadRequestTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.listarPerfilesVigentesPorFiltro(-1, null))
                .withNoCause();

    }

    @Test
    void listarPerfilesVigentesPorFiltroConFiltroNullTest() {
        int numPagina = 1;
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombre"));
        Page<PerfilEntity> page = Mockito.mock(Page.class);
        when(perfilRepository.listarPerfilesVigentes(pageable)).thenReturn(page);

        perfilamientoService.listarPerfilesVigentesPorFiltro(numPagina, null);
    }

    @Test
    void listarPerfilesVigentesPorFiltroConFiltroEmptyTest() {
        int numPagina = 1;
        String filtro = "";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombre"));
        Page<PerfilEntity> page = Mockito.mock(Page.class);
        when(perfilRepository.listarPerfilesVigentes(pageable)).thenReturn(page);

        perfilamientoService.listarPerfilesVigentesPorFiltro(numPagina, filtro);
    }

    @Test
    void listarPerfilesVigentesPorFiltroConFiltroTest() {
        int numPagina = 1;
        String filtro = "perfil 1";
        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("nombre"));

        PerfilEntity perfilEntity = datosPerfilEntity();
        List<PerfilEntity> lista = Arrays.asList(perfilEntity);
        Page<PerfilEntity> pagedResponse = new PageImpl(lista);
        when(perfilRepository.listarPerfilesVigentesPorFiltro(filtro, pageable)).thenReturn(pagedResponse);

        perfilamientoService.listarPerfilesVigentesPorFiltro(numPagina, filtro);

    }

    @Test
    void listarPerfilesPorFuncionarioTest() {
        String rut = "11111111-1";
        doNothing().when(funcionariosService).validarRut(rut);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        PerfilFuncionarioEntity perfilFuncionarioEntity = datosPerfilFuncionarioEntity();
        List<PerfilFuncionarioEntity> lista = Arrays.asList(perfilFuncionarioEntity);
        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(lista);

        perfilamientoService.listarPerfilesPorFuncionario(rut);

    }

    @Test
    void listarPerfilesPorFuncionarioCuandoNoExistenPerfilesAsignadosTest() {
        String rut = "11111111-1";
        doNothing().when(funcionariosService).validarRut(rut);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        List<PerfilFuncionarioEntity> lista = new ArrayList<PerfilFuncionarioEntity>();
        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(lista);

        perfilamientoService.listarPerfilesPorFuncionario(rut);

    }

    @Test
    void crearPerfilesFuncionarioRequestNull() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(null))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioErrorFuncionarioNullTest() {
        CrearPerfilFuncionarioRequestDTO crearPerfilFuncionarioRequestDTO = new CrearPerfilFuncionarioRequestDTO();
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(crearPerfilFuncionarioRequestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioErrorSinPerfilesTest() {
        CrearPerfilFuncionarioRequestDTO crearPerfilFuncionarioRequestDTO = new CrearPerfilFuncionarioRequestDTO();
        crearPerfilFuncionarioRequestDTO.setFuncionario(new FuncionarioRequestDTO());
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(crearPerfilFuncionarioRequestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioErrorConListaPerfilesVacioTest() {
        CrearPerfilFuncionarioRequestDTO crearPerfilFuncionarioRequestDTO = new CrearPerfilFuncionarioRequestDTO();
        crearPerfilFuncionarioRequestDTO.setFuncionario(new FuncionarioRequestDTO());
        crearPerfilFuncionarioRequestDTO.setPerfiles(Arrays.asList());
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(crearPerfilFuncionarioRequestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioErrorNoExistenTodosLosPerfilesTest() {
        CrearPerfilFuncionarioRequestDTO requestDTO = datosCrearPerfilFuncionarioRequestDTO();

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioErrorFuncionarioYaTienePerfilesAsignadosTest() {

        CrearPerfilFuncionarioRequestDTO requestDTO = datosCrearPerfilFuncionarioRequestDTO();

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList(datosPerfilEntity()));

        doNothing().when(funcionariosService).validarRut(requestDTO.getFuncionario().getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getFuncionario().getRut())).thenReturn(rutFormateado);

        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(Arrays.asList(datosPerfilFuncionarioEntity()));

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.crearPerfilesFuncionario(requestDTO))
                .withNoCause();
    }

    @Test
    void crearPerfilesFuncionarioCuandoYaExisteElFuncionarioTest() {

        CrearPerfilFuncionarioRequestDTO requestDTO = datosCrearPerfilFuncionarioRequestDTO();

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList(datosPerfilEntity()));

        doNothing().when(funcionariosService).validarRut(requestDTO.getFuncionario().getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getFuncionario().getRut())).thenReturn(rutFormateado);

        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(Arrays.asList());

        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.empty();
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        perfilamientoService.crearPerfilesFuncionario(requestDTO);
    }

    @Test
    void crearPerfilesFuncionarioTest() {

        CrearPerfilFuncionarioRequestDTO requestDTO = datosCrearPerfilFuncionarioRequestDTO();

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList(datosPerfilEntity()));

        doNothing().when(funcionariosService).validarRut(requestDTO.getFuncionario().getRut());

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getFuncionario().getRut())).thenReturn(rutFormateado);

        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(Arrays.asList());

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        perfilamientoService.crearPerfilesFuncionario(requestDTO);
    }

    @Test
    void actualizarPerfilesFuncionarioNoExisteFuncionarioTest() {

        ActualizarPerfilFuncionarioRequestDTO requestDTO = datosActualizarPerfilFuncionarioRequestDTO();
        doNothing().when(funcionariosService).validarRut(requestDTO.getRutFuncionario());
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRutFuncionario())).thenReturn(rutFormateado);

        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.empty();
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarPerfilesFuncionario(requestDTO))
                .withNoCause();
    }

    @Test
    void actualizarPerfilesFuncionarioConPerfilesNullTest() {

        ActualizarPerfilFuncionarioRequestDTO requestDTO = new ActualizarPerfilFuncionarioRequestDTO();
        requestDTO.setRutFuncionario("11111111-1");
        requestDTO.setPerfiles(null);
        doNothing().when(funcionariosService).validarRut(requestDTO.getRutFuncionario());
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRutFuncionario())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        perfilamientoService.actualizarPerfilesFuncionario(requestDTO);
    }

    @Test
    void actualizarPerfilesFuncionarioConPerfilesVacioTest() {

        ActualizarPerfilFuncionarioRequestDTO requestDTO = new ActualizarPerfilFuncionarioRequestDTO();
        requestDTO.setRutFuncionario("11111111-1");
        requestDTO.setPerfiles(Arrays.asList());
        doNothing().when(funcionariosService).validarRut(requestDTO.getRutFuncionario());
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRutFuncionario())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(Arrays.asList());

        perfilamientoService.actualizarPerfilesFuncionario(requestDTO);
    }

    @Test
    void actualizarPerfilesFuncionarioNoExistenTodosLosPerfilesTest() {

        ActualizarPerfilFuncionarioRequestDTO requestDTO = datosActualizarPerfilFuncionarioRequestDTO();
        doNothing().when(funcionariosService).validarRut(requestDTO.getRutFuncionario());
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRutFuncionario())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.actualizarPerfilesFuncionario(requestDTO))
                .withNoCause();
    }

    @Test
    void actualizarPerfilesFuncionarioTest() {

        ActualizarPerfilFuncionarioRequestDTO requestDTO = datosActualizarPerfilFuncionarioRequestDTO();
        doNothing().when(funcionariosService).validarRut(requestDTO.getRutFuncionario());
        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(requestDTO.getRutFuncionario())).thenReturn(rutFormateado);

        FuncionariosEntity funcionariosEntity = datosFuncionariosEntity();
        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.of(funcionariosEntity);
        when(funcionariosRepository.findByRut(rutFormateado)).thenReturn(funcionariosEntityOptional);

        when(perfilRepository.findAllById(requestDTO.getPerfiles())).thenReturn(Arrays.asList(datosPerfilEntity()));

        when(perfilFuncionarioRepository.findAllByFuncionariosEntityRut(rutFormateado)).thenReturn(Arrays.asList());

        perfilamientoService.actualizarPerfilesFuncionario(requestDTO);
    }

    @Test
    void obtenerRolOK() {

        Long rolId = 1l;
        RolEntity rolEntity = getRolEntity();

        Optional<RolEntity> rolEntityOptional = Optional.of(rolEntity);
        when(rolRepository.findById(rolId)).thenReturn(rolEntityOptional);

        RolAccionEntity rolAccionEntity = getRolAccionEntity();
        List<RolAccionEntity> rolAccionEntities = Arrays.asList(rolAccionEntity);
        when(rolAccionRepository.findAllByRolEntityRolId(rolEntity.getRolId())).thenReturn(rolAccionEntities);


        perfilamientoService.obtenerRol(rolId);
    }

    @Test
    void obtenerRolInexistenteError() {

        Long rolId = 1l;
        RolEntity rolEntity = getRolEntity();

        Optional<RolEntity> rolEntityOptional = Optional.ofNullable(null);
        when(rolRepository.findById(rolId)).thenReturn(rolEntityOptional);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.obtenerRol(rolId))
                .withNoCause();
    }

    @Test
    void obtenerRolIdRolNullError() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.obtenerRol(null))
                .withNoCause();
    }

    @Test
    void obtenerRolIdRolInvalidError() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.obtenerRol(-1l))
                .withNoCause();
    }

    @Test
    void obtenerPerfilOK() {

        Long perfilId = 1l;

        PerfilEntity perfilEntity = getPerfilEntity();
        Optional<PerfilEntity> perfilEntityOptional = Optional.of(perfilEntity);

        when(perfilRepository.findById(perfilId)).thenReturn(perfilEntityOptional);

        PerfilRolEntity perfilRolEntity = getPerfilRolEntity();
        List<PerfilRolEntity> perfilRolEntities = Arrays.asList(perfilRolEntity);
        when(perfilRolRepository.findAllByPerfilEntityPerfilId(perfilEntity.getPerfilId())).thenReturn(perfilRolEntities);


        perfilamientoService.obtenerPerfil(perfilId);
    }

    @Test
    void obtenerPerfilIdPerfilNullError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.obtenerPerfil(null))
                .withNoCause();
    }

    @Test
    void obtenerPerfilIdPerfilInvalidError() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> perfilamientoService.obtenerPerfil(-1l))
                .withNoCause();
    }

    @Test
    void obtenerPerfilNoExisteError() {

        Long perfilId = 1l;

        Optional<PerfilEntity> perfilEntityOptional = Optional.ofNullable(null);

        when(perfilRepository.findById(perfilId)).thenReturn(perfilEntityOptional);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> perfilamientoService.obtenerPerfil(1l))
                .withNoCause();
    }

    private CrearPerfilFuncionarioRequestDTO datosCrearPerfilFuncionarioRequestDTO() {
        CrearPerfilFuncionarioRequestDTO crearPerfilFuncionarioRequestDTO = new CrearPerfilFuncionarioRequestDTO();
        crearPerfilFuncionarioRequestDTO.setFuncionario(datosFuncionarioRequestDTO());
        crearPerfilFuncionarioRequestDTO.setPerfiles(Arrays.asList(Long.valueOf(1)));

        return crearPerfilFuncionarioRequestDTO;
    }

    private ActualizarPerfilFuncionarioRequestDTO datosActualizarPerfilFuncionarioRequestDTO() {
        ActualizarPerfilFuncionarioRequestDTO actualizarPerfilFuncionarioRequestDTO = new ActualizarPerfilFuncionarioRequestDTO();
        actualizarPerfilFuncionarioRequestDTO.setRutFuncionario("11111111-1");
        actualizarPerfilFuncionarioRequestDTO.setPerfiles(Arrays.asList(Long.valueOf(1)));

        return actualizarPerfilFuncionarioRequestDTO;
    }

    private PerfilEntity datosPerfilEntity() {
        PerfilEntity perfilEntity = new PerfilEntity();
        perfilEntity.setPerfilId(1);
        perfilEntity.setNombre("perfil 1");
        perfilEntity.setVigencia(true);

        return perfilEntity;
    }

    private PerfilFuncionarioEntity datosPerfilFuncionarioEntity() {
        PerfilFuncionarioEntity perfilFuncionarioEntity = new PerfilFuncionarioEntity();
        perfilFuncionarioEntity.setPerfilFuncionarioId(1);
        perfilFuncionarioEntity.setFuncionariosEntity(datosFuncionariosEntity());
        perfilFuncionarioEntity.setPerfilEntity(datosPerfilEntity());

        return perfilFuncionarioEntity;
    }

    private FuncionariosEntity datosFuncionariosEntity() {
        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("111111111");
        funcionariosEntity.setNombreCompleto("Juan Perez");
        funcionariosEntity.setNombreUsuario("juan.perez");
        funcionariosEntity.setNombreCargo("Administrador");
        funcionariosEntity.setNombreSucursal("Santiago");
        funcionariosEntity.setSucursalId(1);
        funcionariosEntity.setCargoId(1);
        funcionariosEntity.setVigencia(true);

        return funcionariosEntity;
    }

    private FuncionarioRequestDTO datosFuncionarioRequestDTO() {
        FuncionarioRequestDTO funcionarioRequestDTO = new FuncionarioRequestDTO();
        funcionarioRequestDTO.setRut("11111111-1");
        funcionarioRequestDTO.setNombreCompleto("Juan Perez");
        funcionarioRequestDTO.setNombreUsuario("juan.perez");
        funcionarioRequestDTO.setNombreCargo("Administrador");
        funcionarioRequestDTO.setNombreSucursal("Santiago");
        funcionarioRequestDTO.setSucursalId(1);
        funcionarioRequestDTO.setCargoId(1);

        return funcionarioRequestDTO;
    }
}
