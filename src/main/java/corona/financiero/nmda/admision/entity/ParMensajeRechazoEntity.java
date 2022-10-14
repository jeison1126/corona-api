package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "par_mensaje_rechazo")
public class ParMensajeRechazoEntity {
    @Column(name = "id_mensaje_rechazo", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mensajeRechazoId;
    @Column(name = "descripcion_mensaje", length = 500, nullable = false)
    private String descripcion;
    @Column(name = "mensaje_funcional", length = 500, nullable = false)
    private String mensajeFuncional;
    @Column(name = "mensaje_carta_rechazo", length = 500)
    private String mensajeCartaRechazo;
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
