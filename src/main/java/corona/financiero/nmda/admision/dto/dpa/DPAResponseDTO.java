package corona.financiero.nmda.admision.dto.dpa;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DPAResponseDTO {

    private String codigo;
    private String tipo;
    private String nombre;
    private float lat;
    private float lng;
    private String url;
    @JsonAlias("codigo_padre")
    private String codigoPadre;
}
