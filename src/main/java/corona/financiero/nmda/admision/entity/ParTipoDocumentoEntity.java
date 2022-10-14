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
@Table(name = "par_tipo_documento")
public class ParTipoDocumentoEntity implements Serializable {
    @Column(name = "id_tipo_documento", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tipoDocumentoId;
    @Column(name = "tipo_documento", nullable = false, length = 10)
    private String tipoDocumento;
    @Column(name = "descripcion_documento", nullable = false, length = 100)
    private String descripcion;
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
