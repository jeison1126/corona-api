package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.dto.ecert.WebHookRequestDTO;
import corona.financiero.nmda.admision.service.FirmaDocumentoService;
import corona.financiero.nmda.admision.util.Constantes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/firma-documento")
@Tag(name = "Firma de documentos", description = "APIs de firma de documentos")
@Slf4j
public class FirmaDocumentoController {

    @Autowired
    private FirmaDocumentoService firmaDocumentoService;

    @PostMapping("/webhook/contrato")
    public ResponseEntity<Void> webhookContrato(@RequestBody @Valid WebHookRequestDTO request) {
        firmaDocumentoService.webhookFirmaDocumento(request, Constantes.FASE_FIRMA_CONTRATOS);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/webhook/seguros")
    public ResponseEntity<Void> webhookSeguros(@RequestBody @Valid WebHookRequestDTO request) {
        firmaDocumentoService.webhookFirmaDocumento(request, Constantes.FASE_SEGUROS);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/contrato")
    public ResponseEntity<FirmaDocumentoResponseDTO> firmaContrato(@RequestBody @Valid FirmaContratoRequestDTO request) {

        return new ResponseEntity<>(firmaDocumentoService.firmaContrato(request), HttpStatus.OK);
    }

    @PostMapping("/seguros")
    public ResponseEntity<Void> firmaSeguros(@RequestBody @Valid FirmaDocumentoRequestDTO request) {
        firmaDocumentoService.firmaSeguros(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validar-firma")
    public ResponseEntity<ValidarFirmaDocumentoResponseDTO> validarFirmaDocumento(@RequestBody @Valid ValidarFirmaDocumentoRequestDTO request) {

        return new ResponseEntity<>(firmaDocumentoService.validarFirmaDocumento(request), HttpStatus.OK);
    }

    @GetMapping("/descarga/{rutCliente}/{documentoId}")
    public ResponseEntity<Resource> descargaDocumentoFirmado(@PathVariable String rutCliente, @PathVariable int documentoId) throws IOException {
        DescargaDocumentoResponseDTO descargaDocumentoResponseDTO = firmaDocumentoService.descargaDocumentoFirmado(rutCliente, documentoId);
        InputStreamResource file = new InputStreamResource(descargaDocumentoResponseDTO.getDocumento());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + descargaDocumentoResponseDTO.getNombreDocumento())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/descarga/firma-manual/{rutCliente}/{solicitudId}/{documentoId}")
    public ResponseEntity<Resource> descargaDocumentoFirmaManual(@PathVariable String rutCliente, @PathVariable long solicitudId, @PathVariable int documentoId) throws IOException {

        DescargaDocumentoResponseDTO descargaDocumentoResponseDTO = firmaDocumentoService.descargaDocumentoFirmaManual(rutCliente, solicitudId, documentoId);

        InputStreamResource file = new InputStreamResource(descargaDocumentoResponseDTO.getDocumento());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + descargaDocumentoResponseDTO.getNombreDocumento())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }
}
