package corona.financiero.nmda.admision.dto.cotizacion;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CotizacionDetalleResponseDTO {
    @JsonAlias("@mes")
    private String mes;
    @JsonAlias("@remuneracionimponible")
    private String remuneracionImponible;
    @JsonAlias("@monto")
    private String monto;
    @JsonAlias("@fechapago")
    private String fechaPago;
    @JsonAlias("@tipomovimiento")
    private String tipoMovimiento;
    @JsonAlias("@rutempleador")
    private String rutEmpleador;
    @JsonAlias("@afp")
    private String afp;
}
