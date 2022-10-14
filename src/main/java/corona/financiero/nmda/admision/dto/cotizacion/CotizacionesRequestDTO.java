package corona.financiero.nmda.admision.dto.cotizacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CotizacionesRequestDTO {

    private String rut;
    private int cantidad;
}
