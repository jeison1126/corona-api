package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCupoOfrecidoDTO {
	private long evaluacionProductoId;
    private String descripcionTipoProducto;
    private long cupoAprobado;
    private boolean recomendado;
}
