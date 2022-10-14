package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiometriaDataRequestDTO {
    @JsonProperty("Cedula_Apellido")
    private String apellido;
    @JsonProperty("Cedula_Rut")
    private String rut;
    @JsonProperty("Cedula_Serie")
    private String serie;
    @JsonProperty("Cedula_Tipo")
    private String tipo;
    @JsonProperty("Cedula_Vencimiento")
    private String vencimiento;
    @JsonProperty("CedulaHuellaCoinciden")
    private Boolean huellaCoincide;
    @JsonProperty("IDTransaccion")
    private String transaccion;
}
