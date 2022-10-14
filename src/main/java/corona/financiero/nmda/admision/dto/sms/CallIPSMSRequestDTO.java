package corona.financiero.nmda.admision.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallIPSMSRequestDTO {

    private String message;
    private String dni;
}
