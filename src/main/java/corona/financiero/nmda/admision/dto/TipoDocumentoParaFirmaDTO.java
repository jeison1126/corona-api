package corona.financiero.nmda.admision.dto;

import corona.financiero.nmda.admision.dto.ecert.EcertSubirDocumentoRequestDTO;
import corona.financiero.nmda.admision.dto.ecert.EcertSubirDocumentoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoDocumentoParaFirmaDTO {
    private long tipoDocumento;
    private EcertSubirDocumentoRequestDTO ecertSubirDocumentoRequestDTO;
    private EcertSubirDocumentoResponseDTO ecertSubirDocumentoResponseDTO;
}
