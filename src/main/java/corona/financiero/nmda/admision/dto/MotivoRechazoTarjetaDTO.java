package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotivoRechazoTarjetaDTO {

    private long motivoRechazoId;
    private String descripcion;
    
}
