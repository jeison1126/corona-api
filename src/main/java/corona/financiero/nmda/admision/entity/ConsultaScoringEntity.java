package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "consulta_scoring")
public class ConsultaScoringEntity {

    @Column(name = "id_consulta_scoring", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long consultaScoringId;
    @ManyToOne(targetEntity = ProspectoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_prospecto", referencedColumnName = "id_prospecto")}, foreignKey = @ForeignKey(name = "FK_evaluacion_scoring_prospecto"))
    private ProspectoEntity prospectoEntity;
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;
    @Column(name = "score")
    private Long score;
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
