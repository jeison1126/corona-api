package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "biometria_consulta")
public class BiometriaConsultaEntity implements Serializable {
    @Column(name = "id_biometria_consulta", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long biometriaConsultaId;@ManyToOne(targetEntity = SolicitudAdmisionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")}, foreignKey = @ForeignKey(name = "solicitud_admision_fk"))
    private SolicitudAdmisionEntity solicitudAdmisionEntity;
    @Column(name = "resultado")
    private Boolean resultado;
    @Column(name = "codigo", length = 130)
    private String codigo;
    @Column(name = "detalle", length = 200)
    private String detalle;
    @Column(name = "apellido", length = 100)
    private String apellido;
    @Column(name = "rut", length = 12)
    private String rut;
    @Column(name = "serie", length = 50)
    private String serie;
    @Column(name = "tipo", length = 50)
    private String tipo;
    @Column(name = "vencimiento")
    private LocalDate vencimiento;
    @Column(name = "huella_coincide")
    private Boolean huellaCoincide;
    @Column(name = "id_transaccion", length = 200)
    private String transaccionId;
    @Column(name = "estado_consulta", nullable = false)
    private int estadoConsulta;
    @Column(name = "excepcion", nullable = false)
    private boolean excepcion;
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
