package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.ComunaDTO;
import corona.financiero.nmda.admision.dto.RegionDTO;
import corona.financiero.nmda.admision.service.RegionComunaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/region-comuna")
@Tag(name = "Region y Comunas", description = "APIs de regiones y comunas")
public class RegionComunaController {

    @Autowired
    private RegionComunaService regionComunaService;

    @GetMapping("/regiones")
    public ResponseEntity<List<RegionDTO>> listarRegiones() {
        return new ResponseEntity<>(regionComunaService.listarRegiones(), HttpStatus.OK);
    }

    @GetMapping("/{codigoRegion}/comunas")
    public ResponseEntity<List<ComunaDTO>> listarComunas(@PathVariable String codigoRegion) {
        return new ResponseEntity<>(regionComunaService.listarComunas(codigoRegion), HttpStatus.OK);
    }
}
