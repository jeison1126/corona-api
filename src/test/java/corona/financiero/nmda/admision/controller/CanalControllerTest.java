package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.CanalDTO;
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
@WebMvcTest(CanalController.class)
class CanalControllerTest {

    @MockBean
    private CanalService canalService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarCanalVigentes_Status200() throws Exception {
        var response = new CanalDTO();
        response.setCanalId(1);
        response.setNombre("Web");

        when(canalService.listarCanales()).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/canal/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
