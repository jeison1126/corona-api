package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.EmisionMotivoResponseDTO;
import corona.financiero.nmda.admision.service.EmisionTarjetaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emision")
@Tag(name = "Emision tarjeta", description = "APIs de Emisión de tarjetas")
public class EmisionTarjetaController {

    @Autowired
    private EmisionTarjetaService emisionTarjetaService;

    @GetMapping("/motivo")
    public ResponseEntity<List<EmisionMotivoResponseDTO>> listarMotivos() {
        return new ResponseEntity<>(emisionTarjetaService.listarMotivos(), HttpStatus.OK);
    }
}
