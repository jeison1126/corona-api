package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.MensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.NuevoMensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.PaginacionMensajeRechazoDTO;
import corona.financiero.nmda.admision.service.MensajeRechazoService;
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
@WebMvcTest(MensajeRechazoController.class)
class MensajeRechazoControllerTest {

    @MockBean
    private MensajeRechazoService mensajeRechazoService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarMensajesRechazo_Status200() throws Exception {
        var response = new MensajeRechazoDTO();
        response.setCausalRechazoId(1);
        response.setDescripcion("Descripcion Mensaje rechazo 1");
        response.setMensajeFuncional("Mensaje Funcional Mensaje Rechazo 1");
        response.setVigencia(true);


        var paginacion = new PaginacionMensajeRechazoDTO();
        paginacion.setMensajes(Arrays.asList(response));
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);

        when(mensajeRechazoService.listarMensajesRechazo(anyInt(), anyString())).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/mensaje-rechazo?pagina=0&filtro=")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void crearMensajeRechazo_Status200() throws Exception {
        var request = new NuevoMensajeRechazoDTO();

        request.setDescripcion("Descripcion mensaje rechazo 1");
        request.setMensajeFuncional("Mensaje Funcional Mensaje Rechazo 1");

        doNothing().when(mensajeRechazoService).crearMensajeRechazo(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/mensaje-rechazo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void actualizarMensajeRechazo_Status200() throws Exception {
        var request = new MensajeRechazoDTO();

        request.setCausalRechazoId(1l);
        request.setDescripcion("Descripcion Actualizada");

        doNothing().when(mensajeRechazoService).actualizarMensajeRechazo(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/mensaje-rechazo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminarMensajeRechazo_Status200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/mensaje-rechazo/1")
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
