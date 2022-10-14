package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.PaginacionReglaNegocioDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioBasicoDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioCompletoDTO;
import corona.financiero.nmda.admision.service.ReglaNegocioService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReglaNegocioController.class)
class ReglaNegocioControllerTest {

    @MockBean
    private ReglaNegocioService reglaNegocioService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarPaginado_Status200() throws Exception {
        ReglaNegocioCompletoDTO response = new ReglaNegocioCompletoDTO();
        response.setId(1L);
        response.setDescripcion("Descripcion 1");
        response.setValor("Valor 1");
        response.setVigencia(true);


        var paginacion = new PaginacionReglaNegocioDTO();
        paginacion.setMensajes(Arrays.asList(response));
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);

        when(reglaNegocioService.listarPaginado(anyInt(), anyString(), anyString(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/regla-negocio/listarPaginado?numPagina=0&campoOrdena=descripcion&direccionOrdena=ASC&valorFiltro=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void recuperarPorId_Status200() throws Exception {
        ReglaNegocioCompletoDTO response = new ReglaNegocioCompletoDTO();
        response.setId(1L);
        response.setDescripcion("Descripcion 1");
        response.setValor("Valor 1");
        response.setVigencia(true);

        when(reglaNegocioService.recuperarPorId(anyString())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/regla-negocio/recuperarPorId/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void registrar_Status200() throws Exception {
        var request = new ReglaNegocioBasicoDTO();

        request.setDescripcion("Descripcion 1");
        request.setValor("Valor 1");

        doNothing().when(reglaNegocioService).registrar(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/regla-negocio/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_Status200() throws Exception {
        var request = new ReglaNegocioCompletoDTO();

        request.setId(1l);
        request.setDescripcion("Descripcion Actualizada");

        doNothing().when(reglaNegocioService).actualizar(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/regla-negocio/actualizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminarPorId_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/regla-negocio/eliminarPorId/1")
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
