package corona.financiero.nmda.admision.dto.ecert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcertLoginResponseDTO {

    @JsonProperty("Exito")
    private boolean exito;
    @JsonProperty("Excepcion")
    private boolean excepcion;
    @JsonProperty("Descripcion")
    private String descripcion;
    @JsonProperty("ListaGenerica")
    private Object listaGenerica;
    @JsonProperty("ObjetoGenerico")
    private EcertObjetoGenericoLoginResponseDTO objetoGenerico;

}
