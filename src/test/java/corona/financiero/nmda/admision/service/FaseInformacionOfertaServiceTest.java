package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.OfertaAceptadaDTO;
import corona.financiero.nmda.admision.dto.OfertaRechazadaDTO;
import corona.financiero.nmda.admision.entity.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Constantes;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FaseInformacionOfertaServiceTest {
    @Mock
    private TarjetaRepository tarjetaRepository;

    @Mock
    private ParDiaPagoRepository parDiaPagoRepository;

    @InjectMocks
    private FaseInformacionOfertaService faseInformacionOfertaService;

    @Mock
    private Validaciones validaciones;

    @Mock
    private SolicitudAdmisionRepository solicitudAdmisionRepository;

    @Mock
    private ParMotivoRechazoRepository parMotivoRechazoRepository;

    @Mock
    private RechazoSolicitudRepository rechazoSolicitudRepository;

    @Mock
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    @Mock
    private EvaluacionProductoRepository evaluacionProductoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AdmisionFaseService admisionFaseService;

    private static final int ID_MOTIVO_RECHAZO_OTRO = 9;


    @BeforeEach
    public void initEach() {
    }


    @Test
    void rechazarOferta() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTO();

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.of(solicitudAdmisionEntity);
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        ParMotivoRechazoEntity parMotivoRechazoEntity = datosParMotivoRechazoEntity();
        Optional<ParMotivoRechazoEntity> parMotivoRechazoEntityOptional = Optional.of(parMotivoRechazoEntity);
        when(parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo())).thenReturn(parMotivoRechazoEntityOptional);

        ParEstadoSolicitudEntity parEstadoSolicitudEntity = datosEstadoSolicitudEntity();
        Optional<ParEstadoSolicitudEntity> parEstadoSolicitudEntityOptional = Optional.of(parEstadoSolicitudEntity);
        when(parEstadoSolicitudRepository.findById(any())).thenReturn(parEstadoSolicitudEntityOptional);

        faseInformacionOfertaService.rechazarOferta(request);

    }

    @Test
    void rechazarOfertaCuandoMotivoMotivoRechazoOtroEsNull() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setOtroMotivo(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.of(solicitudAdmisionEntity);
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        ParMotivoRechazoEntity parMotivoRechazoEntity = datosParMotivoRechazoEntity();
        parMotivoRechazoEntity.setMotivoRechazoId(ID_MOTIVO_RECHAZO_OTRO);

        Optional<ParMotivoRechazoEntity> parMotivoRechazoEntityOptional = Optional.of(parMotivoRechazoEntity);
        when(parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo())).thenReturn(parMotivoRechazoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoMotivoMotivoRechazoOtroEsVacio() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setOtroMotivo("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.of(solicitudAdmisionEntity);
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        ParMotivoRechazoEntity parMotivoRechazoEntity = datosParMotivoRechazoEntity();
        parMotivoRechazoEntity.setMotivoRechazoId(ID_MOTIVO_RECHAZO_OTRO);

        Optional<ParMotivoRechazoEntity> parMotivoRechazoEntityOptional = Optional.of(parMotivoRechazoEntity);
        when(parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo())).thenReturn(parMotivoRechazoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoExisteOtroMotivoDeRechazo() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.of(solicitudAdmisionEntity);
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        ParMotivoRechazoEntity parMotivoRechazoEntity = datosParMotivoRechazoEntity();
        parMotivoRechazoEntity.setMotivoRechazoId(ID_MOTIVO_RECHAZO_OTRO);

        Optional<ParMotivoRechazoEntity> parMotivoRechazoEntityOptional = Optional.of(parMotivoRechazoEntity);
        when(parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo())).thenReturn(parMotivoRechazoEntityOptional);

        ParEstadoSolicitudEntity parEstadoSolicitudEntity = datosEstadoSolicitudEntity();
        Optional<ParEstadoSolicitudEntity> parEstadoSolicitudEntityOptional = Optional.of(parEstadoSolicitudEntity);
        when(parEstadoSolicitudRepository.findById(any())).thenReturn(parEstadoSolicitudEntityOptional);

        faseInformacionOfertaService.rechazarOferta(request);

    }

    @Test
    void rechazarOfertaCuandoMotivoDeRechazoNoEsValido() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setOtroMotivo("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        SolicitudAdmisionEntity solicitudAdmisionEntity = datosSolicitudAdmisionEntity();
        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.of(solicitudAdmisionEntity);
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        Optional<ParMotivoRechazoEntity> parMotivoRechazoEntityOptional = Optional.empty();
        when(parMotivoRechazoRepository.recuperarMotivoRechazoByMotivoRechazoId(request.getIdMotivoRechazo())).thenReturn(parMotivoRechazoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoSolicitudAdmisionNoEsValida() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setOtroMotivo("");
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<SolicitudAdmisionEntity> solicitudAdmisionEntityOptional = Optional.empty();
        when(solicitudAdmisionRepository.recuperarSolicitudAdmisionVigenteBySolicitudIdAndRutProspecto(request.getSolicitudId(), rutFormateado)).thenReturn(solicitudAdmisionEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoIdMotivoRechazoEsNull() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setIdMotivoRechazo(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoIdMotivoRechazoNoEsValido() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setIdMotivoRechazo(-1l);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoIdSolicitudEsNull() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setSolicitudId(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoIdSolicitudNoEsValido() {
        OfertaRechazadaDTO request = datosOfertaRechazadaDTOConMotivoRechazoOtro();
        request.setSolicitudId(-1l);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void rechazarOfertaCuandoRequestEsNull() {
        OfertaRechazadaDTO request = null;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.rechazarOferta(request))
                .withNoCause();

    }

    @Test
    void aceptarOferta() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParDiaPagoEntity parDiaPagoEntity = datosDiaPagoEntity();
        Optional<ParDiaPagoEntity> parDiaPagoEntityOptional = Optional.of(parDiaPagoEntity);
        when(parDiaPagoRepository.recuperarDiaPagoVigenteById(request.getDiaPagoId())).thenReturn(parDiaPagoEntityOptional);

        EvaluacionProductoEntity evaluacionProductoEntity = datosEvaluacionProductoEntity();
        Optional<EvaluacionProductoEntity> evaluacionProductoEntityOptional = Optional.of(evaluacionProductoEntity);
        when(evaluacionProductoRepository.recuperarEvaluacionProductoByEvaluacionProductoIdAndRut(request.getEvaluacionProductoId(), rutFormateado)).thenReturn(evaluacionProductoEntityOptional);

        doNothing().when(admisionFaseService).actualizarFaseSolicitud(evaluacionProductoEntity.getSolicitudAdmisionEntity(), Constantes.FASE_INFORMACION_OFERTA);

        faseInformacionOfertaService.aceptarOferta(request);

    }


    @Test
    void aceptarOfertaCuandoNoExisteEvaluacionProductoEntity() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        ParDiaPagoEntity parDiaPagoEntity = datosDiaPagoEntity();
        Optional<ParDiaPagoEntity> parDiaPagoEntityOptional = Optional.of(parDiaPagoEntity);
        when(parDiaPagoRepository.recuperarDiaPagoVigenteById(request.getDiaPagoId())).thenReturn(parDiaPagoEntityOptional);

        Optional<EvaluacionProductoEntity> evaluacionProductoEntityOptional = Optional.empty();
        when(evaluacionProductoRepository.recuperarEvaluacionProductoByEvaluacionProductoIdAndRut(request.getEvaluacionProductoId(), rutFormateado)).thenReturn(evaluacionProductoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();

    }

    @Test
    void aceptarOfertaCuandoNoExisteDiaPagoEntity() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();

        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(request.getRut())).thenReturn(rutFormateado);

        Optional<ParDiaPagoEntity> parDiaPagoEntityOptional = Optional.empty();
        when(parDiaPagoRepository.recuperarDiaPagoVigenteById(request.getDiaPagoId())).thenReturn(parDiaPagoEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();

    }

    @Test
    void aceptarOfertaCuandoIdEvaluacionProductoNoEsValido() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();
        request.setEvaluacionProductoId(-1l);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();

    }

    @Test
    void aceptarOfertaCuandoIdEvaluacionProductoEsNull() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();
        request.setEvaluacionProductoId(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();
    }

    @Test
    void aceptarOfertaCuandoIdDiaPagoNoEsValido() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();
        request.setDiaPagoId(-1l);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();

    }

    @Test
    void aceptarOfertaCuandoIdDiaPagoEsNull() {
        OfertaAceptadaDTO request = datosOfertaAceptadaDTO();
        request.setDiaPagoId(null);
        doNothing().when(validaciones).validacionGeneralRut(request.getRut());

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();
    }

    @Test
    void aceptarOfertaCuandoRequestEsNull() {
        OfertaAceptadaDTO request = null;

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> faseInformacionOfertaService.aceptarOferta(request))
                .withNoCause();

    }

    private OfertaRechazadaDTO datosOfertaRechazadaDTO() {
        OfertaRechazadaDTO ofertaRechazadaDTO = new OfertaRechazadaDTO();
        ofertaRechazadaDTO.setIdMotivoRechazo(1l);
        ofertaRechazadaDTO.setOtroMotivo("otro motivo");
        ofertaRechazadaDTO.setRut("11111111-1");
        ofertaRechazadaDTO.setSolicitudId(1l);

        return ofertaRechazadaDTO;
    }

    private OfertaRechazadaDTO datosOfertaRechazadaDTOConMotivoRechazoOtro() {
        OfertaRechazadaDTO ofertaRechazadaDTO = new OfertaRechazadaDTO();
        ofertaRechazadaDTO.setIdMotivoRechazo(Long.valueOf(ID_MOTIVO_RECHAZO_OTRO));
        ofertaRechazadaDTO.setOtroMotivo("otro motivo");
        ofertaRechazadaDTO.setRut("11111111-1");
        ofertaRechazadaDTO.setSolicitudId(1l);

        return ofertaRechazadaDTO;
    }

    private ParDiaPagoEntity datosDiaPagoEntity() {
        ParDiaPagoEntity parDiaPagoEntity = new ParDiaPagoEntity();
        parDiaPagoEntity.setDiaPagoId(1);
        parDiaPagoEntity.setVigencia(true);
        return parDiaPagoEntity;
    }

    private SolicitudAdmisionEntity datosSolicitudAdmisionEntity() {
        SolicitudAdmisionEntity solicitudAdmisionEntity = new SolicitudAdmisionEntity();
        solicitudAdmisionEntity.setSolicitudId(1);
        solicitudAdmisionEntity.setFechaRegistro(LocalDateTime.now());
        solicitudAdmisionEntity.setParEstadoSolicitudEntity(datosEstadoSolicitudEntity());
        solicitudAdmisionEntity.setProspectoEntity(datosProspectoEntity());
        return solicitudAdmisionEntity;
    }

    private ParEstadoSolicitudEntity datosEstadoSolicitudEntity() {
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

        return parTipoProductoEntity;
    }

    private ParMotivoRechazoEntity datosParMotivoRechazoEntity() {
        ParMotivoRechazoEntity parMotivoRechazoEntity = new ParMotivoRechazoEntity();
        parMotivoRechazoEntity.setDescripcion("Motivo1");
        parMotivoRechazoEntity.setVigencia(true);
        parMotivoRechazoEntity.setMotivoRechazoId(1);
        return parMotivoRechazoEntity;
    }

    private OfertaAceptadaDTO datosOfertaAceptadaDTO() {
        OfertaAceptadaDTO ofertaAceptadaDTO = new OfertaAceptadaDTO();
        ofertaAceptadaDTO.setDiaPagoId(1l);
        ofertaAceptadaDTO.setRut("11111111-1");
        ofertaAceptadaDTO.setEvaluacionProductoId(1l);

        return ofertaAceptadaDTO;
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

    private ProspectoEntity datosProspectoEntity() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setEmail("juanperez@gmail.com");
        prospectoEntity.setRut("111111111");
        prospectoEntity.setVigencia(true);

        prospectoEntity.setMovil(Long.valueOf(312323213));
        prospectoEntity.setFechaIngreso(LocalDateTime.now());
        prospectoEntity.setUsuarioIngreso("USR_TMP");

        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");

        return prospectoEntity;
    }


    private ClienteEntity datosClienteEntity(){
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setProspectoEntity(datosProspectoEntity());
        clienteEntity.setRut("111111111");
        clienteEntity.setVigencia(true);
        clienteEntity.setNombre("Juan");
        clienteEntity.setApellidoPaterno("Perez");


        return clienteEntity;
    }

    private TarjetaEntity datosTarjetaEntity(){
        TarjetaEntity tarjetaEntity = new TarjetaEntity();
        tarjetaEntity.setTarjetaId(1l);

        return tarjetaEntity;
    }
}
