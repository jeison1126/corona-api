package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "camara_comercio")
public class CamaraComercioEntity {
    @Column(name = "rut", length = 20, nullable = false)
    @Id
    private String rut;
    @Column(name = "id_sucursal", nullable = false)
    private long sucursalId;
    @Column(name = "id_dicom", nullable = false)
    private Long dicomId;
    @Column(name = "cantidad_total_impagos", nullable = false)
    private Integer cantidadTotalImpagos;
    @Column(name = "monto_total_impagos", nullable = false)
    private Long montoTotalImpagos;
    @Column(name = "tipo", nullable = false)
    private String tipo;
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
