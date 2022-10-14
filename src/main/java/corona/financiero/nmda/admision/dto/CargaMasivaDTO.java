package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargaMasivaDTO {
    private int linea;
    private CargaMasivaDatosDTO datos;
    private String Errores;
}
