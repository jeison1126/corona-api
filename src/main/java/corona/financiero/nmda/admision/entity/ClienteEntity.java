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
@Table(name = "cliente")
public class ClienteEntity {

    @Column(name = "rut", length = 12, nullable = false)
    @Id
    private String rut;
    @ManyToOne(targetEntity = ProspectoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_prospecto", referencedColumnName = "id_prospecto")}, foreignKey = @ForeignKey(name = "FK_cliente_prospecto"))
    private ProspectoEntity prospectoEntity;
    @ManyToOne(targetEntity = ParNacionalidadEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_nacionalidad", referencedColumnName = "id_nacionalidad")}, foreignKey = @ForeignKey(name = "FK_cliente_par_nacionalidad"))
    private ParNacionalidadEntity parNacionalidadEntity;
    @ManyToOne(targetEntity = ParEstadoCivilEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_estado_civil", referencedColumnName = "id_estado_civil")}, foreignKey = @ForeignKey(name = "par_estado_civil_fk"))
    private ParEstadoCivilEntity parEstadoCivilEntity;
    @ManyToOne(targetEntity = ParProfesionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_profesion", referencedColumnName = "id_profesion")}, foreignKey = @ForeignKey(name = "par_profesion_fk"))
    private ParProfesionEntity parProfesionEntity;
    @ManyToOne(targetEntity = ParActividadEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_actividad", referencedColumnName = "id_actividad")}, foreignKey = @ForeignKey(name = "par_actividad_fk"))
    private ParActividadEntity parActividadEntity;
    @Column(name = "apellido_paterno", length = 50, nullable = false)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 50, nullable = false)
    private String apellidoMaterno;
    @Column(name = "nombres", length = 50, nullable = false)
    private String nombre;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @Column(name = "sexo", length = 1, nullable = false)
    private String sexo;
    @Column(name = "celular", nullable = false)
    private int movil;
    @Column(name = "email", length = 100, nullable = false)
    private String email;
    @Column(name = "calle", length = 200, nullable = false)
    private String calle;
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;
    @Column(name = "departamento", length = 10, nullable = false)
    private String departamento;
    @Column(name = "telefono_adicional")
    private Integer telefonoAdicional;
    @Column(name = "codigo_comuna", length = 10)
    private String codigoComuna;
    @Column(name = "codigo_region", length = 10)
    private String codigoRegion;
    @Column(name = "latitud")
    private Float latitud;
    @Column(name = "longitud")
    private Float longitud;
    @Column(name = "referencia", length = 100)
    private String referencia;
    @Column(name = "envio_ecc_email", nullable = false)
    private boolean envioEccEmail;
    @Column(name = "autoriza_sms", nullable = false)
    private boolean autorizaSms;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioRegistro;
    @Column(name = "fecha_ult_mod_reg")
    private LocalDateTime fechaModificacion;
    @Column(name = "usuario_ult_mod_reg", length = 20)
    private String usuarioModificacion;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
