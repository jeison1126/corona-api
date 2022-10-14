package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescargaDocumentoResponseDTO {
    private String nombreDocumento;
    private ByteArrayInputStream documento;
}
