package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.BiometriaService;
import corona.financiero.nmda.admision.util.Constantes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BiometriaController.class)
class BiometriaControllerTest {
    @MockBean
    private BiometriaService biometriaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void activarBiometriaPropspecto_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void registroBiometriaProspecto_Status200() throws Exception {
        String rut = "11111111-1";
        long solicitudId = 2;
        var request = new BiometriaRequestDTO();

        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(true);


        var response = new BiometriaResponseDTO();
        response.setError(false);

        when(biometriaService.registrarBiometriaCotizacionesExcepcion(rut, solicitudId, request)).thenReturn(response);


        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/prospecto-solicitud/" + rut + "/" + solicitudId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaExcepcion_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/excepcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void registroBiometriaExcepcion_Status200() throws Exception {
        String rut = "11111111-1";
        long solicitudId = 2;
        var request = new BiometriaRequestDTO();

        BiometriaDataRequestDTO data = new BiometriaDataRequestDTO();
        data.setRut("11111111-1");
        data.setApellido("Perez");
        data.setSerie("123454321");
        data.setHuellaCoincide(true);
        data.setVencimiento("2022-05-29");
        data.setTipo("Nueva");
        data.setTransaccion("12345678999876544");
        request.setData(data);
        request.setResultado(true);

        var response = new BiometriaResponseDTO();
        response.setError(false);

        when(biometriaService.registrarBiometriaCotizacionesExcepcion(rut, solicitudId, request)).thenReturn(response);


        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/excepcion/" + rut + "/" + solicitudId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verificacion_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new VerificacionBiometriaResponseDTO();
        response.setSolicitudId(2l);
        response.setValidacionExcepcion(false);
        response.setValidacionBiometrica(true);

        when(biometriaService.verificaValidacionBiometrica(request)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/verificacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaCartaRechazo_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometricoCartaRechazo(request, true)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/carta-rechazo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaCartaRechazoExcepcion_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometricoCartaRechazo(request, false)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/carta-rechazo/excepcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaFirmaElectronicaExcepcion_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_DATOS_CLIENTE)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/firma-electronica/excepcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaContratoSeguro_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/contrato-seguro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void activarBiometriaContratoSeguroExcepcion_Status200() throws Exception {
        var request = new ActivarDispositivoRequestDTO();

        request.setRut("11111111-1");
        request.setSolicitudAdmisionId(2l);

        var response = new ActivarDispositivoResponseDTO();
        response.setRut("11111111-1");
        response.setType("POST");
        response.setUrl("http://localhost:1760/Biometria");

        when(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA)).thenReturn(response);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/biometria/activar/contrato-seguro/excepcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
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
