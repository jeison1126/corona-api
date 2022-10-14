package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificacionBiometriaResponseDTO {

    private boolean validacionBiometrica;
    private boolean validacionExcepcion;
    private long solicitudId;
}
