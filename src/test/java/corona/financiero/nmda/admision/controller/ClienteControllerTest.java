package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.ClienteActivoDatosResponseDTO;
import corona.financiero.nmda.admision.dto.ClienteActivoRequestDTO;
import corona.financiero.nmda.admision.dto.RegistroClienteRequestDTO;
import corona.financiero.nmda.admision.service.ClienteService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registrarClienteOk() throws Exception {
        var request = new RegistroClienteRequestDTO();
        request.setRut("11111111-1");
        request.setCalle("Av uno");
        request.setActividad(1);
        request.setCodigoComuna("1301");
        request.setCodigoRegion("13");
        request.setEnvioEcc(true);
        request.setEnvioSMS(false);
        request.setNumero("23");
        request.setProfesion(2);
        request.setNacionalidad(1);
        request.setEstadoCivil(1);

        String json = dtoToString(request);

        doNothing().when(clienteService).registroDatosCliente(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cliente/datos/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void clienteActivoOK() throws Exception {

        ClienteActivoRequestDTO request = new ClienteActivoRequestDTO();
        request.setRut("11111111-1");

        String json = dtoToString(request);

        ClienteActivoDatosResponseDTO response = new ClienteActivoDatosResponseDTO();
        response.setNumTarjeta("1234");
        response.setNombre("Juan");
        response.setApellidoPaterno("Perez");
        response.setApellidoMaterno("Soto");
        response.setDescripcionTarjeta("tarjeta Full");
        response.setEstadoTarjeta("Activo");

        when(clienteService.validarClienteTarjetaActiva(request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cliente/cliente-activo/")
                        .content(json)
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
