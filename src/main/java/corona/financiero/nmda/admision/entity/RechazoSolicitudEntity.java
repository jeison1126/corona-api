package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "rechazo_solicitud")
public class RechazoSolicitudEntity implements Serializable {
    @Column(name = "id_rechazo_solicitud", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rechazoSolicitudId;
    @ManyToOne(targetEntity = SolicitudAdmisionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")}, foreignKey = @ForeignKey(name = "solicitud_admision_fk"))
    private SolicitudAdmisionEntity solicitudAdmisionEntity;
    @ManyToOne(targetEntity = ParMotivoRechazoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_motivo_rechazo", referencedColumnName = "id_motivo_rechazo")}, foreignKey = @ForeignKey(name = "par_motivo_rechazo_fk"))
    private ParMotivoRechazoEntity parMotivoRechazoEntity;
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
}
