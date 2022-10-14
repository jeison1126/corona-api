package corona.financiero.nmda.admision.dto;

import corona.financiero.nmda.admision.util.EnumOrigenSolcitudAdmision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaniaResponseDTO {

    private boolean campania;
    private EnumOrigenSolcitudAdmision origen;
}
