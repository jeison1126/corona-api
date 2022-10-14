package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.MensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.NuevoMensajeRechazoDTO;
import corona.financiero.nmda.admision.dto.PaginacionMensajeRechazoDTO;
import corona.financiero.nmda.admision.service.MensajeRechazoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mensaje-rechazo")
@Tag(name = "Mensajes de Rechazo", description = "APIs de Mensajes de Rechazo")
public class MensajeRechazoController {

    @Autowired
    private MensajeRechazoService mensajeRechazoService;

    @GetMapping()
    public ResponseEntity<PaginacionMensajeRechazoDTO> listarMensajesRechazo(@RequestParam(name = "pagina", required = false, defaultValue = "0") int pagina,
                                                                             @RequestParam(name = "filtro", required = false, defaultValue = "") String filtro) {

        return new ResponseEntity<>(mensajeRechazoService.listarMensajesRechazo(pagina, filtro), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Void> crearMensajeRechazo(@RequestBody @Valid NuevoMensajeRechazoDTO request) {

        mensajeRechazoService.crearMensajeRechazo(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Void> actualizarMensajeRechazo(@RequestBody @Valid MensajeRechazoDTO request) {

        mensajeRechazoService.actualizarMensajeRechazo(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{mensajeRechazoId}")
    public ResponseEntity<Void> eliminarMensajeRechazo(@PathVariable long mensajeRechazoId) {

        mensajeRechazoService.eliminarMensajeRechazo(mensajeRechazoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
