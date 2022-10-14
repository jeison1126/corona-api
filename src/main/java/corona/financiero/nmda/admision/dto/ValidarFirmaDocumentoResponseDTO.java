package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidarFirmaDocumentoResponseDTO {

    private boolean firmado;
    private String mensajeError;
    private boolean activarExcepcion;
    private List<DocumentoDTO> documentosFirmados;
}
