package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaAceptadaDTO {
    private String rut;
    private Long evaluacionProductoId;
    private Long diaPagoId;
}
