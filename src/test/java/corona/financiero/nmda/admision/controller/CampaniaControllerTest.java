package corona.financiero.nmda.admision.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.CampaniaCoronaRequestDTO;
import corona.financiero.nmda.admision.dto.CampaniaCoronaResponseDTO;
import corona.financiero.nmda.admision.dto.PaginacionCampaniaCoronaResponseDTO;
import corona.financiero.nmda.admision.service.CampaniasService;
import corona.financiero.nmda.admision.service.ExportComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CampaniaController.class)
class CampaniaControllerTest {

    @MockBean
    private CampaniasService campaniasService;

    @MockBean
    private ExportComponent exportComponent;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void listarCampaniaCorona_Status200() throws Exception {
        var campania = new CampaniaCoronaResponseDTO();
        campania.setCabeceraCampaniaId(1l);
        campania.setRegistrosProcesados(1);
        campania.setCantidadRegistros(1);
        campania.setCantidadErrores(0);
        campania.setNombreArchivo("cargaMasiva.xlsx");
        campania.setFechaInicio(LocalDate.now());
        campania.setFechaTermino(LocalDate.now().plusDays(20));
        campania.setUsuario("USR_TMP");
        var paginacion = new PaginacionCampaniaCoronaResponseDTO();
        paginacion.setCampanias(Arrays.asList(campania));
        paginacion.setPagina(0);
        paginacion.setTotalPagina(1);
        paginacion.setTotalElementos(1);


        when(campaniasService.listarCampaniaCorona(anyInt())).thenReturn(paginacion);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campania-corona?numPagina=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void eliminarCampaniaCorona() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/campania-corona/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void cargaMasiva_status200() throws Exception {
        var request = new CampaniaCoronaRequestDTO();
        request.setArchivoBase64("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");
        request.setNombreArchivo("cargaMasiva.xlsx");
        request.setExtension(".xlsx");

        doNothing().when(campaniasService).cargaCampaniaMasiva(request);
        var requestJson = dtoToString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/campania-corona/carga-masiva/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void exportar_status200() throws Exception {
        String tmp = "test";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(tmp.getBytes());

        when(exportComponent.exportarRegistrosCampaniasCorona(anyLong())).thenReturn(byteArrayInputStream);

        InputStreamResource i = new InputStreamResource(byteArrayInputStream);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/campania-corona/exportar/1")
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
            e.printStackTrace();
        }
        return null;
    }

    /*

    @GetMapping("/exportar/{cabeceraCampaniaId}")
    public ResponseEntity<Resource> exportarRegistrosCampaniasCorona(@PathVariable long cabeceraCampaniaId) {

        String filename = "procesados".concat(".xlsx");
        InputStreamResource file = new InputStreamResource(exportComponent.exportarRegistrosCampaniasCorona(cabeceraCampaniaId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
     */
}
