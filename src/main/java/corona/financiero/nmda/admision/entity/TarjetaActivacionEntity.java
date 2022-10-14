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
@Table(name = "tarjeta_activacion")
public class TarjetaActivacionEntity {

    @Column(name = "id_activacion", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long activacionId;
    @ManyToOne(targetEntity = TarjetaEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_tarjeta", referencedColumnName = "id_tarjeta")}, foreignKey = @ForeignKey(name = "FK_tarjeta_activacion_tarjeta"))
    private TarjetaEntity tarjetaEntity;
    @Column(name = "fecha_activacion")
    private LocalDate fechaActivacion;
    @Column(name = "fecha_ing_reg")
    private LocalDateTime fechaIngreso;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioIngreso;
    @Column(name = "fecha_ult_mod_reg")
    private LocalDateTime fechaModificacion;
    @Column(name = "usuario_ult_mod_reg", length = 20)
    private String usuarioModificacion;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
