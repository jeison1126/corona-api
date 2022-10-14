package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "estafa")
public class EstafaEntity {
    @Column(name = "rut", length = 12, nullable = false)
    @Id
    private String rut;
    @Column(name = "celular")
    private Integer movil;
    @Column(name = "comuna")
    private String comuna;
    @Column(name = "calle")
    private String calle;
    @Column(name = "numero")
    private String numero;
    @Column(name = "fecha_captacion")
    private Date fechaCaptacion;
    @Column(name = "estado")
    private Long estadoEstafa;
    @Column(name = "fecha_carga")
    private Date fechaCarga;
    @Column(name = "fecha_seleccion")
    private Date fechaSeleccion;
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
