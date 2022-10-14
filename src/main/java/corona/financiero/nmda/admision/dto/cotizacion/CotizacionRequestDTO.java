package corona.financiero.nmda.admision.dto.cotizacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CotizacionRequestDTO {

    private String rut;
    private String codAutoriza;
    private String tipoServicio;
}
