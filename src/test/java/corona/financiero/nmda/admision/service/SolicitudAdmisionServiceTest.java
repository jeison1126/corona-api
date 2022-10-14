package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.FiltroSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.dto.ListaSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudAdmisionServiceTest {

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;
    @Mock
    private AdmisionFaseRepository admisionFaseRepository;

    @InjectMocks
    private SolicitudAdmisionService solicitudAdmisionService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private FuncionariosRepository funcionariosRepository;

    @Mock
    private SucursalesRepository sucursalesRepository;

    @Mock
    private AdmisionReglaNegocioRepository admisionReglaNegocioRepository;

    @Mock
    private SolicitudAdmisionFiltrosRepositoryImpl solicitudAdmisionFiltrosRepository;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(solicitudAdmisionService, "paginacion", 15);
    }

    private static final String ORDENAMIENTO = "fechaRegistro";

    @Test
    void obtenerEstadoSolicitudAdmisionProspectoTest() {

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(anyLong())).thenReturn(solicitudAdmisionEntity);

        AdmisionFaseEntity admisionFaseEntity = datosAdmisionFaseEntity();

        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(Arrays.asList(admisionFaseEntity));

        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = datosAdmisionReglaNegocioEntity();
        when(admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(admisionFaseEntity.getSolicitudAdmisionEntity(), admisionFaseEntity.getParFaseEntity())).thenReturn(Arrays.asList(admisionReglaNegocioEntity));

        solicitudAdmisionService.obtenerEstadoSolicitudAdmision(1);
    }


    @Test
    void obtenerEstadoSolicitudAdmisionProspectoErrorTest() {
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.obtenerEstadoSolicitudAdmision(-1))
                .withNoCause();
    }


    @Test
    void obtenerEstadoSolicitudAdmisionNoContentTest() {
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(anyLong())).thenReturn(null);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> solicitudAdmisionService.obtenerEstadoSolicitudAdmision(1))
                .withNoCause();
    }

    @Test
    void obtenerEstadoSolicitudAdmisionFasesNoContentTest() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        when(solicitudAdmisionRepository.findByProspectoEntityProspectoIdAndVigenciaIsTrue(anyLong())).thenReturn(solicitudAdmisionEntity);

        List<AdmisionFaseEntity> lista = new ArrayList<>();

        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(lista);
        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> solicitudAdmisionService.obtenerEstadoSolicitudAdmision(1))
                .withNoCause();
    }

    @Test
    void obtenerDetalleSolicitudAdmisionTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "11111111-1";

        when(validaciones.validaRut(rutProspecto)).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rutProspecto)).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());

        ParEstadoSolicitudEntity estadoSolicitud = new ParEstadoSolicitudEntity();
        estadoSolicitud.setDescripcion("Iniciado");
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(estadoSolicitud);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setEmail("a@b.cl");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setRut("111111111");
        when(validaciones.formateaRutHaciaFront(prospectoEntity.getRut())).thenReturn(rutProspecto);
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity);

        ParCanalEntity canalEntity = new ParCanalEntity();
        canalEntity.setCanalId(1l);
        canalEntity.setDescripcion("Tienda");

        solicitudAdmisionEntity.setParCanalEntity(canalEntity);

        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("999999999");
        funcionariosEntity.setCargoId(1l);
        funcionariosEntity.setNombreCompleto("Pedro Soto");

        when(funcionariosRepository.findByNombreUsuario(solicitudAdmisionEntity.getUsuarioRegistro())).thenReturn(Optional.of(funcionariosEntity));


        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setCodigoSucursal(1l);
        sucursalesEntity.setDescripcionSucursal("Santiago");
        sucursalesEntity.setDescripcionZonaGeografica("Santiago Centro");

        when(sucursalesRepository.findById(solicitudAdmisionEntity.getSucursalId())).thenReturn(Optional.of(sucursalesEntity));


        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1l);

        ParFaseEntity faseEntity = new ParFaseEntity();
        faseEntity.setFaseId(1l);
        faseEntity.setDescripcion("qwerty");

        admisionFaseEntity.setParFaseEntity(faseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(Arrays.asList(admisionFaseEntity));


        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setDescripcion("Regla 1");
        admisionReglaNegocioEntity.setReglaNegocioEntity(reglaNegocioEntity);
        when(admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(solicitudAdmisionEntity, faseEntity)).thenReturn(Arrays.asList(admisionReglaNegocioEntity));


        when(solicitudAdmisionRepository.detalleSolicitudAdmision(solicitudAdmisionId, rutFormateado)).thenReturn(Optional.of(solicitudAdmisionEntity));


        solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto);

    }

    @Test
    void obtenerDetalleSolicitudAdmisionSolicitudAdmisionIdErrorTest() {
        long solicitudAdmisionId = 0l;
        String rutProspecto = "11111111-1";

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto))
                .withNoCause();
    }

    @Test
    void obtenerDetalleSolicitudAdmisionRutNullErrorTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = null;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto))
                .withNoCause();
    }

    @Test
    void obtenerDetalleSolicitudAdmisionRutEmptyErrorTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "";

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto))
                .withNoCause();
    }

    @Test
    void obtenerDetalleSolicitudAdmisionRutValidacionErrorTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "11111111-2";

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto))
                .withNoCause();
    }


    @Test
    void obtenerDetalleSolicitudAdmisionEmptyErrorTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "11111111-1";

        when(validaciones.validaRut(rutProspecto)).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rutProspecto)).thenReturn(rutFormateado);


        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntity = Optional.ofNullable(null);

        when(solicitudAdmisionRepository.detalleSolicitudAdmision(solicitudAdmisionId, rutFormateado)).thenReturn(solicitudAdmisionEntity);


        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto))
                .withNoCause();

    }

    @Test
    void obtenerDetalleSolicitudAdmisionSinFuncionariosTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "11111111-1";

        when(validaciones.validaRut(rutProspecto)).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rutProspecto)).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());

        ParEstadoSolicitudEntity estadoSolicitud = new ParEstadoSolicitudEntity();
        estadoSolicitud.setDescripcion("Iniciado");
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(estadoSolicitud);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setEmail("a@b.cl");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setRut("111111111");
        when(validaciones.formateaRutHaciaFront(prospectoEntity.getRut())).thenReturn(rutProspecto);
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity);

        ParCanalEntity canalEntity = new ParCanalEntity();
        canalEntity.setCanalId(1l);
        canalEntity.setDescripcion("Tienda");

        solicitudAdmisionEntity.setParCanalEntity(canalEntity);

        Optional<FuncionariosEntity> funcionariosEntityOptional = Optional.ofNullable(null);
        when(funcionariosRepository.findByNombreUsuario(solicitudAdmisionEntity.getUsuarioRegistro())).thenReturn(funcionariosEntityOptional);


        SucursalesEntity sucursalesEntity = new SucursalesEntity();
        sucursalesEntity.setCodigoSucursal(1l);
        sucursalesEntity.setDescripcionSucursal("Santiago");
        sucursalesEntity.setDescripcionZonaGeografica("Santiago Centro");

        when(sucursalesRepository.findById(solicitudAdmisionEntity.getSucursalId())).thenReturn(Optional.of(sucursalesEntity));


        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1l);

        ParFaseEntity faseEntity = new ParFaseEntity();
        faseEntity.setFaseId(1l);
        faseEntity.setDescripcion("qwerty");

        admisionFaseEntity.setParFaseEntity(faseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(Arrays.asList(admisionFaseEntity));


        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setDescripcion("Regla 1");
        admisionReglaNegocioEntity.setReglaNegocioEntity(reglaNegocioEntity);
        when(admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(solicitudAdmisionEntity, faseEntity)).thenReturn(Arrays.asList(admisionReglaNegocioEntity));


        when(solicitudAdmisionRepository.detalleSolicitudAdmision(solicitudAdmisionId, rutFormateado)).thenReturn(Optional.of(solicitudAdmisionEntity));


        solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto);

    }


    @Test
    void obtenerDetalleSolicitudAdmisionSinSucursalesTest() {
        long solicitudAdmisionId = 1l;
        String rutProspecto = "11111111-1";

        when(validaciones.validaRut(rutProspecto)).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rutProspecto)).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());

        ParEstadoSolicitudEntity estadoSolicitud = new ParEstadoSolicitudEntity();
        estadoSolicitud.setDescripcion("Iniciado");
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(estadoSolicitud);

        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setEmail("a@b.cl");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setRut("111111111");
        when(validaciones.formateaRutHaciaFront(prospectoEntity.getRut())).thenReturn(rutProspecto);
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity);

        ParCanalEntity canalEntity = new ParCanalEntity();
        canalEntity.setCanalId(1l);
        canalEntity.setDescripcion("Tienda");

        solicitudAdmisionEntity.setParCanalEntity(canalEntity);

        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("999999999");
        funcionariosEntity.setCargoId(1l);
        funcionariosEntity.setNombreCompleto("Pedro Soto");

        when(funcionariosRepository.findByNombreUsuario(solicitudAdmisionEntity.getUsuarioRegistro())).thenReturn(Optional.of(funcionariosEntity));


        Optional<SucursalesEntity> sucursalesEntity = Optional.ofNullable(null);

        when(sucursalesRepository.findById(solicitudAdmisionEntity.getSucursalId())).thenReturn(sucursalesEntity);


        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1l);

        ParFaseEntity faseEntity = new ParFaseEntity();
        faseEntity.setFaseId(1l);
        faseEntity.setDescripcion("qwerty");

        admisionFaseEntity.setParFaseEntity(faseEntity);
        when(admisionFaseRepository.findAllBySolicitudAdmisionEntityOrderByFechaRegistroAsc(solicitudAdmisionEntity)).thenReturn(Arrays.asList(admisionFaseEntity));


        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setDescripcion("Regla 1");
        admisionReglaNegocioEntity.setReglaNegocioEntity(reglaNegocioEntity);
        when(admisionReglaNegocioRepository.findAllBySolicitudAdmisionEntityAndReglaNegocioEntityParFaseEntity(solicitudAdmisionEntity, faseEntity)).thenReturn(Arrays.asList(admisionReglaNegocioEntity));


        when(solicitudAdmisionRepository.detalleSolicitudAdmision(solicitudAdmisionId, rutFormateado)).thenReturn(Optional.of(solicitudAdmisionEntity));


        solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto);

    }

    @Test
    void listarAdmisionPaginaErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(-1);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionRutErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(1);
        filtroSolicitudAdmisionDTO.setRut("11111111-2");
        when(validaciones.validaRut(filtroSolicitudAdmisionDTO.getRut())).thenReturn(false);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);
        filtroSolicitudAdmisionDTO.setRut("11111111-1");
        filtroSolicitudAdmisionDTO.setFechaDesde(new Date());
        filtroSolicitudAdmisionDTO.setFechaHasta(new Date());
        filtroSolicitudAdmisionDTO.setColumnaOrden(1);
        filtroSolicitudAdmisionDTO.setOrden(DireccionOrdenaEnum.ASC);
        when(validaciones.validaRut(filtroSolicitudAdmisionDTO.getRut())).thenReturn(true);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(filtroSolicitudAdmisionDTO.getRut())).thenReturn(rutFormateado);

        Pageable pageable = PageRequest.of(filtroSolicitudAdmisionDTO.getPagina(), 15);


        ListaSolicitudAdmisionDTO l = new ListaSolicitudAdmisionDTO();
        l.setCanalAtencion("Tienda");
        l.setEstadoSolicitud("Iniciado");
        l.setFechaSolicitud(LocalDate.now());
        l.setSolicitudId(1l);
        l.setRut("1111111111");
        l.setSucursal("Santiago");
        l.setFaseEvaluacion("Evaluacion");

        List<ListaSolicitudAdmisionDTO> lista = Arrays.asList(l);
        Page<ListaSolicitudAdmisionDTO> page = new PageImpl(lista);

        LocalDate desde = filtroSolicitudAdmisionDTO.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = filtroSolicitudAdmisionDTO.getFechaHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        when(solicitudAdmisionFiltrosRepository.findAllByNativo(desde, hasta, rutFormateado, filtroSolicitudAdmisionDTO.getCanalAtencion(), filtroSolicitudAdmisionDTO.getSucursal(), filtroSolicitudAdmisionDTO.getEstadoEvaluacion(), filtroSolicitudAdmisionDTO.getFaseEvaluacion(), filtroSolicitudAdmisionDTO.getZonaGeografica(), filtroSolicitudAdmisionDTO.getColumnaOrden(), filtroSolicitudAdmisionDTO.getOrden().name(), pageable)).thenReturn(page);


        solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO);
    }

    @Test
    void listarAdmisionSinRutTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);
        filtroSolicitudAdmisionDTO.setFechaDesde(new Date());
        filtroSolicitudAdmisionDTO.setFechaHasta(new Date());
        filtroSolicitudAdmisionDTO.setColumnaOrden(9);
        filtroSolicitudAdmisionDTO.setOrden(DireccionOrdenaEnum.ASC);
        Pageable pageable = PageRequest.of(filtroSolicitudAdmisionDTO.getPagina(), 15);

        ListaSolicitudAdmisionDTO l = new ListaSolicitudAdmisionDTO();
        l.setCanalAtencion("Tienda");
        l.setEstadoSolicitud("Iniciado");
        l.setFechaSolicitud(LocalDate.now());
        l.setSolicitudId(1l);
        l.setRut("1111111111");
        l.setSucursal("Santiago");
        l.setFaseEvaluacion("Evaluacion");

        List<ListaSolicitudAdmisionDTO> lista = Arrays.asList(l);
        Page<ListaSolicitudAdmisionDTO> page = new PageImpl(lista);

        LocalDate desde = filtroSolicitudAdmisionDTO.getFechaDesde().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hasta = filtroSolicitudAdmisionDTO.getFechaHasta().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        when(solicitudAdmisionFiltrosRepository.findAllByNativo(desde, hasta, null, filtroSolicitudAdmisionDTO.getCanalAtencion(), filtroSolicitudAdmisionDTO.getSucursal(), filtroSolicitudAdmisionDTO.getEstadoEvaluacion(), filtroSolicitudAdmisionDTO.getFaseEvaluacion(), filtroSolicitudAdmisionDTO.getZonaGeografica(), filtroSolicitudAdmisionDTO.getColumnaOrden(), filtroSolicitudAdmisionDTO.getOrden().name(), pageable)).thenReturn(page);


        solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO);
    }

    @Test
    void listarAdmisionFechasNulasTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);
        filtroSolicitudAdmisionDTO.setColumnaOrden(9);
        filtroSolicitudAdmisionDTO.setOrden(DireccionOrdenaEnum.ASC);

        Pageable pageable = PageRequest.of(filtroSolicitudAdmisionDTO.getPagina(), 15);

        ListaSolicitudAdmisionDTO l = new ListaSolicitudAdmisionDTO();
        l.setCanalAtencion("Tienda");
        l.setEstadoSolicitud("Iniciado");
        l.setFechaSolicitud(LocalDate.now());
        l.setSolicitudId(1l);
        l.setRut("1111111111");
        l.setSucursal("Santiago");
        l.setFaseEvaluacion("Evaluacion");

        List<ListaSolicitudAdmisionDTO> lista = Arrays.asList(l);
        Page<ListaSolicitudAdmisionDTO> page = new PageImpl(lista);

        LocalDate hasta = LocalDate.now();
        LocalDate desde = hasta.minusDays(89);

        when(solicitudAdmisionFiltrosRepository.findAllByNativo(desde, hasta, null, filtroSolicitudAdmisionDTO.getCanalAtencion(), filtroSolicitudAdmisionDTO.getSucursal(), filtroSolicitudAdmisionDTO.getEstadoEvaluacion(), filtroSolicitudAdmisionDTO.getFaseEvaluacion(), filtroSolicitudAdmisionDTO.getZonaGeografica(), filtroSolicitudAdmisionDTO.getColumnaOrden(), filtroSolicitudAdmisionDTO.getOrden().name(), pageable)).thenReturn(page);


        solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO);
    }

    @Test
    void listarAdmisionFechasDesdeNulaErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);
        filtroSolicitudAdmisionDTO.setFechaHasta(new Date());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionFechasHastaNulaErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);
        filtroSolicitudAdmisionDTO.setFechaDesde(new Date());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionFechasDesdeMayorActualErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);

        LocalDate tmp = LocalDate.now();

        LocalDate localHasta = tmp.minusDays(2);
        Date hasta = Date.from(localHasta.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaHasta(hasta);


        LocalDate localDate = tmp.plusDays(1);
        Date desde = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaDesde(desde);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionFechasHastaMayorActualErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);

        LocalDate tmp = LocalDate.now();

        Date desde = Date.from(tmp.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaDesde(desde);


        LocalDate localDate = tmp.plusDays(1);
        Date hasta = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaHasta(hasta);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionFechasDesdeMayorHastaErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);

        LocalDate tmp = LocalDate.now();

        Date desde = Date.from(tmp.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaDesde(desde);

        LocalDate localHasta = tmp.minusDays(3);
        Date hasta = Date.from(localHasta.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaHasta(hasta);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }

    @Test
    void listarAdmisionFechasRangosErrorTest() {
        FiltroSolicitudAdmisionDTO filtroSolicitudAdmisionDTO = new FiltroSolicitudAdmisionDTO();
        filtroSolicitudAdmisionDTO.setPagina(0);

        LocalDate tmp = LocalDate.now();
        LocalDate localDate = tmp.minusDays(98);

        Date desde = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaDesde(desde);


        Date hasta = Date.from(tmp.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        filtroSolicitudAdmisionDTO.setFechaHasta(hasta);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> solicitudAdmisionService.listaSolicitudAdmision(filtroSolicitudAdmisionDTO))
                .withNoCause();
    }


    private AdmisionReglaNegocioEntity datosAdmisionReglaNegocioEntity() {
        AdmisionReglaNegocioEntity admisionReglaNegocioEntity = new AdmisionReglaNegocioEntity();
        admisionReglaNegocioEntity.setReglaNegocioEntity(datosReglaNegocio());
        admisionReglaNegocioEntity.setSolicitudAdmisionEntity(datosSolicitudAdmisionEntity());
        admisionReglaNegocioEntity.setAdmisionReglaNegocioId(1l);

        return admisionReglaNegocioEntity;
    }

    private ReglaNegocioEntity datosReglaNegocio() {
        ReglaNegocioEntity reglaNegocioEntity = new ReglaNegocioEntity();
        reglaNegocioEntity.setId(1l);
        reglaNegocioEntity.setDescripcion("Regla 1");
        reglaNegocioEntity.setParMensajeRechazoEntity(datosMensajesRechazo());
        return reglaNegocioEntity;
    }

    private ParMensajeRechazoEntity datosMensajesRechazo() {
        ParMensajeRechazoEntity mensajeRechazoEntity = new ParMensajeRechazoEntity();
        mensajeRechazoEntity.setMensajeRechazoId(1l);
        mensajeRechazoEntity.setMensajeFuncional("Funcional");
        mensajeRechazoEntity.setDescripcion("Descripcion");

        return mensajeRechazoEntity;
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

    private ParFaseEntity datosFasesEntity() {
        ParFaseEntity parFaseEntity = new ParFaseEntity();
        parFaseEntity.setFaseId(1);
        parFaseEntity.setDescripcion("Iniciado");

        return parFaseEntity;
    }

    private AdmisionFaseEntity datosAdmisionFaseEntity() {
        AdmisionFaseEntity admisionFaseEntity = new AdmisionFaseEntity();
        admisionFaseEntity.setAdmisionFaseId(1);
        admisionFaseEntity.setVigencia(true);
        admisionFaseEntity.setFechaRegistro(LocalDateTime.now());
        admisionFaseEntity.setParFaseEntity(datosFasesEntity());

        return admisionFaseEntity;
    }
}
