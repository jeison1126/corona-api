package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "consulta_scoring_persona")
public class ConsultaScoringPersonaEntity {

    @Column(name = "id_consulta_scoring_persona", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long consultaScoringPersonaId;
    @ManyToOne(targetEntity = ConsultaScoringEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_consulta_scoring", referencedColumnName = "id_consulta_scoring")}, foreignKey = @ForeignKey(name = "consulta_scoring_fk"))
    private ConsultaScoringEntity consultaScoringEntity;
    @Column(name = "apellido_paterno", length = 50)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;
    @Column(name = "nombres", length = 50)
    private String nombres;
    @Column(name = "edad", length = 3)
    private String edad;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column(name = "nombre_completo", length = 100)
    private String nombreCompleto;
}
