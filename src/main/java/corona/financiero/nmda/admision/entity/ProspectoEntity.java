package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "prospecto")
public class ProspectoEntity {

    @Column(name = "id_prospecto", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long prospectoId;
    @Column(name = "rut", nullable = false, length = 12)
    private String rut;
    @Column(name = "nombres", length = 50)
    private String nombres;
    @Column(name = "apellido_paterno", length = 50)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;
    @Column(name = "celular", nullable = false)
    private long movil;
    @Column(name = "email", length = 100, nullable = false)
    private String email;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column(name = "fecha_ing_reg")
    private LocalDateTime fechaIngreso;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioIngreso;
    @Column(name = "fecha_ult_mod_reg")
    private LocalDateTime fechaModificacion;
    @Column(name = "usuario_ult_mod_reg", length = 20)
    private String usuarioModificacion;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
