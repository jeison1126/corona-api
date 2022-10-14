package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxMercadosReponseDTO {

    private String codigo;
    private String descripcion;
    private String montoImpagos;
    private String nroImpagos;
}
