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
@Table(name = "par_tipo_solicitud")
public class ParTipoSolicitudEntity implements Serializable {
    @Column(name = "id_tipo_solicitud", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tipoSolicitudId;
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
    @Column(name = "tipo_impresion", nullable = false, length = 1)
    private String tipoImpresion;
    @Column(name = "codigo_bloqueo_intellect")
    private Integer codigoBloqueoIntellect;
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
