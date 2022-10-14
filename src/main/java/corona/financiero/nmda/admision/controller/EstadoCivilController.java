package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.EstadoCivilResponseDTO;
import corona.financiero.nmda.admision.service.EstadoCivilService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estado-civil")
@Tag(name = "Estado Civil", description = "APIs de Estado Civil")
public class EstadoCivilController {

    @Autowired
    private EstadoCivilService estadoCivilService;

    @GetMapping("/")
    public ResponseEntity<List<EstadoCivilResponseDTO>> listarEstadoCivil() {

        return new ResponseEntity<>(estadoCivilService.listarEstadoCivil(), HttpStatus.OK);
    }
}
