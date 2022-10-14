package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.EmisionMotivoResponseDTO;
import corona.financiero.nmda.admision.service.EmisionTarjetaService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(EmisionTarjetaController.class)
class EmisionTarjetaControllerTest {

    @MockBean
    private EmisionTarjetaService emisionTarjetaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarMotivo_status200() throws Exception {

        var response = new EmisionMotivoResponseDTO();
        response.setMotivoId(1l);
        response.setDescripcion(("Cliente Nuevo"));

        var response2 = new EmisionMotivoResponseDTO();
        response2.setMotivoId(2l);
        response2.setDescripcion("Extravio");

        List<EmisionMotivoResponseDTO> emisionMotivoResponseDTOS = Arrays.asList(response, response2);

        when(emisionTarjetaService.listarMotivos()).thenReturn(emisionMotivoResponseDTOS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/emision/motivo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
