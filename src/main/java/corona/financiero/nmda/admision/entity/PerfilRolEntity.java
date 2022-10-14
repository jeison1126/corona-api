package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "perfil_rol", uniqueConstraints = {@UniqueConstraint(name = "uk_perfil_rol", columnNames = {"id_perfil", "id_rol"})})
public class PerfilRolEntity implements Serializable {
    @Column(name = "id_perfil_rol", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perfilRolId;

    @ManyToOne(targetEntity = PerfilEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")}, foreignKey = @ForeignKey(name = "perfil_fk"))
    private PerfilEntity perfilEntity;

    @ManyToOne(targetEntity = RolEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_rol", referencedColumnName = "id_rol")}, foreignKey = @ForeignKey(name = "rol_fk"))
    private RolEntity rolEntity;
}
