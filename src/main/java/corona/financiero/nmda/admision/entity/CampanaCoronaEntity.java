package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "campana_corona")
public class CampanaCoronaEntity implements Serializable {
    @Column(name = "id_campana_corona", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long campanaCoronaId;
    @ManyToOne(targetEntity = CabeceraCampanaEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_cabecera_campana", referencedColumnName = "id_cabecera_campana")}, foreignKey = @ForeignKey(name = "cabecera_campana_fk"))
    private CabeceraCampanaEntity cabeceraCampanaEntity;
    @Column(name = "rut", nullable = false, length = 12)
    private String rut;
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name = "apellido_paterno", length = 50, nullable = false)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 50, nullable = false)
    private String apellidoMaterno;
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
    @Column(name = "cupo_asignado")
    private Long cupoAsignado;
    @Column(name = "producto_predeterminado", length = 100, nullable = false)
    private String productoPredeterminado;
    @Column(name = "es_funcionario", nullable = false)
    private boolean esFuncionario;
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
