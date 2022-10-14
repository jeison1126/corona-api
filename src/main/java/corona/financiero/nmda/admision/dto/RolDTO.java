package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolDTO {

    private long rolId;
    private String nombre;
    private Long moduloId;
    private List<Long> acciones;
    private boolean vigencia;
}
