package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.PaginacionReglaNegocioDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioBasicoDTO;
import corona.financiero.nmda.admision.dto.ReglaNegocioCompletoDTO;
import corona.financiero.nmda.admision.service.ReglaNegocioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/regla-negocio")
@Tag(name = "Reglas de Negocio", description = "APIs de Reglas de Negocio")
public class ReglaNegocioController {

    @Autowired
    private ReglaNegocioService reglaNegocioService;

    @PostMapping("registrar")
    public ResponseEntity<Void> registrar(@RequestBody ReglaNegocioBasicoDTO reglaNegocioDTO) {
        reglaNegocioService.registrar(reglaNegocioDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("actualizar")
    public ResponseEntity<Void> actualizar(@RequestBody ReglaNegocioCompletoDTO reglaNegocioCompletoDTO) {
        reglaNegocioService.actualizar(reglaNegocioCompletoDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("recuperarPorId/{id}")
    public ResponseEntity<ReglaNegocioCompletoDTO> recuperarPorId(@PathVariable String id) {
        return new ResponseEntity<>(reglaNegocioService.recuperarPorId(id), HttpStatus.OK);
    }

    @GetMapping("listarPaginado")
    public ResponseEntity<PaginacionReglaNegocioDTO> listarPaginado(
            @RequestParam(name = "numPagina", required = false, defaultValue = "0") int numPagina,
            @RequestParam(name = "campoOrdena", required = false, defaultValue = "descripcion") String campoOrdena,
            @RequestParam(name = "direccionOrdena", required = false, defaultValue = "ASC") String direccionOrdena,
            @RequestParam(name = "valorFiltro", required = false, defaultValue = "") String valorFiltro) {

        return new ResponseEntity<>(reglaNegocioService.listarPaginado(numPagina, campoOrdena, direccionOrdena, valorFiltro), HttpStatus.OK);
    }

    @DeleteMapping("eliminarPorId/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        reglaNegocioService.eliminarPorId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
