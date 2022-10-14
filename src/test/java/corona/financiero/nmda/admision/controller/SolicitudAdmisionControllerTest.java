package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.ExportComponent;
import corona.financiero.nmda.admision.service.SolicitudAdmisionService;
import corona.financiero.nmda.admision.util.Validaciones;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SolicitudAdmisionController.class)
class SolicitudAdmisionControllerTest {


    @MockBean
    private SolicitudAdmisionService solicitudAdmisionService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Validaciones validaciones;

    @MockBean
    private ExportComponent exportComponent;


    @Test
    void obtenerEstadoSolicitudAdmision_Status200() throws Exception {
        var response = new SolicitudAdmisionResponseDTO();
        response.setSolicitudId(1);
        response.setEstado("Iniciado");
        response.setFechaRegistroSolicitud(LocalDateTime.now());

        var fase = new AdmisionFaseResponseDTO();
        fase.setFaseId(1);
        fase.setAdmisionFaseId(2);
        fase.setFechaRegistroFase(LocalDateTime.now());
        fase.setDescripcionFase("Evaluacion");
        response.setFases(Arrays.asList(fase));

        when(solicitudAdmisionService.obtenerEstadoSolicitudAdmision(1)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/solicitud/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void obtenerDetalleSolicitudAdmision_Status200() throws Exception {

        var response = new DetalleSolicitudAdmisionResponseDTO();

        SucursalDTO s = new SucursalDTO();
        s.setNombre("Santiago");
        s.setCodigoSucursal(1l);
        s.setZonaGeografica("Santiago Centro");
        response.setSucursal(s);


        var ejecutivo = new FuncionarioDTO();
        ejecutivo.setRut("99999999-9");
        ejecutivo.setNombres("Juan Perez");
        response.setEjecutivo(ejecutivo);

        response.setMovil(987654321);
        response.setCanal("Tienda");
        response.setApellidoPaterno("Soto");
        response.setEmail("psoto@ab.cl");
        response.setNombres("Pedro");
        response.setEdad(30);
        response.setSolicitudId(1l);
        response.setFechaSolicitud(LocalDate.now());
        response.setRut("11111111-1");

        String rut = "11111111-1";
        String rutFormateado = "111111111";

        long solicitudAdmision = 1l;

        when(validaciones.formateaRutHaciaBD(rut)).thenReturn(rutFormateado);


        when(solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmision, rutFormateado)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/solicitud/detalle/1/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void listaSolicitudAdmision_Status200() throws Exception {

        FiltroSolicitudAdmisionDTO filtros = new FiltroSolicitudAdmisionDTO();
        filtros.setFechaDesde(new Date());
        filtros.setFechaHasta(new Date());

        String filtroJson = dtoToString(filtros);

        PaginacionSolicitudAdmisionDTO paginacion = new PaginacionSolicitudAdmisionDTO();
        ListaSolicitudAdmisionDTO listaSolicitudAdmisionDTO = new ListaSolicitudAdmisionDTO();
        listaSolicitudAdmisionDTO.setSolicitudId(1l);
        paginacion.setSolicitudes(Arrays.asList(listaSolicitudAdmisionDTO));
        paginacion.setPagina(0l);
        paginacion.setTotalPagina(1l);
        paginacion.setTotalElementos(1l);


        when(solicitudAdmisionService.listaSolicitudAdmision(filtros)).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/solicitud/")
                        .content(filtroJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void exportar_status200() throws Exception {

        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarSolicitudAdmision()).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/solicitud/exportar/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void exportarCartaRechazo_status200() throws Exception {

        String rutProspecto = "11111111-1";
        long solicitudId = 1l;

        var request = new BiometriaRequestDTO();
        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(true);

        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarCartaRechazo(rutProspecto, solicitudId)).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        String requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/solicitud/exportar/carta-rechazo/"+rutProspecto+"/"+solicitudId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void exportarCartaRechazoError_status200() throws Exception {

        String rutProspecto = "11111111-1";
        long solicitudId = 1l;

        var request = new BiometriaRequestDTO();
        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(false);
        request.setCodigo("1234");
        request.setDetalle("falta");
        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarCartaRechazo(rutProspecto, solicitudId)).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        String requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/solicitud/exportar/carta-rechazo/"+rutProspecto+"/"+solicitudId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void exportarCartaRechazoError2_status200() throws Exception {

        String rutProspecto = "11111111-1";
        long solicitudId = 1l;

        var request = new BiometriaRequestDTO();
        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(false);

        request.setDetalle("falta");

        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarCartaRechazo(rutProspecto, solicitudId)).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        String requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/solicitud/exportar/carta-rechazo/"+rutProspecto+"/"+solicitudId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    void exportarCartaRechazoError3_status200() throws Exception {

        String rutProspecto = "11111111-1";
        long solicitudId = 1l;

        var request = new BiometriaRequestDTO();
        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(false);
        request.setCodigo("1234");

        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarCartaRechazo(rutProspecto, solicitudId)).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        String requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/solicitud/exportar/carta-rechazo/"+rutProspecto+"/"+solicitudId)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    private <T> String dtoToString(T dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
}
