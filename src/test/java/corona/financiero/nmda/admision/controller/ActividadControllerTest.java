package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ActividadResponseDTO;
import corona.financiero.nmda.admision.dto.CanalDTO;
import corona.financiero.nmda.admision.service.ActividadService;
import corona.financiero.nmda.admision.service.CanalService;
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
@WebMvcTest(ActividadController.class)
class ActividadControllerTest {

    @MockBean
    private ActividadService actividadService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarActividadesVigentes_Status200() throws Exception {
        var response = new ActividadResponseDTO();
        response.setActividadId(1);
        response.setDescripcion("Dependiente");

        when(actividadService.listarActividades()).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/actividad/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
