package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirmaDocumentoResponseDTO {
    private String urlLoginEcert;
    private Integer usuarioEcertId;
}
