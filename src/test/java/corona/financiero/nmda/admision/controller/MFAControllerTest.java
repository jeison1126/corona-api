package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.MFARequestDTO;
import corona.financiero.nmda.admision.dto.ValidarMFARequestDTO;
import corona.financiero.nmda.admision.service.MFAService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MFAController.class)
class MFAControllerTest {


    @MockBean
    private MFAService mfaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void enviarSMSTest() throws Exception {
        var request = new MFARequestDTO();
        request.setRut("111111111");

        String requestJson = dtoToString(request);

        doNothing().when(mfaService).enviarSMS(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/mfa/")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void validarSMSTest() throws Exception {
        var request = new ValidarMFARequestDTO();
        request.setRut("111111111");
        request.setCodigo("asd12");

        String requestJson = dtoToString(request);

        doNothing().when(mfaService).validarSMS(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/mfa/validar")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verificacionTest() throws Exception {
        var request = new MFARequestDTO();
        request.setRut("111111111");

        String requestJson = dtoToString(request);

        doNothing().when(mfaService).enviarSMS(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/mfa/verificacion")
                        .content(requestJson)
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
