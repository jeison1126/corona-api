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
@Table(name = "par_tipo_producto")
public class ParTipoProductoEntity implements Serializable {
    @Column(name = "id_tipo_producto", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tipoProductoId;
    @Column(name = "descripcion_tipo_producto", nullable = false, length = 200)
    private String descripcion;
    @Column(name = "rango_min", nullable = false)
    private int rangoMin;
    @Column(name = "rango_max", nullable = false)
    private int rangoMax;
    @Column(name = "tasa_interes", nullable = false)
    private float tasaInteres;
    @Column(name = "costo_mantencion", nullable = false)
    private float costoMantencion;
    @Column(name = "cortesia", nullable = false)
    private boolean cortesia;
    @Column(name = "cupo_cortesia")
    private Long cupoCortesia;
    @Column(name = "caracteristicas", length = 1000)
    private String caracteristicas;
    @Column(name = "beneficios", length = 1000)
    private String beneficios;
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
