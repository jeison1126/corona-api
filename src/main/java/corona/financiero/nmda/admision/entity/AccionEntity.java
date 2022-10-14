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
@Table(name = "accion", uniqueConstraints = {@UniqueConstraint(name = "uk_nombre_accion", columnNames = {"nombre"})})
public class AccionEntity implements Serializable {
    @Column(name = "id_accion", nullable = false)
    @Id
    private long accionId;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
}
