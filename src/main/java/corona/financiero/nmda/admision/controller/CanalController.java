package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.CanalDTO;
import corona.financiero.nmda.admision.service.CanalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/canal")
@Tag(name = "Canal", description = "APIs de Canales de Atención")
public class CanalController {

    @Autowired
    private CanalService canalService;

    @GetMapping("/")
    public ResponseEntity<List<CanalDTO>> listarCanales() {
        return new ResponseEntity<>(canalService.listarCanales(), HttpStatus.OK);
    }

}
