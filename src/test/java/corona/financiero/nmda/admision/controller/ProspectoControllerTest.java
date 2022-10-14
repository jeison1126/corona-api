package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.DatosBasicosMinimosResponseDTO;
import corona.financiero.nmda.admision.dto.ProspectoDTO;
import corona.financiero.nmda.admision.dto.ProspectoResponseDTO;
import corona.financiero.nmda.admision.dto.RegistroProspectoResponseDTO;
import corona.financiero.nmda.admision.service.ProspectoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProspectoController.class)
class ProspectoControllerTest {


    @MockBean
    private ProspectoService prospectoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buscarCliente_Status200() throws Exception {
        var response = new ProspectoResponseDTO();
        response.setEmail("a@b.cl");
        response.setMovil("912345678");
        response.setRut("11111111-1");
        response.setProspectoId(1l);

        when(prospectoService.buscarCliente(any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cliente/11111111-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void crearProspecto_Status200() throws Exception {
        var request = new ProspectoDTO();

        request.setRut("11111111-1");
        request.setEmail("a@b.cl");
        request.setMovil("912345678");

        var response = new RegistroProspectoResponseDTO();
        response.setExito(true);
        response.setMensaje("Formulario de registro procesado correctamente");

        when(prospectoService.registroProspecto(request)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cliente/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void datosPersonalesBasicos_Status200() throws Exception {
        DatosBasicosMinimosResponseDTO response = new DatosBasicosMinimosResponseDTO();
        response.setProspectoId(1l);
        response.setRut("11111111-1");
        response.setNombre("Juan");
        response.setApellidoPatenro("Perez");
        response.setApellidoMaterno("Soto");
        String rut = "11111111-1";
        when(prospectoService.datosPersonalesBasicos(rut)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/cliente/datos/11111111-1")
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
