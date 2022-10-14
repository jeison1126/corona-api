package corona.financiero.nmda.admision.dto.equifax;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxAdditionalAttributeRequestDTO {
    @JsonAlias("usu_actividad")
    private String usuActividad;
    @JsonAlias("usu_ingreso_minimo")
    private String usuIngresoMinimo;
    @JsonAlias("usu_total_haberes")
    private String usuTotalHaberes;
    @JsonAlias("usu_desc_legales")
    private String usuDescLegales;
    @JsonAlias("usu_otros_desc")
    private String usuOtrosDesc;
}
