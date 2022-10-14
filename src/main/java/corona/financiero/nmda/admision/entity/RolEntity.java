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
@Table(name = "rol", uniqueConstraints = {@UniqueConstraint(name = "uk_nombre_rol", columnNames = {"nombre"})})
public class RolEntity {
    @Column(name = "id_rol", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rolId;
    @ManyToOne(targetEntity = ModuloEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_modulo", referencedColumnName = "id_modulo")}, foreignKey = @ForeignKey(name = "modulo_fk"))
    private ModuloEntity moduloEntity;
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
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
