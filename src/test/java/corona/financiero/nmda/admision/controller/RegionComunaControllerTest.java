package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ComunaDTO;
import corona.financiero.nmda.admision.dto.RegionDTO;
import corona.financiero.nmda.admision.service.RegionComunaService;
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
@WebMvcTest(RegionComunaController.class)
class RegionComunaControllerTest {

    @MockBean
    private RegionComunaService regionComunaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarRegiones_Status200() throws Exception {
        var response = new RegionDTO();
        response.setDescripcion("Tarapacá");
        response.setCodigo("01");

        when(regionComunaService.listarRegiones()).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/region-comuna/regiones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarComunas_Status200() throws Exception {
        var response = new ComunaDTO();
        response.setDescripcion("Comuna 1");
        response.setCodigo("01001");


        when(regionComunaService.listarComunas("01")).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/region-comuna/01/comunas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
