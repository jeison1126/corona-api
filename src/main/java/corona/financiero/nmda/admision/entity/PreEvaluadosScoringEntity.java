package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "pre_evaluados_scoring")
public class PreEvaluadosScoringEntity {
    @Column(name = "rut", nullable = false, length = 12)
    @Id
    private String rut;
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    @Column(name = "apellido_paterno", length = 100, nullable = false)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 100, nullable = false)
    private String apellidoMaterno;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column(name = "edad", nullable = false)
    private Integer edad;
    @Column(name = "score")
    private Integer score;
    @Column(name = "renta_asignada")
    private Long rentaAsignada;
    @Column(name = "cupo_asignado")
    private Long cupoAsignado;
    @Column(name = "celular")
    private Long celular;
    @Column(name = "sexo", length = 1)
    private String sexo;
    @Column(name = "fecha_carga")
    private LocalDate fechaCarga;
    @Column(name = "estado", length = 50)
    private String estado;
    @Column(name = "fecha_admision")
    private LocalDate fechaAdmision;
    @Column(name = "fecha_primera_compra")
    private LocalDate fechaPrimeraCompra;
    @Column(name = "fecha_activacion_tarjeta")
    private LocalDate fechaActivacionTarjeta;
    @Column(name = "id_base", length = 200)
    private String idBase;
    @Column(name = "fecha_base")
    private LocalDate fechaBase;
    @Column(name = "fecha_ult_envio_consolidacion")
    private LocalDate fechaConsolidacion;
    @Column(name = "tipo_admision")
    private Integer tipoAdmision;
    @Column(name = "estado_activacion")
    private Integer estadoActivacion;
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
