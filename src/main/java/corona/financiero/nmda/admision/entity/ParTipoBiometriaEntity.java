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
@Table(name = "par_tipo_biometria")
public class ParTipoBiometriaEntity {
    @Column(name = "id_tipo_biometria", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tipoBiometriaId;
    @Column(name = "descripcion", nullable = false, length = 50)
    private String descripcion;
}
