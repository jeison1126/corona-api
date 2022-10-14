package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoDTO {
    private long documentoId;
    private String nombreDocumento;
    private String uriDocumento;
    private String textoBoton;
}
