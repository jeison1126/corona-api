package corona.financiero.nmda.admision.controller;

import corona.financiero.nmda.admision.dto.MFARequestDTO;
import corona.financiero.nmda.admision.dto.ValidarMFARequestDTO;
import corona.financiero.nmda.admision.service.MFAService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/mfa")
@Tag(name = "MFA", description = "APIs de MFA")
public class MFAController {
    @Autowired
    private MFAService mfaService;

    @PostMapping("/")
    public ResponseEntity<Void> enviarSMS(@RequestBody @Valid MFARequestDTO request) {
        mfaService.validacionPreviaDesdeController(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validar")
    public ResponseEntity<Void> validarSMS(@RequestBody @Valid ValidarMFARequestDTO request) {
        mfaService.validarSMS(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verificacion")
    public ResponseEntity<Void> verificaValidacionMFA(@RequestBody @Valid MFARequestDTO request){
        mfaService.verificaValidacionMFA(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
