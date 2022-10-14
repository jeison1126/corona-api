package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.CampaniaResponseDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
import corona.financiero.nmda.admision.util.EnumOrigenSolcitudAdmision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaniasServiceTest {

    @Mock
    private CampanaCoronaRepository campanaCoronaRepository;

    @Mock
    private PreEvaluadosScoringRepository preEvaluadosScoringRepository;

    @Mock
    private AdmisionFaseRepository admisionFaseRepository;

    @Mock
    private ParOrigenRepository parOrigenRepository;

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Mock
    private AdmisionFaseService admisionfaseService;

    @InjectMocks
    private CampaniasService campaniasService;

    @Mock
    private ProspectoRepository prospectoRepository;

    @Mock
    private EvaluacionProductoService evaluacionProductoService;

    @Mock
    private CabeceraCampanaRepository cabeceraCampanaRepository;

    @BeforeEach
    public void initEach() {
        ReflectionTestUtils.setField(campaniasService, "paginacion", 15);
    }

    @Test
    void evaluaCampaniaEquifaxTest() {
        String rut = "111111111";
        CampaniaResponseDTO campaniaResponseDTO = obtieneCampaniaEquifaxResponseDTO();
        PreEvaluadosScoringEntity preEvaluadosScoringEntity = obtienePreEvaluadosScoringEntity();
        Optional<PreEvaluadosScoringEntity> preEvaluadosScoringEntityOptional = Optional.of(preEvaluadosScoringEntity);

        when(preEvaluadosScoringRepository.esCampaniaVigente(rut)).thenReturn(preEvaluadosScoringEntityOptional);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_INTERNA);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_SCORING);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);

        ParOrigenEntity parOrigenEntity = obtieneParOrigenEntity();
        Optional<ParOrigenEntity> parOrigenEntityOptional = Optional.of(parOrigenEntity);
        when(parOrigenRepository.findById(campaniaResponseDTO.getOrigen().getCodigo())).thenReturn(parOrigenEntityOptional);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        doNothing().when(evaluacionProductoService).cargaProductosPreEvaluados(solicitudAdmisionEntity, preEvaluadosScoringEntity.getCupoAsignado(), null);

        campaniasService.evaluaCampania(solicitudAdmisionEntity, rut);
    }

    @Test
    void evaluaCampaniaCoronaTest() {
        String rut = "111111111";

        Optional<PreEvaluadosScoringEntity> preEvaluadosScoringEntityOptional = Optional.ofNullable(null);
        when(preEvaluadosScoringRepository.esCampaniaVigente(rut)).thenReturn(preEvaluadosScoringEntityOptional);

        CampaniaResponseDTO campaniaResponseDTO = obtieneCampaniCoronaResponseDTO();

        CampanaCoronaEntity campanaCoronaEntity = obtieneCampanaCoronaEntity();
        Optional<CampanaCoronaEntity> campanaCoronaEntityOptional = Optional.of(campanaCoronaEntity);
        when(campanaCoronaRepository.esCampanaVigente(rut)).thenReturn(campanaCoronaEntityOptional);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_INTERNA);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_SCORING);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);

        ParOrigenEntity parOrigenEntity = obtieneParOrigenEntity();
        Optional<ParOrigenEntity> parOrigenEntityOptional = Optional.of(parOrigenEntity);
        when(parOrigenRepository.findById(campaniaResponseDTO.getOrigen().getCodigo())).thenReturn(parOrigenEntityOptional);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        doNothing().when(evaluacionProductoService).cargaProductosPreEvaluados(solicitudAdmisionEntity, campanaCoronaEntity.getCupoAsignado(), campanaCoronaEntity.getProductoPredeterminado());

        campaniasService.evaluaCampania(solicitudAdmisionEntity, rut);
    }

    @Test
    void evaluaCampaniaCoronaReevaluadoTest() {
        String rut = "111111111";

        Optional<PreEvaluadosScoringEntity> preEvaluadosScoringEntityOptional = Optional.ofNullable(null);
        when(preEvaluadosScoringRepository.esCampaniaVigente(rut)).thenReturn(preEvaluadosScoringEntityOptional);

        CampaniaResponseDTO campaniaResponseDTO = obtieneCampaniCoronaResponseDTO();
        campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.REEVALUADOS);

        CampanaCoronaEntity campanaCoronaEntity = obtieneCampanaCoronaEntity();
        campanaCoronaEntity.setEsFuncionario(false);
        Optional<CampanaCoronaEntity> campanaCoronaEntityOptional = Optional.of(campanaCoronaEntity);
        when(campanaCoronaRepository.esCampanaVigente(rut)).thenReturn(campanaCoronaEntityOptional);

        SolicitudAdmisionEntity solicitudAdmisionEntity = obtieneSolicitudAdmisionEntity();
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_INTERNA);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_SCORING);
        doNothing().when(admisionfaseService).actualizarFaseSolicitud(solicitudAdmisionEntity, Constantes.FASE_EVALUACION_COTIZACIONES);

        ParOrigenEntity parOrigenEntity = obtieneParOrigenEntity();
        Optional<ParOrigenEntity> parOrigenEntityOptional = Optional.of(parOrigenEntity);
        when(parOrigenRepository.findById(campaniaResponseDTO.getOrigen().getCodigo())).thenReturn(parOrigenEntityOptional);

        ProspectoEntity prospectoEntity = obtieneProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        doNothing().when(evaluacionProductoService).cargaProductosPreEvaluados(solicitudAdmisionEntity, campanaCoronaEntity.getCupoAsignado(), campanaCoronaEntity.getProductoPredeterminado());

        campaniasService.evaluaCampania(solicitudAdmisionEntity, rut);
    }

    @Test
    void evaluaCampaniaSinCampaniaTest() {
        String rut = "111111111";

        Optional<PreEvaluadosScoringEntity> preEvaluadosScoringEntityOptional = Optional.ofNullable(null);
        when(preEvaluadosScoringRepository.esCampaniaVigente(rut)).thenReturn(preEvaluadosScoringEntityOptional);

        Optional<CampanaCoronaEntity> campanaCoronaEntityOptional = Optional.ofNullable(null);
        when(campanaCoronaRepository.esCampanaVigente(rut)).thenReturn(campanaCoronaEntityOptional);

        campaniasService.evaluaCampania(obtieneSolicitudAdmisionEntity(), rut);
    }

    @Test
    void eliminarCampaniaTest() {
        CampanaCoronaEntity campanaCoronaEntity = campanaCoronaEntity();
        List<CampanaCoronaEntity> list = Arrays.asList(campanaCoronaEntity);
        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(1l)).thenReturn(list);
        CabeceraCampanaEntity cabeceraCampanaEntity = list.get(0).getCabeceraCampanaEntity();

        doNothing().when(campanaCoronaRepository).deleteAll(list);

        doNothing().when(cabeceraCampanaRepository).delete(cabeceraCampanaEntity);

        campaniasService.eliminarRegistrosCargaMasiva(1l);
    }

    @Test
    void eliminarCampaniaIdErrorTest() {

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> campaniasService.eliminarRegistrosCargaMasiva(-1l))
                .withNoCause();
    }

    @Test
    void eliminarCampaniaNullErrorTest() {
        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(1l)).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> campaniasService.eliminarRegistrosCargaMasiva(1l))
                .withNoCause();
    }

    @Test
    void eliminarCampaniaEmptyErrorTest() {
        List<CampanaCoronaEntity> list = new ArrayList<>();
        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(1l)).thenReturn(list);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> campaniasService.eliminarRegistrosCargaMasiva(1l))
                .withNoCause();
    }


    @Test
    void listarCampanias() {
        int numPagina = 1;

        Pageable pageable = PageRequest.of(numPagina, 15, Sort.by("fechaRegistro"));

        CabeceraCampanaEntity cabeceraCampanaEntity = cabeceraCampanaEntity();
        List<CabeceraCampanaEntity> lista = Arrays.asList(cabeceraCampanaEntity);
        Page<CabeceraCampanaEntity> pagedResponse = new PageImpl(lista);
        when(cabeceraCampanaRepository.findAll(pageable)).thenReturn(pagedResponse);

        campaniasService.listarCampaniaCorona(numPagina);
    }

    @Test
    void listarCampaniasNumpaginaError() {
        int numPagina = -1;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> campaniasService.listarCampaniaCorona(numPagina))
                .withNoCause();
    }

    private CampanaCoronaEntity campanaCoronaEntity() {
        CampanaCoronaEntity campanaCoronaEntity = new CampanaCoronaEntity();
        campanaCoronaEntity.setCampanaCoronaId(1l);
        campanaCoronaEntity.setCabeceraCampanaEntity(cabeceraCampanaEntity());
        LocalDate nacimiento = LocalDate.of(1980, 03, 02);
        campanaCoronaEntity.setFechaNacimiento(nacimiento);
        campanaCoronaEntity.setVigencia(true);
        campanaCoronaEntity.setNombre("Juan");
        campanaCoronaEntity.setApellidoPaterno("Perez");
        campanaCoronaEntity.setApellidoMaterno("Soto");
        campanaCoronaEntity.setRut("11111111-1");
        campanaCoronaEntity.setProductoPredeterminado("Mastercard Full");
        campanaCoronaEntity.setCupoAsignado(350000l);
        campanaCoronaEntity.setFechaRegistro(LocalDateTime.now());
        campanaCoronaEntity.setUsuarioRegistro("USR_TMP");
        campanaCoronaEntity.setEsFuncionario(false);


        return campanaCoronaEntity;
    }

    private List<CampanaCoronaEntity> campanaCoronaEntityList() {
        CampanaCoronaEntity campanaCoronaEntity1 = new CampanaCoronaEntity();
        campanaCoronaEntity1.setCampanaCoronaId(1l);
        campanaCoronaEntity1.setCabeceraCampanaEntity(cabeceraCampanaEntity());
        LocalDate nacimiento = LocalDate.of(1980, 03, 02);
        campanaCoronaEntity1.setFechaNacimiento(nacimiento);
        campanaCoronaEntity1.setVigencia(true);
        campanaCoronaEntity1.setNombre("Juan");
        campanaCoronaEntity1.setApellidoPaterno("Perez");
        campanaCoronaEntity1.setApellidoMaterno("Soto");
        campanaCoronaEntity1.setRut("11111111-1");
        campanaCoronaEntity1.setProductoPredeterminado("Mastercard Full");
        campanaCoronaEntity1.setCupoAsignado(350000l);
        campanaCoronaEntity1.setFechaRegistro(LocalDateTime.now());
        campanaCoronaEntity1.setUsuarioRegistro("USR_TMP");
        campanaCoronaEntity1.setEsFuncionario(false);


        CampanaCoronaEntity campanaCoronaEntity2 = new CampanaCoronaEntity();
        campanaCoronaEntity2.setCampanaCoronaId(1l);
        campanaCoronaEntity2.setCabeceraCampanaEntity(cabeceraCampanaEntity());
        LocalDate nacimiento2 = LocalDate.of(1982, 05, 05);
        campanaCoronaEntity2.setFechaNacimiento(nacimiento);
        campanaCoronaEntity2.setVigencia(true);
        campanaCoronaEntity2.setNombre("Sofia");
        campanaCoronaEntity2.setApellidoPaterno("Riquelme");
        campanaCoronaEntity2.setApellidoMaterno("Saavedra");
        campanaCoronaEntity2.setRut("22222222-2");
        campanaCoronaEntity2.setProductoPredeterminado("Mastercard Light");
        campanaCoronaEntity2.setCupoAsignado(400000l);
        campanaCoronaEntity2.setFechaRegistro(LocalDateTime.now());
        campanaCoronaEntity2.setUsuarioRegistro("USR_TMP");
        campanaCoronaEntity2.setEsFuncionario(true);

        List<CampanaCoronaEntity> campanaCoronaEntityList = Arrays.asList(campanaCoronaEntity1, campanaCoronaEntity2);


        return campanaCoronaEntityList;
    }

    private CabeceraCampanaEntity cabeceraCampanaEntity() {
        CabeceraCampanaEntity cabeceraCampanaEntity = new CabeceraCampanaEntity();
        cabeceraCampanaEntity.setCabeceraCampanaId(1l);
        cabeceraCampanaEntity.setNombreArchivo("cargaMasiva.xlsx");
        cabeceraCampanaEntity.setVigencia(true);
        cabeceraCampanaEntity.setCantidadRegistros(1);
        cabeceraCampanaEntity.setCantidadErrores(0);
        cabeceraCampanaEntity.setRegistrosProcesados(1);
        cabeceraCampanaEntity.setFechaTermino(LocalDate.of(2022, 8, 21));
        cabeceraCampanaEntity.setFechaInicio(LocalDate.of(2022, 8, 1));
        cabeceraCampanaEntity.setFechaRegistro(LocalDateTime.now());
        cabeceraCampanaEntity.setUsuarioRegistro("USR_TMP");

        return cabeceraCampanaEntity;
    }

    private CampanaCoronaEntity obtieneCampanaCoronaEntity() {
        CampanaCoronaEntity campanaCoronaEntity = new CampanaCoronaEntity();
        campanaCoronaEntity.setCupoAsignado(300000l);
        campanaCoronaEntity.setEsFuncionario(true);
        campanaCoronaEntity.setProductoPredeterminado("Mastercard Full");

        return campanaCoronaEntity;
    }

    private CampaniaResponseDTO obtieneCampaniaEquifaxResponseDTO() {
        CampaniaResponseDTO campaniaResponseDTO = new CampaniaResponseDTO();
        campaniaResponseDTO.setCampania(true);
        campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.CAMPANIA_EQUIFAX);

        return campaniaResponseDTO;
    }

    private CampaniaResponseDTO obtieneCampaniCoronaResponseDTO() {
        CampaniaResponseDTO campaniaResponseDTO = new CampaniaResponseDTO();
        campaniaResponseDTO.setCampania(true);
        campaniaResponseDTO.setOrigen(EnumOrigenSolcitudAdmision.CAMPANIA_CORONA);

        return campaniaResponseDTO;
    }

    private ParOrigenEntity obtieneParOrigenEntity() {
        ParOrigenEntity parOrigenEntity = new ParOrigenEntity();
        parOrigenEntity.setOrigenId(1l);
        parOrigenEntity.setDescripcion("Varios");
        parOrigenEntity.setVigencia(true);

        return parOrigenEntity;
    }

    private SolicitudAdmisionEntity obtieneSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        return solicitudAdmisionEntity;
    }

    private PreEvaluadosScoringEntity obtienePreEvaluadosScoringEntity() {
        PreEvaluadosScoringEntity preEvaluadosScoringEntity = new PreEvaluadosScoringEntity();
        preEvaluadosScoringEntity.setRut("111111111");
        preEvaluadosScoringEntity.setNombre("Juan");
        preEvaluadosScoringEntity.setApellidoPaterno("Perez");
        preEvaluadosScoringEntity.setApellidoMaterno("Soto");
        preEvaluadosScoringEntity.setEdad(40);
        preEvaluadosScoringEntity.setCupoAsignado(400000l);
        preEvaluadosScoringEntity.setVigencia(true);
        return preEvaluadosScoringEntity;
    }

    private ProspectoEntity obtieneProspectoEntity() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setVigencia(true);
        return prospectoEntity;
    }
}
