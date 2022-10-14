package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "biometria_fase")
public class BiometriaFaseEntity implements Serializable {
    @Column(name = "id_biometria_fase", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long biometriaFaseId;
    @ManyToOne(targetEntity = BiometriaConsultaEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_biometria_consulta", referencedColumnName = "id_biometria_consulta")}, foreignKey = @ForeignKey(name = "biometria_consulta_fk"))
    private BiometriaConsultaEntity biometriaConsultaEntity;
    @ManyToOne(targetEntity = ClienteEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "rut", referencedColumnName = "rut")}, foreignKey = @ForeignKey(name = "FK_tarjeta_cliente"))
    private ClienteEntity clienteEntity;
    @ManyToOne(targetEntity = ProspectoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_prospecto", referencedColumnName = "id_prospecto")}, foreignKey = @ForeignKey(name = "FK_solicitud_admision_prospecto"))
    private ProspectoEntity prospectoEntity;
    @ManyToOne(targetEntity = ParFaseEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_fase", referencedColumnName = "id_fase")}, foreignKey = @ForeignKey(name = "par_fase_fk"))
    private ParFaseEntity parFaseEntity;
    @ManyToOne(targetEntity = ParTipoBiometriaEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_tipo_biometria", referencedColumnName = "id_tipo_biometria")}, foreignKey = @ForeignKey(name = "par_tipo_biometria_fk"))
    private ParTipoBiometriaEntity parTipoBiometriaEntity;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
