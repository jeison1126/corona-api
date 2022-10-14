package corona.financiero.nmda.admision.dto.notificacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailParametersDTO {
    private String param;
    private String value;
}
