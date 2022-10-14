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
@Table(name = "par_estado_civil")
public class ParEstadoCivilEntity {

    @Column(name = "id_estado_civil", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long estadoCivilId;
    @Column(name = "descripcion_estado_civil", length = 200, nullable = false)
    private String descripcion;
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
