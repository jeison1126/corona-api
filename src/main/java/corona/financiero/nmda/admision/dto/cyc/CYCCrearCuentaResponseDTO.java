package corona.financiero.nmda.admision.dto.cyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CYCCrearCuentaResponseDTO {
    private String codigo;
    private String descripcion;
    private CYCInstrumentoResponseDTO instrumento;

}
