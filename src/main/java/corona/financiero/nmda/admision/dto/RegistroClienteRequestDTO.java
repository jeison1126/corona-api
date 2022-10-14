package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroClienteRequestDTO {

    //datos personales
    private String rut;
    private String telefonoAdicional;//opcional
    private long nacionalidad;
    private long estadoCivil;
    private long profesion;
    private long actividad;

    //datos direccion
    private String codigoRegion;
    private String codigoComuna;
    private String calle;
    private String numero;
    private String departamento;//opcional
    private String referencias;//opcional
    private Float latitud;//opcional
    private Float longitud;//opcional

    //envio estdo cuenta y alertas a sms
    private boolean envioEcc;
    private boolean envioSMS;

}
