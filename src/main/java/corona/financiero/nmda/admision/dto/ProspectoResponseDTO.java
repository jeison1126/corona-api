package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProspectoResponseDTO extends ProspectoDTO {

    private long prospectoId;
    private Long solicitudId;
}
