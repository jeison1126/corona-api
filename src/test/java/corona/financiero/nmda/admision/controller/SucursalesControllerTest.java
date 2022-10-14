package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.SucursalDTO;
import corona.financiero.nmda.admision.dto.ZonaGeograficaDTO;
import corona.financiero.nmda.admision.service.SucursalesService;
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
@WebMvcTest(SucursalesController.class)
class SucursalesControllerTest {

    @MockBean
    private SucursalesService sucursalesService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarSucursales_Status200() throws Exception {
        var response = new SucursalDTO();
        response.setCodigoSucursal(1);
        response.setNombre("Nombre sucursal 1");

        when(sucursalesService.listarSucursales("")).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarSucursalesPorZona_Status200() throws Exception {
        var response = new SucursalDTO();
        response.setCodigoSucursal(1);
        response.setNombre("Nombre sucursal 1");
        response.setZonaGeografica("Zona 1");

        when(sucursalesService.listarSucursales("zona 1")).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sucursales?zona=zona 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void listarZonasGeograficasSucursales_Status200() throws Exception {
        var response = new ZonaGeograficaDTO();
        response.setZonaGeografica("Santuiago Centro");

        when(sucursalesService.listarZonasGeograficasSucursal()).thenReturn(Arrays.asList(response));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sucursales/zonas/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
