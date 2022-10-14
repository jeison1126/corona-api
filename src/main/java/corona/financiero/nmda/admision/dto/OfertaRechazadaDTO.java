package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaRechazadaDTO {

    private Long solicitudId;
    private String rut;
    private Long idMotivoRechazo;
    private String otroMotivo;
}
