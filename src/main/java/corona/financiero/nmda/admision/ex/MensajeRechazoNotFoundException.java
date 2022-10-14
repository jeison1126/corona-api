package corona.financiero.nmda.admision.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MensajeRechazoNotFoundException extends RuntimeException {
    public MensajeRechazoNotFoundException() {
        super("No existe el mensaje de rechazo indicado");
    }
}
