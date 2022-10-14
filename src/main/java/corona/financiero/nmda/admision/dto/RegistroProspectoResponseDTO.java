package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroProspectoResponseDTO {
    private long solicitudId;
    private boolean exito;
    private String mensaje;
}
