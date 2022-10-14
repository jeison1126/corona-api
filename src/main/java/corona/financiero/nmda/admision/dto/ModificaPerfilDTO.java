package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificaPerfilDTO extends CrearPerfilDTO {
    private Long perfilId;
    private boolean vigencia;
}
