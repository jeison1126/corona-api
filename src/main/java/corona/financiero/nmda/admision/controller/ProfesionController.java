package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ProfesionResponseDTO;
import corona.financiero.nmda.admision.service.ProfesionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profesion")
@Tag(name = "Profesion Ocupacion  u Oficio", description = "APIs de Profesion / Ocupacion / Oficio")
public class ProfesionController {

    @Autowired
    private ProfesionService profesionService;

    @GetMapping("/")
    public ResponseEntity<List<ProfesionResponseDTO>> listarProfesion() {

        return new ResponseEntity<>(profesionService.listarProfesion(), HttpStatus.OK);
    }
}
