package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiometriaFirmaContratoExcepcionResponseDTO extends BiometriaResponseDTO {

    private List<DocumentoDTO> documentos;
}
