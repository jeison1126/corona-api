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
@Table(name = "tarjeta")
public class TarjetaEntity {

    @Column(name = "id_tarjeta", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tarjetaId;
    @ManyToOne(targetEntity = EvaluacionProductoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_evaluacion_producto", referencedColumnName = "id_evaluacion_producto")}, foreignKey = @ForeignKey(name = "evaluacion_producto_fk"))
    private EvaluacionProductoEntity evaluacionProductoEntity;
    @ManyToOne(targetEntity = ClienteEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "rut", referencedColumnName = "rut")}, foreignKey = @ForeignKey(name = "FK_tarjeta_cliente"))
    private ClienteEntity clienteEntity;
    @Column(name = "numero_tarjeta")
    private Long numeroTarjeta;
    @Column(name = "dia_pago", nullable = false)
    private int diaPago;
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    @Column(name = "cupo_aprobado", nullable = false)
    private long cupoAprobado;
    @Column(name = "cupo_disponible")
    private Long cupoDisponible;
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
