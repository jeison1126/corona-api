package corona.financiero.nmda.admision.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ReglaNegocioNotFoundException extends RuntimeException {
    public ReglaNegocioNotFoundException() {
        super("No existe la regla de negocio indicada");
    }
}

