package corona.financiero.nmda.admision.dto.cyc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CYCDatosPersonalesDTO {

    private String rutCliente;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private String estadoCivil;
    private String sexo;
    private String profesion;
    private String renta;
    private String fechaNacimiento;
    private String nacionalidad;
    private String condicionLaboral;
    private String numeroTelefono;
    private String email;
}
