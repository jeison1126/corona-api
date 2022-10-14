package corona.financiero.nmda.admision.dto.ecert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcertLoginRequestDTO {

    @JsonProperty("UserName")
    private String userName;
    @JsonProperty("Password")
    private String password;
}
