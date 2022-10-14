package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.service.ExportComponent;
import corona.financiero.nmda.admision.service.SolicitudAdmisionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/solicitud")
@Tag(name = "Solicitud Admision", description = "APIs de Solicitud Admision")
public class SolicitudAdmisionController {

    @Autowired
    private SolicitudAdmisionService solicitudAdmisionService;

    @Autowired
    private ExportComponent exportComponent;

    @GetMapping("/{prospectoId}")
    public ResponseEntity<SolicitudAdmisionResponseDTO> obtenerEstadoSolicitudAdmision(@PathVariable long prospectoId) {

        return new ResponseEntity<>(solicitudAdmisionService.obtenerEstadoSolicitudAdmision(prospectoId), HttpStatus.OK);
    }

    @GetMapping("/detalle/{solicitudAdmisionId}/{rutProspecto}")
    public ResponseEntity<DetalleSolicitudAdmisionResponseDTO> detalleSolicitudAdmision(@PathVariable long solicitudAdmisionId, @PathVariable String rutProspecto) {
        return new ResponseEntity<>(solicitudAdmisionService.detalleSolicitudAdmision(solicitudAdmisionId, rutProspecto), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<PaginacionSolicitudAdmisionDTO> listaSolicitudAdmision(@RequestBody FiltroSolicitudAdmisionDTO filtros) {
        return new ResponseEntity<>(solicitudAdmisionService.listaSolicitudAdmision(filtros), HttpStatus.OK);
    }


    @GetMapping("/exportar/")
    public ResponseEntity<Resource> exportar() {

        LocalDateTime ldt = LocalDateTime.now();
        String fechaFormateada = ldt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));

        String filename = "solicitudes_admision_".concat(fechaFormateada).concat(".xlsx");
        InputStreamResource file = new InputStreamResource(exportComponent.exportarSolicitudAdmision());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }


    @PostMapping("/exportar/carta-rechazo/{rutProspecto}/{solicitudAdmisionId}")
    public ResponseEntity<Resource> exportarCartaRechazo(@PathVariable String rutProspecto, @PathVariable long solicitudAdmisionId, @RequestBody @Valid BiometriaRequestDTO request) throws IOException, InvalidFormatException {

        if (!request.getResultado() && (request.getCodigo() != null || request.getDetalle() != null)) {
            throw new BadRequestException("No paso seguridad biometrica");
        }
        String filename = "carta_rechazo.pdf";
        InputStreamResource file = new InputStreamResource(exportComponent.exportarCartaRechazo(rutProspecto, solicitudAdmisionId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportar/resumen")
    public ResponseEntity<Resource> exportarResumenTmp() throws IOException {

        String filename = "resumen.pdf";
        InputStreamResource file = new InputStreamResource(exportComponent.exportarResumen());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportar/contrato")
    public ResponseEntity<Resource> exportarContratoTmp() throws IOException {

        String filename = "contrato.pdf";
        InputStreamResource file = new InputStreamResource(exportComponent.exportarContrato());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportar/anexo/cesantia")
    public ResponseEntity<Resource> exportarAnexCesantiaTmp() throws IOException {

        String filename = "anexos_cesantia.pdf";
        InputStreamResource file = new InputStreamResource(exportComponent.exportarAnexo("plantilla_anexo_cesantia.html"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

    @PostMapping("/exportar/anexo/degravamen")
    public ResponseEntity<Resource> exportarAnexoDegravamenTmp() throws IOException {

        String filename = "anexo_degravamen.pdf";
        InputStreamResource file = new InputStreamResource(exportComponent.exportarAnexo("plantilla_anexo_degravamen.html"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }

}
