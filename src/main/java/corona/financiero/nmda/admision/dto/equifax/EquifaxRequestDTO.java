package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxRequestDTO {

    private String rut;
    private String numeroSerie;
    private String iCom;
}
