package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidacionMorosidadDTO {

    private Long creditosDirectosImpagos30a90Dias;
    private Long creditosDirectosImpagos180a3anios;
    private Long creditosDirectosImpagosIgualMayor3anios;
    private Long creditosDirectosImpagos90a180dias;


}
