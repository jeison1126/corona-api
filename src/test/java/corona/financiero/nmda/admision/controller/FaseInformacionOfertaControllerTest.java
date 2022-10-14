package corona.financiero.nmda.admision.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import corona.financiero.nmda.admision.dto.MotivoRechazoTarjetaDTO;
import corona.financiero.nmda.admision.dto.OfertaAceptadaDTO;
import corona.financiero.nmda.admision.dto.OfertaRechazadaDTO;
import corona.financiero.nmda.admision.dto.ProductoCupoOfrecidoDTO;
import corona.financiero.nmda.admision.service.DiaPagoService;
import corona.financiero.nmda.admision.service.EvaluacionProductoService;
import corona.financiero.nmda.admision.service.FaseInformacionOfertaService;
import corona.financiero.nmda.admision.service.MotivoRechazoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FaseInformacionOfertaController.class)
class FaseInformacionOfertaControllerTest {

    @MockBean
    private FaseInformacionOfertaService faseInformacionOfertaService;
    
    @MockBean
    private DiaPagoService diaPagoService;

    @MockBean
    private EvaluacionProductoService evaluacionProductoService;
    
    @MockBean
    private MotivoRechazoService motivoRechazoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarDiasPagoVigentes_Status200() throws Exception {
    	
        List<Long> diasPago = Arrays.asList(1l);
        when(diaPagoService.listarDiasPagoVigentes()).thenReturn(diasPago);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/informacion-oferta/diaPago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarMotivosRechazoVigentes_Status200() throws Exception {

        var motivoRechazo = new MotivoRechazoTarjetaDTO();
        motivoRechazo.setMotivoRechazoId(1l);
        motivoRechazo.setDescripcion("Motivo1");

        List<MotivoRechazoTarjetaDTO> motivosRechazo = Arrays.asList(motivoRechazo);

        when(motivoRechazoService.listarMotivosRechazoVigentes()).thenReturn(motivosRechazo);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/informacion-oferta/motivoRechazoTarjeta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void listarProductosOfercidosBySolicitudIdAndRut_Status200() throws Exception {

        var productoCupoOfrecidoDTO = new ProductoCupoOfrecidoDTO();
        productoCupoOfrecidoDTO.setEvaluacionProductoId(1l);
        productoCupoOfrecidoDTO.setDescripcionTipoProducto("Tarjeta1");
        
        Long solicitudId= Long.valueOf(1);
    	String rut = "11111111-1";

        List<ProductoCupoOfrecidoDTO> lstProductos = Arrays.asList(productoCupoOfrecidoDTO);

        when(evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId,rut)).thenReturn(lstProductos);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/informacion-oferta/evaluacionProducto/solicitud/"+solicitudId+"/rut/"+rut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    void rechazarOferta_Status200() throws Exception {
        var request = new OfertaRechazadaDTO();
        request.setRut("11111111-1");

        doNothing().when(faseInformacionOfertaService).rechazarOferta(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/informacion-oferta/rechazarOferta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void aceptarOferta_Status200() throws Exception {
        var request = new OfertaAceptadaDTO();
        request.setRut("11111111-1");

        doNothing().when(faseInformacionOfertaService).aceptarOferta(request);

        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/informacion-oferta/aceptarOferta")
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
