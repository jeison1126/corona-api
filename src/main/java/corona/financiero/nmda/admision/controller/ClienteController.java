package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ClienteActivoRequestDTO;
import corona.financiero.nmda.admision.dto.ClienteActivoDatosResponseDTO;
import corona.financiero.nmda.admision.dto.RegistroClienteRequestDTO;
import corona.financiero.nmda.admision.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente", description = "APIs de Prospecto / Cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/datos/")
    public ResponseEntity<Void> registroDatosCliente(@RequestBody RegistroClienteRequestDTO request) {
        clienteService.registroDatosCliente(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cliente-activo/")
    public ResponseEntity<ClienteActivoDatosResponseDTO> validarClienteTarjetaActiva(@RequestBody ClienteActivoRequestDTO request) {
        return new ResponseEntity<>(clienteService.validarClienteTarjetaActiva(request), HttpStatus.OK);
    }
}
