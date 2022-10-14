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
@Table(name = "cabecera_campana")
public class CabeceraCampanaEntity {
    @Column(name = "id_cabecera_campana", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cabeceraCampanaId;
    @Column(name = "nombre_archivo", length = 100, nullable = false)
    private String nombreArchivo;
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    @Column(name = "fecha_termino", nullable = false)
    private LocalDate fechaTermino;
    @Column(name = "cantidad_registros")
    private Integer cantidadRegistros;
    @Column(name = "registros_procesados")
    private Integer registrosProcesados;
    @Column(name = "cantidad_errores")
    private Integer cantidadErrores;
    @Column(name = "errores", length = 500)
    private String errores;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioRegistro;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
