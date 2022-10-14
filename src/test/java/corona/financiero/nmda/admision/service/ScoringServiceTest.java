package corona.financiero.nmda.admision.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import corona.financiero.nmda.admision.adapter.EquifaxAdapter;
import corona.financiero.nmda.admision.dto.equifax.*;
import corona.financiero.nmda.admision.entity.ConsultaScoringEntity;
import corona.financiero.nmda.admision.entity.ConsultaScoringPersonaEntity;
import corona.financiero.nmda.admision.entity.ConsultaScoringRegistroMorosidadProtestoEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.*;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @InjectMocks
    private ScoringService scoringService;

    @Mock
    private EquifaxAdapter equifaxAdapter;

    @Mock
    private ConsultaScoringRepository consultaScoringRepository;

    @Mock
    private ConsultaScoringRegistroMorosidadProtestoRepository consultaScoringRegistroMorosidadProtestoRepository;

    @Mock
    private ConsultaScoringPersonaRepository consultaScoringPersonaRepository;

    @Mock
    private ProspectoRepository prospectoRepository;

    @Mock
    private Validaciones validaciones;

    @Mock
    private ResumenMorosidadesProtestosImpagosRepository resumenMorosidadesProtestosImpagosRepository;

    @Test
    void recopilarInformacionScoringLocalOkTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringErrorDatosPersonaTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = null;
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringErrorScoringRegistrosTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = null;
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringEdadMinimaValidacionTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEdadMinimaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringEdadMaximaValidacionTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEdadMaximaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringEdadNulaValidacionTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEdadNulaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringScoreBajoTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringBajoScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringProtestosTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadDocumentoBoletinProtestoImpagos("3");
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringProtestoNuloTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadDocumentoBoletinProtestoImpagos(null);
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringProtestoVacioTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadDocumentoBoletinProtestoImpagos("");
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringCantidadMorosaNuloTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadMorososComercio(null);
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringCantidadMorosaVacioTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadMorososComercio("");
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringCantidadMorosaTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringEntity();

        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = obtieneConsultaScoringPersonaEntity();
        when(consultaScoringPersonaRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringPersonaEntity);

        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = obtieneConsultaScoringMorosidad();
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadMorososComercio("5");
        when(consultaScoringRegistroMorosidadProtestoRepository.findByConsultaScoringEntity(consultaScoringEntity)).thenReturn(consultaScoringRegistroMorosidadProtestoEntity);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringRemotoErrorProspectoTest() {
        String rut = "111111111";

        assertThatExceptionOfType(NoContentException.class)
                .isThrownBy(() -> scoringService.recopilarInformacionScoring(rut))
                .withNoCause();
    }

    @Test
    void recopilarInformacionScoringRemotoConsultaScoringNullTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringVencidaEntity();
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(null);

        ProspectoEntity prospectoEntity = obtieneDatosProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        Map<String, Object> respuestaEquifax = respuestaEquifax();
        when(equifaxAdapter.infoProspectoEquifax(any())).thenReturn(respuestaEquifax);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringRemotoDatosPersonalesNullTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringVencidaEntity();
        List<ConsultaScoringEntity> lista = new ArrayList<>();
        lista.add(consultaScoringEntity);
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(lista);

        ProspectoEntity prospectoEntity = obtieneDatosProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        Map<String, Object> respuestaEquifax = respuestaEquifaxDatosPersonasNulos();
        when(equifaxAdapter.infoProspectoEquifax(any())).thenReturn(respuestaEquifax);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringRemotoErrorServicioTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringVencidaEntity();
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(null);

        ProspectoEntity prospectoEntity = obtieneDatosProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        Map<String, Object> respuestaEquifax = respuestaErrorEquifax();
        when(equifaxAdapter.infoProspectoEquifax(any())).thenReturn(respuestaEquifax);


        scoringService.recopilarInformacionScoring(rut);
    }

    @Test
    void recopilarInformacionScoringRemotoDatosPersonalesFechaInvalidaTest() {
        String rut = "111111111";
        ConsultaScoringEntity consultaScoringEntity = obtieneConsultaScoringVencidaEntity();
        when(consultaScoringRepository.buscarRegistroEquifaxVigente(rut)).thenReturn(null);

        ProspectoEntity prospectoEntity = obtieneDatosProspectoEntity();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rut)).thenReturn(prospectoEntity);

        Map<String, Object> respuestaEquifax = respuestaEquifaxDatosPersonasFechaInvalida();
        when(equifaxAdapter.infoProspectoEquifax(any())).thenReturn(respuestaEquifax);


        scoringService.recopilarInformacionScoring(rut);
    }


    private Map<String, Object> respuestaEquifax() {
        Map<String, Object> map = new HashMap<>();
        map.put("identificacionPersona", obtenerMapRespuestaPersonaServicio());
        map.put("registroMorosidadesYProtestos", obtenerMapRespuestaRegistrosMorososSubEstructuras());
        map.put("resumenMorosidadesYProtestosImpagos", obtenerMapRespuestaResumenMorosidadesEstructuras());
        map.put("score", 800);

        return map;
    }

    private Map<String, Object> respuestaEquifaxDatosPersonasNulos() {
        Map<String, Object> map = new HashMap<>();
        map.put("identificacionPersona", obtenerMapRespuestaPersonaNulosServicio());
        map.put("registroMorosidadesYProtestos", obtenerMapRespuestaRegistrosMorosos());
        map.put("resumenMorosidadesYProtestosImpagos", obtenerMapRespuestaResumenMorosidadesEstructuras());
        map.put("score", 800);

        return map;
    }

    private Map<String, Object> respuestaEquifaxDatosPersonasFechaInvalida() {
        Map<String, Object> map = new HashMap<>();
        map.put("identificacionPersona", obtenerMapRespuestaPersonaFechaInvalidaervicio());
        map.put("registroMorosidadesYProtestos", obtenerMapRespuestaRegistrosMorososFechaBedInvalida());
        map.put("resumenMorosidadesYProtestosImpagos", obtenerMapRespuestaResumenMorosidadesEstructuras());
        map.put("score", 800);

        return map;
    }

    private Map<String, Object> respuestaErrorEquifax() {
        Map<String, Object> errores = new HashMap<>();
        errores.put("tag1", "error");
        Map<String, Object> map = new HashMap<>();
        map.put("errores", errores);

        return map;
    }


    private Map<String, Object> obtenerMapRespuestaPersonaNulosServicio() {
        EquifaxIdentificacionPersonaResponseDTO identificacionPersonaResponseDTO = datosIdentificacionPersonaNulos();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(identificacionPersonaResponseDTO, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaPersonaFechaInvalidaervicio() {
        EquifaxIdentificacionPersonaResponseDTO identificacionPersonaResponseDTO = datosIdentificacionPersonaFechaInvalida();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(identificacionPersonaResponseDTO, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaPersonaServicio() {
        EquifaxIdentificacionPersonaResponseDTO identificacionPersonaResponseDTO = datosIdentificacionPersona();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(identificacionPersonaResponseDTO, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaRegistrosMorosos() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifax = datosMorosos();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(equifax, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaRegistrosMorososFechaBedInvalida() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifax = datosMorososConSubEstructuraBedInvalida();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(equifax, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaRegistrosMorososSubEstructuras() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifax = datosMorososConSubEstructura();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(equifax, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }

    private Map<String, Object> obtenerMapRespuestaResumenMorosidadesEstructuras() {
        EquifaxResumenMorosidadesProtestosImpagosResponseDTO equifax = datosResumenMorosodadesEstructura();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(equifax, new TypeReference<Map<String, Object>>() {
        });

        return map;
    }


    private EquifaxIdentificacionPersonaResponseDTO datosIdentificacionPersona() {
        EquifaxIdentificacionPersonaResponseDTO equifaxResponse = new EquifaxIdentificacionPersonaResponseDTO();
        equifaxResponse.setApellidoMaterno("Perez");
        equifaxResponse.setApellidoMaterno("Soto");
        equifaxResponse.setNombres("Juan");
        equifaxResponse.setEdad("40");
        equifaxResponse.setCodigoOficio("0");
        equifaxResponse.setCodigoProfesion("0");
        equifaxResponse.setEstadoCivil("S");
        equifaxResponse.setFechaNacimiento("1982-02-02");
        equifaxResponse.setNacionalidad("Chilena");
        equifaxResponse.setTipoNacionalidad("C");
        equifaxResponse.setNombres("Juan Perez Soto");

        return equifaxResponse;
    }

    private EquifaxIdentificacionPersonaResponseDTO datosIdentificacionPersonaNulos() {
        EquifaxIdentificacionPersonaResponseDTO equifaxResponse = new EquifaxIdentificacionPersonaResponseDTO();

        equifaxResponse.setCodigoOficio("0");
        equifaxResponse.setCodigoProfesion("0");
        equifaxResponse.setEstadoCivil("S");

        equifaxResponse.setNacionalidad("Chilena");
        equifaxResponse.setTipoNacionalidad("C");

        return equifaxResponse;
    }

    private EquifaxIdentificacionPersonaResponseDTO datosIdentificacionPersonaFechaInvalida() {
        EquifaxIdentificacionPersonaResponseDTO equifaxResponse = new EquifaxIdentificacionPersonaResponseDTO();

        equifaxResponse.setCodigoOficio("0");
        equifaxResponse.setCodigoProfesion("0");
        equifaxResponse.setEstadoCivil("S");
        equifaxResponse.setFechaNacimiento("0000-00-00");

        equifaxResponse.setNacionalidad("Chilena");
        equifaxResponse.setTipoNacionalidad("C");

        return equifaxResponse;
    }

    private EquifaxRegistroMorosidadProtestoResponseDTO datosMorosos() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifaxResponse = new EquifaxRegistroMorosidadProtestoResponseDTO();
        equifaxResponse.setCantidadMorososComercio("0");
        equifaxResponse.setCantidadDocumentosBoletinProtestosEImpagos("0");
        equifaxResponse.setCantidadDocumentosICOM("0");
        equifaxResponse.setCantidadImpagosInformados("0");
        equifaxResponse.setMontoTotalImpago("0");
        equifaxResponse.setCantidadMultasEInfraccionesEnLaboralYPrevisional("0");
        equifaxResponse.setMorosidadesBED(datosBEDNulos());
        equifaxResponse.setMorosidadesBOLAB(datosBOLAB());
        equifaxResponse.setMorosidadesBOLCOM(datosBOLCOMNulos());
        equifaxResponse.setPorMercados(datosMercados());
        equifaxResponse.setMorosidadesICOM(datosICOMNulos());

        return equifaxResponse;
    }

    private EquifaxRegistroMorosidadProtestoResponseDTO datosMorososConSubEstructura() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifaxResponse = new EquifaxRegistroMorosidadProtestoResponseDTO();
        equifaxResponse.setCantidadMorososComercio("0");
        equifaxResponse.setCantidadDocumentosBoletinProtestosEImpagos("0");
        equifaxResponse.setCantidadDocumentosICOM("0");
        equifaxResponse.setCantidadImpagosInformados("0");
        equifaxResponse.setMontoTotalImpago("0");
        equifaxResponse.setCantidadMultasEInfraccionesEnLaboralYPrevisional("0");
        equifaxResponse.setMorosidadesBED(datosBED());
        equifaxResponse.setMorosidadesBOLAB(datosBOLAB());
        equifaxResponse.setMorosidadesBOLCOM(datosBOLCOM());
        equifaxResponse.setPorMercados(datosMercados());
        equifaxResponse.setMorosidadesICOM(datosICOM());

        return equifaxResponse;
    }

    private EquifaxResumenMorosidadesProtestosImpagosResponseDTO datosResumenMorosodadesEstructura() {
        EquifaxResumenMorosidadesProtestosImpagosResponseDTO equifaxResponse = new EquifaxResumenMorosidadesProtestosImpagosResponseDTO();
        equifaxResponse.setCantidadDocumentos6A12Meses("0");
        equifaxResponse.setAccesoCamaraComercio(false);
        equifaxResponse.setCantidadDocumentos12A24Meses("0");

        return equifaxResponse;
    }

    private EquifaxRegistroMorosidadProtestoResponseDTO datosMorososConSubEstructuraBedInvalida() {
        EquifaxRegistroMorosidadProtestoResponseDTO equifaxResponse = new EquifaxRegistroMorosidadProtestoResponseDTO();
        equifaxResponse.setCantidadMorososComercio("0");
        equifaxResponse.setCantidadDocumentosBoletinProtestosEImpagos("0");
        equifaxResponse.setCantidadDocumentosICOM("0");
        equifaxResponse.setCantidadImpagosInformados("0");
        equifaxResponse.setMontoTotalImpago("0");
        equifaxResponse.setCantidadMultasEInfraccionesEnLaboralYPrevisional("0");
        EquifaxMorosidadesBEDResponseDTO equifaxMorosidadesBEDResponseDTO = datosBED();
        equifaxMorosidadesBEDResponseDTO.setJustificacionFecha("0000-00-00");
        equifaxResponse.setMorosidadesBED(equifaxMorosidadesBEDResponseDTO);
        equifaxResponse.setMorosidadesBOLAB(datosBOLAB());
        equifaxResponse.setMorosidadesBOLCOM(datosBOLCOM());
        equifaxResponse.setPorMercados(datosMercados());
        equifaxResponse.setMorosidadesICOM(datosICOM());

        return equifaxResponse;
    }

    private EquifaxMorosidadesIcomResponseDTO datosICOMNulos() {
        EquifaxMorosidadesIcomResponseDTO response = new EquifaxMorosidadesIcomResponseDTO();

        return response;
    }

    private EquifaxMorosidadesIcomResponseDTO datosICOM() {
        EquifaxMorosidadesIcomResponseDTO response = new EquifaxMorosidadesIcomResponseDTO();
        response.setFechaVencimiento("2022-06-20");
        return response;
    }

    private EquifaxMercadosReponseDTO datosMercados() {
        EquifaxMercadosReponseDTO response = new EquifaxMercadosReponseDTO();
        return response;
    }

    private EquifaxMorosidadesBolcomResponseDTO datosBOLCOMNulos() {
        EquifaxMorosidadesBolcomResponseDTO response = new EquifaxMorosidadesBolcomResponseDTO();
        return response;
    }

    private EquifaxMorosidadesBolcomResponseDTO datosBOLCOM() {
        EquifaxMorosidadesBolcomResponseDTO response = new EquifaxMorosidadesBolcomResponseDTO();
        response.setFechaIngreso("2022-01-01");
        response.setFechaVencimiento("2022-06-20");
        response.setJustificacionFecha("2022-06-20");
        return response;
    }

    private EquifaxMorosidadesBolabResponseDTO datosBOLAB() {
        EquifaxMorosidadesBolabResponseDTO response = new EquifaxMorosidadesBolabResponseDTO();

        return response;
    }

    private EquifaxMorosidadesBEDResponseDTO datosBEDNulos() {
        EquifaxMorosidadesBEDResponseDTO response = new EquifaxMorosidadesBEDResponseDTO();

        return response;
    }

    private EquifaxMorosidadesBEDResponseDTO datosBED() {
        EquifaxMorosidadesBEDResponseDTO response = new EquifaxMorosidadesBEDResponseDTO();
        response.setFechaIngresoEFX("2022-06-09");
        response.setFechaVencimiento("2022-06-20");
        response.setJustificacionFecha("2022-06-20");

        return response;
    }

    private EquifaxRequestDTO requestEquifax() {
        EquifaxRequestDTO equifaxRequestDTO = new EquifaxRequestDTO();
        equifaxRequestDTO.setRut("11111111");

        return equifaxRequestDTO;
    }

    private ProspectoEntity obtieneDatosProspectoEntity() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setMovil(987654321);
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setRut("111111111");
        prospectoEntity.setEmail("a@b.cl");
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setVigencia(true);
        prospectoEntity.setFechaIngreso(LocalDateTime.now());


        return prospectoEntity;
    }


    private ConsultaScoringEntity obtieneConsultaScoringEntity() {

        ConsultaScoringEntity consultaScoringEntity = new ConsultaScoringEntity();
        consultaScoringEntity.setScore(700l);
        consultaScoringEntity.setFechaConsulta(LocalDate.now());
        consultaScoringEntity.setVigencia(true);
        consultaScoringEntity.setUsuarioRegistro("USR_TMP");


        return consultaScoringEntity;

    }

    private ConsultaScoringEntity obtieneConsultaScoringVencidaEntity() {

        ConsultaScoringEntity consultaScoringEntity = new ConsultaScoringEntity();
        consultaScoringEntity.setScore(501l);
        LocalDate ahora = LocalDate.now();
        LocalDate vencida = ahora.minusMonths(2);
        consultaScoringEntity.setFechaConsulta(vencida);
        consultaScoringEntity.setVigencia(true);
        consultaScoringEntity.setUsuarioRegistro("USR_TMP");


        return consultaScoringEntity;

    }

    private ConsultaScoringEntity obtieneConsultaScoringBajoScoringEntity() {

        ConsultaScoringEntity consultaScoringEntity = new ConsultaScoringEntity();
        consultaScoringEntity.setScore(0l);
        consultaScoringEntity.setFechaConsulta(LocalDate.now());
        consultaScoringEntity.setVigencia(true);
        consultaScoringEntity.setUsuarioRegistro("USR_TMP");


        return consultaScoringEntity;

    }

    private ConsultaScoringPersonaEntity obtieneConsultaScoringPersonaEntity() {

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = new ConsultaScoringPersonaEntity();
        consultaScoringPersonaEntity.setConsultaScoringEntity(obtieneConsultaScoringEntity());
        consultaScoringPersonaEntity.setNombreCompleto("Juan Perez Soto");
        consultaScoringPersonaEntity.setNombres("Juan");
        consultaScoringPersonaEntity.setApellidoPaterno("Perez");
        consultaScoringPersonaEntity.setApellidoMaterno("Soto");
        consultaScoringPersonaEntity.setEdad("37");
        LocalDate date = LocalDate.now();
        date.minusYears(30);
        consultaScoringPersonaEntity.setFechaNacimiento(date);
        return consultaScoringPersonaEntity;

    }

    private ConsultaScoringPersonaEntity obtieneConsultaScoringPersonaEdadMinimaEntity() {

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = new ConsultaScoringPersonaEntity();
        consultaScoringPersonaEntity.setConsultaScoringEntity(obtieneConsultaScoringEntity());
        consultaScoringPersonaEntity.setNombreCompleto("Juan Perez Soto");
        consultaScoringPersonaEntity.setNombres("Juan");
        consultaScoringPersonaEntity.setApellidoPaterno("Perez");
        consultaScoringPersonaEntity.setApellidoMaterno("Soto");
        consultaScoringPersonaEntity.setEdad("20");
        LocalDate date = LocalDate.now();
        date.minusYears(20);
        consultaScoringPersonaEntity.setFechaNacimiento(date);
        return consultaScoringPersonaEntity;

    }

    private ConsultaScoringPersonaEntity obtieneConsultaScoringPersonaEdadNulaEntity() {

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = new ConsultaScoringPersonaEntity();
        consultaScoringPersonaEntity.setConsultaScoringEntity(obtieneConsultaScoringEntity());
        consultaScoringPersonaEntity.setNombreCompleto("Juan Perez Soto");
        consultaScoringPersonaEntity.setNombres("Juan");
        consultaScoringPersonaEntity.setApellidoPaterno("Perez");
        consultaScoringPersonaEntity.setApellidoMaterno("Soto");
        return consultaScoringPersonaEntity;

    }

    private ConsultaScoringPersonaEntity obtieneConsultaScoringPersonaEdadMaximaEntity() {

        ConsultaScoringPersonaEntity consultaScoringPersonaEntity = new ConsultaScoringPersonaEntity();
        consultaScoringPersonaEntity.setConsultaScoringEntity(obtieneConsultaScoringEntity());
        consultaScoringPersonaEntity.setNombreCompleto("Juan Perez Soto");
        consultaScoringPersonaEntity.setNombres("Juan");
        consultaScoringPersonaEntity.setApellidoPaterno("Perez");
        consultaScoringPersonaEntity.setApellidoMaterno("Soto");
        consultaScoringPersonaEntity.setEdad("87");
        LocalDate date = LocalDate.now();
        date.plusYears(80);
        consultaScoringPersonaEntity.setFechaNacimiento(date);
        return consultaScoringPersonaEntity;

    }

    private ConsultaScoringRegistroMorosidadProtestoEntity obtieneConsultaScoringMorosidad() {
        ConsultaScoringRegistroMorosidadProtestoEntity consultaScoringRegistroMorosidadProtestoEntity = new ConsultaScoringRegistroMorosidadProtestoEntity();
        consultaScoringRegistroMorosidadProtestoEntity.setConsultaScoringEntity(obtieneConsultaScoringEntity());
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadDocumentoBoletinProtestoImpagos("0");
        consultaScoringRegistroMorosidadProtestoEntity.setCantidadMorososComercio("0");

        return consultaScoringRegistroMorosidadProtestoEntity;
    }
}
