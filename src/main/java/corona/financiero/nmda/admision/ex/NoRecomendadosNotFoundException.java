package corona.financiero.nmda.admision.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoRecomendadosNotFoundException extends RuntimeException {
    public NoRecomendadosNotFoundException() {
        super("No existe el registro indicado");
    }
}
