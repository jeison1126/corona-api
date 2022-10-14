package corona.financiero.nmda.admision.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import corona.financiero.nmda.admision.dto.FuncionarioRequestDTO;
import corona.financiero.nmda.admision.dto.FuncionarioResponseDTO;
import corona.financiero.nmda.admision.dto.PaginacionFuncionarioResponseDTO;
import corona.financiero.nmda.admision.entity.FuncionariosEntity;
import corona.financiero.nmda.admision.service.FuncionariosService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FuncionariosController.class)
class FuncionariosControllerTest {

    @MockBean
    private FuncionariosService funcionariosService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarFuncionariosVigentes_Status200() throws Exception {
        var response = new FuncionarioResponseDTO();
        response.setRut("11111111-1");
        response.setNombreCompleto("juan perez");

        var paginacion = new PaginacionFuncionarioResponseDTO();
        paginacion.setPagina(0);
        paginacion.setTotalElementos(1);
        paginacion.setTotalPagina(1);
        paginacion.setFuncionarios(Arrays.asList(response));

        when(funcionariosService.listarFuncionariosVigentes(0, null)).thenReturn(paginacion);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/funcionario?pagina=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void crearFuncionario_Status200() throws Exception {
        var request = new FuncionarioRequestDTO();

        request.setRut("11111111-1");
        request.setNombreCompleto("Soto");
        
        FuncionariosEntity funcionariosEntity = new FuncionariosEntity();
        funcionariosEntity.setRut("111111111");
        funcionariosEntity.setNombreCompleto("Soto");

        when(funcionariosService.crearFuncionario(request)).thenReturn(funcionariosEntity);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/funcionario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void existeFuncionario_Status200() throws Exception {
        var response = true;

        var rut = "11111111-1";

        when(funcionariosService.existeFuncionario(rut)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/funcionario/existe/"+rut)
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
