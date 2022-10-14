package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeRechazoDTO {

    private long causalRechazoId;
    private String descripcion;
    private String mensajeFuncional;
    private boolean vigencia;
}
