package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.SucursalDTO;
import corona.financiero.nmda.admision.dto.ZonaGeograficaDTO;
import corona.financiero.nmda.admision.service.SucursalesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
@Tag(name = "Sucursales", description = "APIs de Sucursales")
public class SucursalesController {

    @Autowired
    private SucursalesService sucursalesService;

    @GetMapping()
    public ResponseEntity<List<SucursalDTO>> listarSucursales(@RequestParam(name = "zona", required = false, defaultValue = "") String zona) {
        return new ResponseEntity<>(sucursalesService.listarSucursales(zona), HttpStatus.OK);
    }

    @GetMapping("/zonas/")
    public ResponseEntity<List<ZonaGeograficaDTO>> listarZonasGeograficasSucursal() {
        return new ResponseEntity<>(sucursalesService.listarZonasGeograficasSucursal(), HttpStatus.OK);
    }
}
