package corona.financiero.nmda.admision.dto.cyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CYCDatosDemograficosDTO {

    private String codigoPais;
    private String calle;
    private String casilla;
    private String numero;
    private String region;
    private String ciudad;
    private String comuna;
    private String comentarioAdicional;
}
