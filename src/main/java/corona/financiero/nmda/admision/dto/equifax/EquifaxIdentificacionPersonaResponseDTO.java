package corona.financiero.nmda.admision.dto.equifax;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquifaxIdentificacionPersonaResponseDTO {
    @JsonAlias("ApellidoPaterno")
    private String apellidoPaterno;
    @JsonAlias("ApellidoMaterno")
    private String apellidoMaterno;
    @JsonAlias("Nombres")
    private String nombres;
    @JsonAlias("codigoOficio")
    private String codigoOficio;
    @JsonAlias("codigoProfesion")
    private String codigoProfesion;
    @JsonAlias("descripcionActividadEconomica")
    private String descripcionActividadEconomica;
    @JsonAlias("descripcionOficio")
    private String descripcionOficio;
    @JsonAlias("descripcionProfesion")
    private String descripcionProfesion;
    @JsonAlias("edad")
    private String edad;
    @JsonAlias("estadoCivil")
    private String estadoCivil;
    @JsonAlias("fechaNacimiento")
    private String fechaNacimiento;
    @JsonAlias("nacionalidad")
    private String nacionalidad;
    @JsonAlias("nombre")
    private String nombre;
    @JsonAlias("sexo")
    private String sexo;
    @JsonAlias("tipoNacionalidad")
    private String tipoNacionalidad;
}
