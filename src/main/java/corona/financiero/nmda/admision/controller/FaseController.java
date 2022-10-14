package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ParFaseDTO;
import corona.financiero.nmda.admision.service.FaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fase")
@Tag(name = "Fases", description = "APIs de Fases")
public class FaseController {

    @Autowired
    private FaseService faseService;

    @GetMapping("/")
    public ResponseEntity<List<ParFaseDTO>> listarFasesVigentes() {

        return new ResponseEntity<>(faseService.listarFasesVigentes(), HttpStatus.OK);
    }
}
