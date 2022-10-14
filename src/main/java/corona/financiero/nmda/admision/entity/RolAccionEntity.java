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
@Table(name = "rol_accion", uniqueConstraints = {@UniqueConstraint(name = "uk_rol_accion", columnNames = {"id_rol", "id_accion"})})
public class RolAccionEntity implements Serializable {
    @Column(name = "id_rol_accion", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rolAccionId;

    @ManyToOne(targetEntity = AccionEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_accion", referencedColumnName = "id_accion", nullable = false)}, foreignKey = @ForeignKey(name = "accion_fk"))
    private AccionEntity accionEntity;

    @ManyToOne(targetEntity = RolEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_rol", referencedColumnName = "id_rol", nullable = false)}, foreignKey = @ForeignKey(name = "rol_fk"))
    private RolEntity rolEntity;
}
