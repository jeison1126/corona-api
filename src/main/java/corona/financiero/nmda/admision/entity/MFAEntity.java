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
@Table(name = "mfa")
public class MFAEntity implements Serializable {
    @Column(name = "id_mfa", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mfaId;

    @ManyToOne(targetEntity = SolicitudAdmisionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")}, foreignKey = @ForeignKey(name = "solicitud_admision_fk"))
    private SolicitudAdmisionEntity solicitudAdmisionEntity;
    @Column(name = "codigo", nullable = false, length = 5)
    private String codigo;
    @Column(name = "validado", nullable = false)
    private boolean validado;
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
