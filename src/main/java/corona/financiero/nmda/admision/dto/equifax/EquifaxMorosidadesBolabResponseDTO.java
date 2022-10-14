package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxMorosidadesBolabResponseDTO {

    private String anoResolucion;
    private String fechaBoletin;
    private String montoDeuda;
    private String motivoInfraccion;
    private String nombreInstitucion;
    private String nroBoletin;
    private String nroCotizacionesAdeudadas;
    private String nroImpeccion;
    private String nroMesesAdeudados;
    private String nroResolucion;
    private String pagBoletin;
    private String regionImpeccion;
    private String tipoInfraccion;
}
