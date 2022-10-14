package corona.financiero.nmda.admision.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import corona.financiero.nmda.admision.adapter.CotizacionAdapter;
import corona.financiero.nmda.admision.dto.cotizacion.CotizacionesRequestDTO;
import corona.financiero.nmda.admision.entity.ParRespuestaCotizacionPrevisionalEntity;
import corona.financiero.nmda.admision.entity.ProspectoEntity;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.repository.CotizacionPrevisionalDetalleRepository;
import corona.financiero.nmda.admision.repository.CotizacionPrevisionalRepository;
import corona.financiero.nmda.admision.repository.ParRespuestaCotizacionPrevisionalRepository;
import corona.financiero.nmda.admision.repository.ProspectoRepository;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CotizacionesSeviceTest {

    @InjectMocks
    private CotizacionService cotizacionService;

    @Mock
    private CotizacionAdapter cotizacionAdapter;

    @Mock
    private ProspectoRepository prospectoRepository;

    @Mock
    private Validaciones validaciones;

    @Mock
    private ParRespuestaCotizacionPrevisionalRepository parRespuestaCotizacionPrevisionalRepository;

    @Mock
    private CotizacionPrevisionalRepository cotizacionPrevisionalRepository;

    @Mock
    private CotizacionPrevisionalDetalleRepository cotizacionPrevisionalDetalleRepository;

    @Test
    void cotizacionesTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        Map map = obtieneCotizaciones();
        when(cotizacionAdapter.consultarCotizaciones(any())).thenReturn(map);

        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntity = parRespuesta();
        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptional = Optional.of(parRespuestaCotizacionPrevisionalEntity);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9000l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptional);

        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntityAut = parRespuesta();
        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptionalAut = Optional.of(parRespuestaCotizacionPrevisionalEntityAut);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9050l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptionalAut);


        cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion);

    }

    @Test
    void cotizacionesProspectoErrorTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(null);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion))
                .withNoCause();
    }

    @Test
    void cotizacionesMapErrorTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        when(cotizacionAdapter.consultarCotizaciones(any())).thenReturn(null);

        cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion);
    }

    @Test
    void cotizacionesCodigoControlErrorTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        Map map = obtieneCotizaciones();
        when(cotizacionAdapter.consultarCotizaciones(any())).thenReturn(map);

        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptional = Optional.ofNullable(null);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9000l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptional);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion))
                .withNoCause();
    }

    @Test
    void cotizacionesCodigoAutErrorTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        Map map = obtieneCotizaciones();
        when(cotizacionAdapter.consultarCotizaciones(any())).thenReturn(map);

        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntity = parRespuesta();
        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptional = Optional.of(parRespuestaCotizacionPrevisionalEntity);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9000l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptional);

        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptionalAut = Optional.ofNullable(null);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9050l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptionalAut);


        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion))
                .withNoCause();

    }

    @Test
    void cotizacionesCodigoDetalleErrorTest() {

        CotizacionesRequestDTO cotizacionesRequestDTO = cotizacionesRequest();
        String transaccion = "12432FGjjkgh34";

        String rutFormateado = "111111111";

        when(validaciones.formateaRutHaciaBD(cotizacionesRequestDTO.getRut())).thenReturn(rutFormateado);

        ProspectoEntity prospectoEntity = datosMinimosProspecto();
        when(prospectoRepository.findByRutAndVigenciaIsTrue(rutFormateado)).thenReturn(prospectoEntity);

        Map map = obtieneCotizaciones();
        when(cotizacionAdapter.consultarCotizaciones(any())).thenReturn(map);

        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntity = parRespuesta();
        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptional = Optional.of(parRespuestaCotizacionPrevisionalEntity);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9000l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptional);

        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntityAut = parRespuesta();
        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptionalAut = Optional.of(parRespuestaCotizacionPrevisionalEntityAut);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9050l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptionalAut);

        Optional<ParRespuestaCotizacionPrevisionalEntity> parRespuestaCotizacionPrevisionalEntityOptionalDetalle = Optional.ofNullable(null);
        when(parRespuestaCotizacionPrevisionalRepository.findById(9050l)).thenReturn(parRespuestaCotizacionPrevisionalEntityOptionalDetalle);


        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> cotizacionService.obtenerCotizaciones(cotizacionesRequestDTO, transaccion))
                .withNoCause();

    }

    private ParRespuestaCotizacionPrevisionalEntity parRespuesta() {
        ParRespuestaCotizacionPrevisionalEntity parRespuestaCotizacionPrevisionalEntity = new ParRespuestaCotizacionPrevisionalEntity();
        parRespuestaCotizacionPrevisionalEntity.setCodigoRespuestaId(1l);
        parRespuestaCotizacionPrevisionalEntity.setDescripcion("COD");
        parRespuestaCotizacionPrevisionalEntity.setVigencia(true);

        return parRespuestaCotizacionPrevisionalEntity;

    }

    private ProspectoEntity datosMinimosProspecto() {
        ProspectoEntity prospectoEntity = new ProspectoEntity();
        prospectoEntity.setProspectoId(1l);
        prospectoEntity.setNombres("Juan");
        prospectoEntity.setApellidoPaterno("Perez");
        prospectoEntity.setApellidoMaterno("Soto");
        prospectoEntity.setRut("111111111");

        return prospectoEntity;
    }

    private CotizacionesRequestDTO cotizacionesRequest() {
        CotizacionesRequestDTO cotizacionesRequestDTO = new CotizacionesRequestDTO();
        cotizacionesRequestDTO.setCantidad(12);
        cotizacionesRequestDTO.setRut("11111111-1");


        return cotizacionesRequestDTO;
    }

    private Map obtieneCotizaciones() {
        String cotizaciones = "{\n" +
                "    \"version\": \"1.0\",\n" +
                "    \"encoding\": \"ISO-8859-1\",\n" +
                "    \"respuesta\": {\n" +
                "        \"control\": {\n" +
                "            \"@codigo\": \"9000\"\n" +
                "        },\n" +
                "        \"respuestaservicio\": [{\n" +
                "                \"@tipo\": \"AUT\",\n" +
                "                \"control\": {\n" +
                "                    \"@codigo\": \"9050\"\n" +
                "                },\n" +
                "                \"respuestaaut\": {\n" +
                "                    \"llave\": \"amlu4ksxISBlDY16RCGNFsfW\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"@tipo\": \"CCX\",\n" +
                "                \"control\": {\n" +
                "                    \"@codigo\": \"9050\"\n" +
                "                },\n" +
                "                \"respuestaccx\": {\n" +
                "                    \"respuestaccx_encabezado\": {\n" +
                "                        \"@rut\": \"15902664-7\",\n" +
                "                        \"@nombres\": \"NICOLAS ANTONIO\",\n" +
                "                        \"@apellidopaterno\": \"ARANDA\",\n" +
                "                        \"@apellidomaterno\": \"MORALES\"\n" +
                "                    },\n" +
                "                    \"respuestaccx_detalle\": [{\n" +
                "                            \"@mes\": \"052022\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/05/2022\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"042022\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/04/2022\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"032022\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/03/2022\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"022022\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/02/2022\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"012022\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/01/2022\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"082021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/08/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"072021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/07/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"062021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/06/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"052021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/05/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"042021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/04/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"032021\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/03/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"@mes\": \"020201\",\n" +
                "                            \"@remuneracionimponible\": \"1200000\",\n" +
                "                            \"@monto\": \"100000\",\n" +
                "                            \"@fechapago\": \"02/02/2021\",\n" +
                "                            \"@tipomovimiento\": \"COTIZACION OBLIGATORIA\",\n" +
                "                            \"@rutempleador\": \"96.523.710-0\",\n" +
                "                            \"@afp\": \"MODELO\"\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"respuestaccx_legal\": {\n" +
                "                        \"@folio\": \"5949763\",\n" +
                "                        \"@firma\": \"43908C931967D19FA14BA5DF708370A5\",\n" +
                "                        \"@fecha\": \"27/07/2022\",\n" +
                "                        \"@hora\": \"17:09:28:848\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(cotizaciones, Map.class);
        } catch (Exception e) {

        }


        return map;
    }
}
