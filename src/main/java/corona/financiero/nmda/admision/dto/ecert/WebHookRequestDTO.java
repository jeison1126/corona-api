package corona.financiero.nmda.admision.dto.ecert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebHookRequestDTO {

    @JsonProperty("DoctoId")
    private int doctoId;
    @JsonProperty("DoctoBase64")
    private String doctoBase64;
    @JsonProperty("Firmado")
    private boolean firmado;
    @JsonProperty("RazonRechazo")
    private String razonRechazo;
}
