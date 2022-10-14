package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioRequestDTO {
	private String rut;
    private String nombreCompleto;
    private String nombreUsuario;
    private long sucursalId;
    private String nombreSucursal;
    private long cargoId;
    private String nombreCargo;
}