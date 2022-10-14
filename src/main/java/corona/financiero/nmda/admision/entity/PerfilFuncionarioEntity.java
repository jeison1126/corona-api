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
@Table(name = "perfil_funcionario")
public class PerfilFuncionarioEntity implements Serializable {
    @Column(name = "id_perfil_funcionario", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perfilFuncionarioId;

    @ManyToOne(targetEntity = FuncionariosEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "rut", referencedColumnName = "rut")}, foreignKey = @ForeignKey(name = "funcionarios_fk"))
    private FuncionariosEntity funcionariosEntity;

    @ManyToOne(targetEntity = PerfilEntity.class, fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")}, foreignKey = @ForeignKey(name = "perfil_fk"))
    private PerfilEntity perfilEntity;
}
