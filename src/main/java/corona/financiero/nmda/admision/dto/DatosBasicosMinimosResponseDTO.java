package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosBasicosMinimosResponseDTO {

    private long prospectoId;
    private String rut;
    private String nombre;
    private String apellidoPatenro;
    private String apellidoMaterno;
}
