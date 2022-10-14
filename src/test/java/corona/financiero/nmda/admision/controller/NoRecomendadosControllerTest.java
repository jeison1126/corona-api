package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.NoRecomendadosDTO;
import corona.financiero.nmda.admision.dto.NoRecomendadosRequestDTO;
import corona.financiero.nmda.admision.dto.PaginacionNoRecomendadosDTO;
import corona.financiero.nmda.admision.dto.SucursalDTO;
import corona.financiero.nmda.admision.service.NoRecomendadosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NoRecomendadosController.class)
class NoRecomendadosControllerTest {

    @MockBean
    private NoRecomendadosService noRecomendadosService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarNoRecomendados_Status200() throws Exception {
        var response = new NoRecomendadosDTO();
        response.setRut("11111111-1");
        response.setNombre("Nombre sucursal 1");

        var paginacion = new PaginacionNoRecomendadosDTO();
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);
        paginacion.setNoRecomendados(Arrays.asList(response));

        when(noRecomendadosService.listarNoRecomendados(0, null)).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/no_recomendados?pagina=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void obtenerNoRecomendados_Status200() throws Exception {
        String rut = "11111111-1";
        var response = new NoRecomendadosDTO();
        response.setRut("11111111-1");
        response.setNombre("Nombre sucursal 1");

        when(noRecomendadosService.obtenerNoRecomendable(rut)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/no_recomendados/" + rut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void registrarNoRecomendado_Status200() throws Exception {
        var request = new NoRecomendadosRequestDTO();

        request.setMotivo("No paga");
        SucursalDTO sucursalDTO = new SucursalDTO();
        request.setSucursalOrigen(1l);
        request.setRut("11111111-1");
        request.setApellidoMaterno("Soto");
        request.setApellidoPaterno("Perez");
        request.setNombre("Juan");

        doNothing().when(noRecomendadosService).registrarNoRecomendado(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/no_recomendados/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void actualizarNoRecomendado_Status200() throws Exception {
        var request = new NoRecomendadosRequestDTO();

        request.setMotivo("No paga");
        SucursalDTO sucursalDTO = new SucursalDTO();
        request.setSucursalOrigen(1l);
        request.setRut("11111111-1");
        request.setApellidoMaterno("Soto");
        request.setApellidoPaterno("Perez");
        request.setNombre("Juan");

        doNothing().when(noRecomendadosService).registrarNoRecomendado(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/no_recomendados/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminarNoRecomendado_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/no_recomendados/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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
