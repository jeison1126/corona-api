package corona.financiero.nmda.admision.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallIPSMSResponseDTO {

    private boolean status;
    private int code;
    private String message;
    private long id;
}
