package corona.financiero.nmda.admision.dto.cyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CYCInstrumentoResponseDTO {
    private String codigo;
    private List<CYCAtributoResponseDTO> atributo;
}
