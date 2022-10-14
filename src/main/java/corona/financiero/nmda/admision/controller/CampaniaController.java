package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.CampaniaCoronaRequestDTO;
import corona.financiero.nmda.admision.dto.PaginacionCampaniaCoronaResponseDTO;
import corona.financiero.nmda.admision.service.CampaniasService;
import corona.financiero.nmda.admision.service.ExportComponent;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/campania-corona")
@Tag(name = "Campaña Corona", description = "APIs de Campaña Corona - Revaluacion Cliente")
@Slf4j
public class CampaniaController {

    @Autowired
    private CampaniasService campaniasService;

    @Autowired
    private ExportComponent exportComponent;

    @GetMapping()
    public ResponseEntity<PaginacionCampaniaCoronaResponseDTO> listarCampaniaCorona(@RequestParam(name = "numPagina", required = false, defaultValue = "0") int numPagina) {
        return new ResponseEntity<>(campaniasService.listarCampaniaCorona(numPagina), HttpStatus.OK);
    }

    @DeleteMapping("/{cabeceraCampaniaId}")
    public ResponseEntity<Void> eliminarRegistrosCargaMasiva(@PathVariable long cabeceraCampaniaId) {

        campaniasService.eliminarRegistrosCargaMasiva(cabeceraCampaniaId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/carga-masiva")
    public ResponseEntity<Void> cargaCampaniaMasiva(
            @RequestBody @Valid CampaniaCoronaRequestDTO request) throws IOException {
        campaniasService.cargaCampaniaMasiva(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/exportar/{cabeceraCampaniaId}")
    public ResponseEntity<Resource> exportarRegistrosCampaniasCorona(@PathVariable long cabeceraCampaniaId) {

        String filename = "procesados".concat(".xlsx");
        InputStreamResource file = new InputStreamResource(exportComponent.exportarRegistrosCampaniasCorona(cabeceraCampaniaId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
