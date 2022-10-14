package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ExportSolicitudAdmisionDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.CampanaCoronaRepository;
import corona.financiero.nmda.admision.repository.MotivoCartaRechazoRepository;
import corona.financiero.nmda.admision.repository.SolicitudAdmisionFiltrosRepositoryImpl;
import corona.financiero.nmda.admision.util.Validaciones;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
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
class ExportComponentTest {

    @Mock
    private SolicitudAdmisionFiltrosRepositoryImpl solicitudAdmisionFiltrosRepository;

    @Mock
    private CampanaCoronaRepository campanaCoronaRepository;

    @Mock
    private Validaciones validaciones;

    @InjectMocks
    private ExportComponent exportComponent;

    @Mock
    private MotivoCartaRechazoRepository motivoCartaRechazoRepository;


    @Test
    void exportarSolicitudAdmisionTest() {

        LocalDate hasta = LocalDate.now();
        LocalDate desde = hasta.minusDays(90);

        ExportSolicitudAdmisionDTO exportSolicitudAdmisionDTO = new ExportSolicitudAdmisionDTO();
        exportSolicitudAdmisionDTO.setSolicitudId(1l);
        exportSolicitudAdmisionDTO.setMovil(912345678);
        exportSolicitudAdmisionDTO.setEdad(35);
        exportSolicitudAdmisionDTO.setNombreEjecutivo("Bart Simpsons");
        exportSolicitudAdmisionDTO.setRutEjecutivo("999999999");
        exportSolicitudAdmisionDTO.setEmailCliente("a@b.cl");
        exportSolicitudAdmisionDTO.setEstadoSolicitud("Iniciado");
        exportSolicitudAdmisionDTO.setFechaSolicitud(LocalDate.now());
        exportSolicitudAdmisionDTO.setApellidoMaternoCliente("Soto");
        exportSolicitudAdmisionDTO.setApellidoPaternoCliente("Perez");
        exportSolicitudAdmisionDTO.setNombreCliente("Juan");
        exportSolicitudAdmisionDTO.setRutCliente("111111111");
        List<ExportSolicitudAdmisionDTO> lista = new ArrayList<>();
        lista.add(exportSolicitudAdmisionDTO);


        when(solicitudAdmisionFiltrosRepository.findAllByNativoExportar(desde, hasta)).thenReturn(lista);

        exportComponent.exportarSolicitudAdmision();
    }

    @Test
    void exportarolicitudAdmisionSinEdadSinMovilTest() {

        LocalDate hasta = LocalDate.now();
        LocalDate desde = hasta.minusDays(90);


        ExportSolicitudAdmisionDTO exportSolicitudAdmisionDTO = new ExportSolicitudAdmisionDTO();
        exportSolicitudAdmisionDTO.setSolicitudId(1l);

        exportSolicitudAdmisionDTO.setNombreEjecutivo("Bart Simpsons");
        exportSolicitudAdmisionDTO.setRutEjecutivo("999999999");
        exportSolicitudAdmisionDTO.setEmailCliente("a@b.cl");
        exportSolicitudAdmisionDTO.setEstadoSolicitud("Iniciado");
        exportSolicitudAdmisionDTO.setFechaSolicitud(LocalDate.now());
        exportSolicitudAdmisionDTO.setApellidoMaternoCliente("Soto");
        exportSolicitudAdmisionDTO.setApellidoPaternoCliente("Perez");
        exportSolicitudAdmisionDTO.setNombreCliente("Juan");
        exportSolicitudAdmisionDTO.setRutCliente("111111111");
        List<ExportSolicitudAdmisionDTO> lista = new ArrayList<>();
        lista.add(exportSolicitudAdmisionDTO);


        when(solicitudAdmisionFiltrosRepository.findAllByNativoExportar(desde, hasta)).thenReturn(lista);

        exportComponent.exportarSolicitudAdmision();

    }

    @Test
    void exportarRegistrosCampaniaCoronaTest() {
        long cabeceraId = 1l;
        CampanaCoronaEntity campanaCoronaEntity = campanaCoronaEntity();
        List<CampanaCoronaEntity> lista = Arrays.asList(campanaCoronaEntity);
        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(cabeceraId)).thenReturn(lista);

        when(validaciones.formateaRutHaciaFront(campanaCoronaEntity().getRut())).thenReturn("11111111-1");

        exportComponent.exportarRegistrosCampaniasCorona(cabeceraId);
    }

    @Test
    void exportarRegistrosCampaniaCoronaRequestErrorTest() {
        long cabeceraId = -11l;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> exportComponent.exportarRegistrosCampaniasCorona(cabeceraId))
                .withNoCause();

    }

    @Test
    void exportarRegistrosCampaniaCoronaNotFoundNullTest() {
        long cabeceraId = 1l;

        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(cabeceraId)).thenReturn(null);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> exportComponent.exportarRegistrosCampaniasCorona(cabeceraId))
                .withNoCause();

    }

    @Test
    void exportarRegistrosCampaniaCoronaNotFoundEmptyTest() {
        long cabeceraId = 1l;

        List<CampanaCoronaEntity> lista = new ArrayList<>();

        when(campanaCoronaRepository.findAllByCabeceraCampanaEntityCabeceraCampanaId(cabeceraId)).thenReturn(lista);

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> exportComponent.exportarRegistrosCampaniasCorona(cabeceraId))
                .withNoCause();

    }

    //@Test
    void exportarCartaRechazoTest() throws IOException, InvalidFormatException {
        String rut = "11111111-1";
        long solicitudId = 1;

        doNothing().when(validaciones).validacionGeneralRut(rut);

        String rutFormateado = "111111111";
        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);

        MotivoCartaRechazoEntity motivoCartaRechazoEntity = motivoCartaRechazoEntity();
        Optional<MotivoCartaRechazoEntity> motivoCartaRechazoEntityOptional = Optional.of(motivoCartaRechazoEntity);

        when(motivoCartaRechazoRepository.findBySolicitudAdmisionEntitySolicitudIdAndSolicitudAdmisionEntityProspectoEntityRut(solicitudId, rutFormateado)).thenReturn(motivoCartaRechazoEntityOptional);


        exportComponent.exportarCartaRechazo(rut,solicitudId);
    }

    private MotivoCartaRechazoEntity motivoCartaRechazoEntity(){
        MotivoCartaRechazoEntity motivoCartaRechazoEntity = new MotivoCartaRechazoEntity();
        motivoCartaRechazoEntity.setSolicitudAdmisionEntity(solicitudAdmisionEntity());
        motivoCartaRechazoEntity.setDescripcion("Descripcion motivo");
        motivoCartaRechazoEntity.setUsuarioRegistro("USR_TMP");
        motivoCartaRechazoEntity.setFechaRegistro(LocalDateTime.now());
        motivoCartaRechazoEntity.setMotivoCartarechazoId(1l);
        return motivoCartaRechazoEntity;
    }

    private SolicitudAdmisionEntity solicitudAdmisionEntity(){
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1l);
        solicitudAdmisionEntity.setVigencia(true);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        solicitudAdmisionEntity.setUsuarioRegistro("USR_TMP");
        solicitudAdmisionEntity.setFechaSolicitud(LocalDate.now());
        solicitudAdmisionEntity.setProspectoEntity(prospectoEntity());
        return solicitudAdmisionEntity;
    }

    private ProspectoEntity prospectoEntity(){
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setVigencia(true);
        prospectoEntity.setRut("111111111");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setApellidoMaterno("Soto");
        return prospectoEntity;
    }

    private CampanaCoronaEntity campanaCoronaEntity() {
        CampanaCoronaEntity c = new CampanaCoronaEntity();
        c.setCabeceraCampanaEntity(cabeceraCampanaEntity());
        c.setRut("111111111");
        c.setNombre("Juan");
        c.setApellidoPaterno("Perez");
        c.setApellidoMaterno("Soto");
        c.setEsFuncionario(false);
        c.setFechaNacimiento(LocalDate.now());
        c.setVigencia(true);
        c.setUsuarioRegistro("USR_TMP");
        c.setCupoAsignado(250000l);
        c.setProductoPredeterminado("Mastercard Light");


        return c;
    }

    private CabeceraCampanaEntity cabeceraCampanaEntity() {
        CabeceraCampanaEntity c = new CabeceraCampanaEntity();
        c.setVigencia(true);
        c.setRegistrosProcesados(4);
        c.setNombreArchivo("cargaMasiva.xlsx");
        c.setFechaRegistro(LocalDateTime.now());
        c.setUsuarioRegistro("USR_TMP");
        c.setFechaInicio(LocalDate.now());
        c.setFechaTermino(LocalDate.now().plusDays(20));

        return c;
    }

}
