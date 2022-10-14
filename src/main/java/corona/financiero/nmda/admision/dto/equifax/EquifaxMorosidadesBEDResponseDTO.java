package corona.financiero.nmda.admision.dto.equifax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxMorosidadesBEDResponseDTO {

    private String codigoMoneda;
    private String fechaIngresoEFX;
    private String fechaVencimiento;
    private String justificacionDescripcion;
    private String justificacionFecha;
    private String mercadoCodigo;
    private String mercadoDescripcion;
    private String montoImpago;
    private String nombreLibrador;
    private String nombreLocalidad;
    private String nroChequeOperacion;
    private String tipoDeuda;
    private String tipoDocumento;
}
