package corona.financiero.nmda.admision.dto.cyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CYCDatosCreacionCuentaDTO {

    private String rutUsuario;
    private String numeroOperacion;
    private String numeroTarjeta;
    private String tipoCliente;
    private String fecha;
    private String hora;
    private String diaPago;
    private String codigoEstadoCuenta;
    private String cupoAsignado;
    private CYCDatosPersonalesDTO datosPersonales;
    private CYCDatosDemograficosDTO datosDemograficos;
}
