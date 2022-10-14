package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.EstadoCivilResponseDTO;
import corona.financiero.nmda.admision.service.EstadoCivilService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EstadoCivilController.class)
class EstadoCivilControllerTest {

    @MockBean
    private EstadoCivilService estadoCivilService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarEstadoCivilVigentes_Status200() throws Exception {
        var response = new EstadoCivilResponseDTO();
        response.setEstadoCivilId(1);
        response.setDescripcion("Soltero");

        when(estadoCivilService.listarEstadoCivil()).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/estado-civil/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
