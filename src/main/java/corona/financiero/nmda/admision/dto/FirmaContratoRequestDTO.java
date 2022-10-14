package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirmaContratoRequestDTO extends FirmaDocumentoRequestDTO {
    private boolean pep;
}
