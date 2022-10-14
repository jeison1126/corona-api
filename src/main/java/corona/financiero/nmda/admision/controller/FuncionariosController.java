package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.FuncionarioRequestDTO;
import corona.financiero.nmda.admision.dto.PaginacionFuncionarioResponseDTO;
import corona.financiero.nmda.admision.service.FuncionariosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/funcionario")
@Tag(name = "Funcionarios", description = "APIs para Funcionarios")
public class FuncionariosController {

    @Autowired
    private FuncionariosService funcionariosService;

    @GetMapping("/existe/{rutFuncionario}")
    public ResponseEntity<Boolean> existeFuncionario(@PathVariable String rutFuncionario) {
        return new ResponseEntity<>(funcionariosService.existeFuncionario(rutFuncionario), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<PaginacionFuncionarioResponseDTO> listarFuncionariosVigentes(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                                                       @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {
        return new ResponseEntity<>(funcionariosService.listarFuncionariosVigentes(pagina, filtro), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> crearFuncionario(@RequestBody @Valid FuncionarioRequestDTO request) {
        funcionariosService.crearFuncionario(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
