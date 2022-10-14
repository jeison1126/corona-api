package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "regla_negocio")
public class ReglaNegocioEntity {

    @Column(name = "id_regla", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = ParMensajeRechazoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_mensaje_rechazo", referencedColumnName = "id_mensaje_rechazo")}, foreignKey = @ForeignKey(name = "par_mensaje_rechazo_fk"))
    private ParMensajeRechazoEntity parMensajeRechazoEntity;

    @ManyToOne(targetEntity = ParFaseEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_fase", referencedColumnName = "id_fase")}, foreignKey = @ForeignKey(name = "par_fase_fk"))
    private ParFaseEntity parFaseEntity;

    @Column(name = "descripcion_regla", length = 500, nullable = false)
    private String descripcion;

    @Column(name = "valor_regla", length = 50, nullable = false)
    private String valor;

    @Column(name = "fecha_inicio_vigencia")
    private Date fechaInicioVigencia;

    @Column(name = "fecha_fin_vigencia")
    private Date fechaFinVigencia;

    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "usuario_ing_reg", length = 12, nullable = false)
    private String usuarioIngreso;

    @Column(name = "fecha_ult_mod_reg")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_ult_mod_reg", length = 12)
    private String usuarioModificacion;

    @Column(name = "editable", nullable = false)
    private boolean editable;

    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}