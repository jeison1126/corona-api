package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReglaNegocioCompletoDTO extends ReglaNegocioBasicoDTO {

    private static final long serialVersionUID = 1L;
    private Long id;
    private boolean vigencia;
}
