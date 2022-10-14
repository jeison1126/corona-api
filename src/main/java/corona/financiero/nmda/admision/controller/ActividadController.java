package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ActividadResponseDTO;
import corona.financiero.nmda.admision.service.ActividadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/actividad")
@Tag(name = "Actividad", description = "APIs de Actividad")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/")
    public ResponseEntity<List<ActividadResponseDTO>> listarActividades() {

        return new ResponseEntity<>(actividadService.listarActividades(), HttpStatus.OK);
    }

}
