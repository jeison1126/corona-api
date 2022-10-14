package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxMorosidadesIcomResponseDTO {

    private String codigoMoneda;
    private String fechaVencimiento;
    private String mercadoCodigo;
    private String mercadoDescripcion;
    private String montoImpago;
    private String nombreLibrador;
    private String nombreLocalidad;
    private String nroChequeOperacion;
    private String tipoCredito;
    private String tipoDeuda;
    private String tipoDocumento;
    private String tipoMotivo;
}
