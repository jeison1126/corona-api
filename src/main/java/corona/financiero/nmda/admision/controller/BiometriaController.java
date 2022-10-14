package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.*;
import corona.financiero.nmda.admision.service.BiometriaService;
import corona.financiero.nmda.admision.util.Constantes;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/biometria")
@Tag(name = "Biometria", description = "APIs de biometria")
public class BiometriaController {

    @Autowired
    private BiometriaService biometriaService;

    @PostMapping("/verificacion")
    public ResponseEntity<VerificacionBiometriaResponseDTO> verificaValidacionBiometrica(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.verificaValidacionBiometrica(request), HttpStatus.OK);
    }


    @PostMapping("/activar")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoBiometrico(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_EVALUACION_SCORING), HttpStatus.OK);
    }

    @PostMapping("/activar/excepcion")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoExcepcion(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_EVALUACION_SCORING), HttpStatus.OK);
    }

    @PostMapping("/activar/carta-rechazo")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoBiometricoCartaRechazo(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometricoCartaRechazo(request, true), HttpStatus.OK);
    }

    @PostMapping("/activar/carta-rechazo/excepcion")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoBiometricoCartaRechazoExcepcion(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometricoCartaRechazo(request, false), HttpStatus.OK);
    }

    @PostMapping("/activar/firma-electronica/excepcion")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoFirmaElectronicaExcepcion(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_DATOS_CLIENTE), HttpStatus.OK);
    }

    @PostMapping("/activar/contrato-seguro")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoBiometricoContratoSeguro(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA), HttpStatus.OK);
    }

    @PostMapping("/activar/contrato-seguro/excepcion")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoContratoSeguroExcepcion(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_IMPRIMIR_ACTIVAR_TARJETA), HttpStatus.OK);
    }

    @PostMapping("/activar/impresion-tarjeta")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoBiometricoImpresionTarjeta(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, true, Constantes.FASE_FIRMA_CONTRATOS), HttpStatus.OK);
    }

    @PostMapping("/activar/impresion-tarjeta/excepcion")
    public ResponseEntity<ActivarDispositivoResponseDTO> activarDispositivoImpresionTarjetaContratoSeguroExcepcion(@RequestBody @Valid ActivarDispositivoRequestDTO request) {
        return new ResponseEntity<>(biometriaService.activarDispositivoBiometrico(request, false, Constantes.FASE_FIRMA_CONTRATOS), HttpStatus.OK);
    }

    @PostMapping("/prospecto-solicitud/{rut}/{solicitudId}")
    public ResponseEntity<BiometriaResponseDTO> registrarBiometriaCotizaciones(@PathVariable String rut, @PathVariable Long solicitudId, @RequestBody @Valid BiometriaRequestDTO request) {

        return new ResponseEntity<>(biometriaService.registrarBiometriaCotizaciones(rut, solicitudId, request), HttpStatus.OK);
    }

    @PostMapping("/excepcion/{rut}/{solicitudId}")
    public ResponseEntity<BiometriaResponseDTO> registrarBiometriaCotizacionesExcepcion(@PathVariable String rut, @PathVariable Long solicitudId, @RequestBody @Valid BiometriaRequestDTO request) {

        return new ResponseEntity<>(biometriaService.registrarBiometriaCotizacionesExcepcion(rut, solicitudId, request), HttpStatus.OK);
    }

    @PostMapping("/firma-electronica/excepcion/{rut}/{solicitudId}/{usuarioEcertId}")
    public ResponseEntity<BiometriaFirmaContratoExcepcionResponseDTO> registrarBiometriaFirmaElectronicaExcepcion(@PathVariable String rut, @PathVariable long solicitudId, @PathVariable int usuarioEcertId, @RequestBody @Valid BiometriaRequestDTO request) {

        return new ResponseEntity<>(biometriaService.registrarBiometriaFirmaElectronicaExcepcion(rut, solicitudId, usuarioEcertId, request), HttpStatus.OK);
    }

    @PostMapping("/impresion-tarjeta/{rut}/{solicitudId}/{pep}")
    public ResponseEntity<BiometriaResponseDTO> registrarBiometriaImpresionTarjeta(@PathVariable String rut, @PathVariable Long solicitudId, @RequestBody @Valid BiometriaRequestDTO request) {

        return new ResponseEntity<>(biometriaService.registrarBiometriaImpresionTarjeta(rut, solicitudId, request), HttpStatus.OK);
    }

    @PostMapping("/impresion-tarjeta/excepcion/{rut}/{solicitudId}/{pep}")
    public ResponseEntity<BiometriaResponseDTO> registrarBiometriaImpresionTarjetaExcepcion(@PathVariable String rut, @PathVariable Long solicitudId, @RequestBody @Valid BiometriaRequestDTO request) {

        return new ResponseEntity<>(biometriaService.registrarBiometriaImpresionTarjetaExcepcion(rut, solicitudId, request), HttpStatus.OK);
    }
}
