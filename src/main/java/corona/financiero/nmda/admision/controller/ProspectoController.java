package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.ProspectoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente", description = "APIs de Prospecto / Cliente")
public class ProspectoController {

    @Autowired
    private ProspectoService prospectoService;

    @PostMapping("/registro")
    public ResponseEntity<RegistroProspectoResponseDTO> registroProspecto(@RequestBody ProspectoDTO prospectoDTO) {
        return new ResponseEntity<>(prospectoService.registroProspecto(prospectoDTO), HttpStatus.OK);
    }

    @GetMapping("/{rutCliente}")
    public ResponseEntity<ProspectoResponseDTO> buscarCliente(@PathVariable String rutCliente) {
        return new ResponseEntity<>(prospectoService.buscarCliente(rutCliente), HttpStatus.OK);
    }

    @GetMapping("/datos/{rutCliente}")
    public ResponseEntity<DatosBasicosMinimosResponseDTO> datosPersonalesBasicos(@PathVariable String rutCliente) {
        return new ResponseEntity<>(prospectoService.datosPersonalesBasicos(rutCliente), HttpStatus.OK);
    }
}