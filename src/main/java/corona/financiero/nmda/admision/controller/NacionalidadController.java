package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.NacionalidadDTO;
import corona.financiero.nmda.admision.service.NacionalidadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nacionalidad")
@Tag(name = "Nacionalidad", description = "APIs de Nacionalidades")
public class NacionalidadController {

    @Autowired
    private NacionalidadService nacionalidadService;

    @GetMapping("/")
    public ResponseEntity<List<NacionalidadDTO>> listarNacionalidades() {

        return new ResponseEntity<>(nacionalidadService.listarNacionalidades(), HttpStatus.OK);
    }
}
