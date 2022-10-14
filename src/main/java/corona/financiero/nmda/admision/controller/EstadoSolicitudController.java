package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.EstadoSolicitudDTO;
import corona.financiero.nmda.admision.service.EstadoSolicitudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estado_solicitud")
@Tag(name = "Estado Solicitud", description = "APIs de Estado de Solicitud")
public class EstadoSolicitudController {

    @Autowired
    private EstadoSolicitudService estadoSolicitudService;

    @GetMapping("/")
    public ResponseEntity<List<EstadoSolicitudDTO>> listarEstados() {
        return new ResponseEntity<>(estadoSolicitudService.listarEstados(), HttpStatus.OK);
    }

}
