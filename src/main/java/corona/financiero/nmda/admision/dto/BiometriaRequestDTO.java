package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiometriaRequestDTO {

    @JsonProperty("Resultado")
    private Boolean resultado;
    @JsonProperty("Codigo")
    private String codigo;
    @JsonProperty("Detalle")
    private String detalle;
    @JsonProperty("Data")
    private BiometriaDataRequestDTO data;
}
