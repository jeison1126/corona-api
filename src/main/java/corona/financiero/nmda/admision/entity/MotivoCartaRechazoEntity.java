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
@Table(name = "motivo_carta_rechazo")
public class MotivoCartaRechazoEntity implements Serializable {
    @Column(name = "id_motivo_carta_rechazo", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long motivoCartarechazoId;

    @ManyToOne(targetEntity = SolicitudAdmisionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")}, foreignKey = @ForeignKey(name = "solicitud_admision_fk"))
    private SolicitudAdmisionEntity solicitudAdmisionEntity;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioRegistro;
}
