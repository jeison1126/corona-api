package corona.financiero.nmda.admision.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import corona.financiero.nmda.admision.dto.MotivoRechazoTarjetaDTO;
import corona.financiero.nmda.admision.dto.OfertaAceptadaDTO;
import corona.financiero.nmda.admision.dto.OfertaRechazadaDTO;
import corona.financiero.nmda.admision.dto.ProductoCupoOfrecidoDTO;
import corona.financiero.nmda.admision.service.DiaPagoService;
import corona.financiero.nmda.admision.service.EvaluacionProductoService;
import corona.financiero.nmda.admision.service.FaseInformacionOfertaService;
import corona.financiero.nmda.admision.service.MotivoRechazoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/informacion-oferta")
@Tag(name = "Fase Informacion Oferta", description = "APIs para la fase Informacion Oferta")
public class FaseInformacionOfertaController {

    @Autowired
    private FaseInformacionOfertaService faseInformacionOfertaService;
    
    @Autowired
    private DiaPagoService diaPagoService;

    @Autowired
    private EvaluacionProductoService evaluacionProductoService;
    
    @Autowired
    private MotivoRechazoService motivoRechazoService;

    @GetMapping("/diaPago")
    public ResponseEntity<List<Long>> listarDiasPagoVigentes() {
        return new ResponseEntity<>(diaPagoService.listarDiasPagoVigentes(), HttpStatus.OK);
    }
    
    @GetMapping("/motivoRechazoTarjeta")
    public ResponseEntity<List<MotivoRechazoTarjetaDTO>> listarMotivosRechazoVigentes() {
        return new ResponseEntity<>(motivoRechazoService.listarMotivosRechazoVigentes(), HttpStatus.OK);
    }
    
    @GetMapping("/evaluacionProducto/solicitud/{solicitudId}/rut/{rut}")
    public ResponseEntity<List<ProductoCupoOfrecidoDTO>> listarProductosOfercidosBySolicitudIdAndRut(@PathVariable Long solicitudId, @PathVariable String rut) {
        return new ResponseEntity<>(evaluacionProductoService.listarProductosOfercidosBySolicitudIdAndRut(solicitudId, rut), HttpStatus.OK);
    }

    @PostMapping("/rechazarOferta")
    public ResponseEntity<Void> rechazarOferta(@RequestBody @Valid OfertaRechazadaDTO request) {
    	faseInformacionOfertaService.rechazarOferta(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/aceptarOferta")
    public ResponseEntity<Void> aceptarOferta(@RequestBody @Valid OfertaAceptadaDTO request) {
    	faseInformacionOfertaService.aceptarOferta(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
