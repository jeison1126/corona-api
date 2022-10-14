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
@Table(name = "par_origen")
public class ParOrigenEntity {

    @Column(name = "id_origen", nullable = false)
    @Id
    private long origenId;
    @Column(name = "descripcion", length = 50, nullable = false)
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
