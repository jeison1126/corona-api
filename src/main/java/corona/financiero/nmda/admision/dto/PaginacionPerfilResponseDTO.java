package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginacionPerfilResponseDTO {

    private long totalPagina;
    private long pagina;
    private long totalElementos;
    private List<PerfilResponseDTO> perfiles;
}
