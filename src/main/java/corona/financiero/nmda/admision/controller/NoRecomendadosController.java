package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.NoRecomendadosDTO;
import corona.financiero.nmda.admision.dto.NoRecomendadosRequestDTO;
import corona.financiero.nmda.admision.dto.PaginacionNoRecomendadosDTO;
import corona.financiero.nmda.admision.service.NoRecomendadosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/no_recomendados")
@Tag(name = "No Recomendados", description = "APIs de No Recomendados")
public class NoRecomendadosController {


    @Autowired
    private NoRecomendadosService noRecomendadosService;

    @GetMapping()
    public ResponseEntity<PaginacionNoRecomendadosDTO> listarNoRecomendados(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                                            @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {

        return new ResponseEntity<>(noRecomendadosService.listarNoRecomendados(pagina, filtro), HttpStatus.OK);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<NoRecomendadosDTO> obtenerNoRecomendable(@PathVariable String rut) {

        return new ResponseEntity<>(noRecomendadosService.obtenerNoRecomendable(rut), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> registrarNoRecomendado(@RequestBody @Valid NoRecomendadosRequestDTO request) {

        noRecomendadosService.registrarNoRecomendado(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/")
    public ResponseEntity<Void> actualizarNoRecomendado(@RequestBody @Valid NoRecomendadosRequestDTO request) {

        noRecomendadosService.actualizarNoRecomendado(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarNoRecomendado(@PathVariable String rut) {

        noRecomendadosService.eliminarNoRecomendado(rut);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
