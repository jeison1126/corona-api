package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "modulo", uniqueConstraints = {@UniqueConstraint(name = "uk_nombre_modulo", columnNames = {"nombre"})})
public class ModuloEntity {
    @Column(name = "id_modulo", nullable = false)
    @Id
    private long moduloId;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;

}
