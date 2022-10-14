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
@Table(name = "funcionarios")
public class FuncionariosEntity {
    @Column(name = "rut", length = 12, nullable = false)
    @Id
    private String rut;
    @Column(name = "nombre_completo", length = 100, nullable = false)
    private String nombreCompleto;
    @Column(name = "nombre_usuario", length = 50, nullable = false)
    private String nombreUsuario;
    @Column(name = "id_sucursal")
    private long sucursalId;
    @Column(name = "nombre_sucursal", length = 50, nullable = false)
    private String nombreSucursal;
    @Column(name = "id_cargo")
    private long cargoId;
    @Column(name = "nombre_cargo", length = 50, nullable = false)
    private String nombreCargo;
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
