package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "cotizacion_previsional_detalle")
public class CotizacionPrevisionalDetalleEntity implements Serializable {
    @Column(name = "id_cotizacion_previsional_detalle", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cotizacionPrevisionalDetalleId;
    @ManyToOne(targetEntity = CotizacionPrevisionalEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_cotizacion_previsional", referencedColumnName = "id_cotizacion_previsional")}, foreignKey = @ForeignKey(name = "FK_evaluacion_imposicion_det_evaluacion_imposicion"))
    private CotizacionPrevisionalEntity cotizacionPrevisionalEntity;
    @Column(name = "periodo", nullable = false, length = 10)
    private String periodo;
    @Column(name = "remuneracion_imponible", nullable = false)
    private long remuneracionImponible;
    @Column(name = "monto", nullable = false)
    private long monto;
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;
    @Column(name = "tipo_movimiento", nullable = false, length = 255)
    private String tipoMovimiento;
    @Column(name = "rut_empleador", nullable = false, length = 12)
    private String rutEmpleador;
    @Column(name = "afp", nullable = false, length = 50)
    private String afp;
}
