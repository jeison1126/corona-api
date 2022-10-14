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
@Table(name = "solicitud_admision")
public class SolicitudAdmisionEntity {

    @Column(name = "id_solicitud", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long solicitudId;
    @ManyToOne(targetEntity = ProspectoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_prospecto", referencedColumnName = "id_prospecto")}, foreignKey = @ForeignKey(name = "FK_solicitud_admision_prospecto"))
    private ProspectoEntity prospectoEntity;
    @ManyToOne(targetEntity = ParCanalEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_canal", referencedColumnName = "id_canal")}, foreignKey = @ForeignKey(name = "par_canal_fk"))
    private ParCanalEntity parCanalEntity;
    @ManyToOne(targetEntity = ParEstadoSolicitudEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_estado_solicitud", referencedColumnName = "id_estado_solicitud")}, foreignKey = @ForeignKey(name = "par_estado_solicitud_fk"))
    private ParEstadoSolicitudEntity parEstadoSolicitudEntity;
    @ManyToOne(targetEntity = ParOrigenEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_origen", referencedColumnName = "id_origen")}, foreignKey = @ForeignKey(name = "par_origen_fk"))
    private ParOrigenEntity parOrigenEntity;
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;
    @Column(name = "id_sucursal", nullable = false)
    private long sucursalId;
    @Column(name = "origen_operacion", length = 10)
    private String origenOperacion;
    @Column(name = "observacion", length = 100)
    private String observacion;
    @Column(name = "pep", nullable = false)
    private boolean pep;
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
