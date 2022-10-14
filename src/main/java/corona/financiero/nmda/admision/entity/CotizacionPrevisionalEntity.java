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
@Table(name = "cotizacion_previsional")
public class CotizacionPrevisionalEntity implements Serializable {
    @Column(name = "id_cotizacion_previsional", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cotizacionPrevisionalId;
    @ManyToOne(targetEntity = ProspectoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_prospecto", referencedColumnName = "id_prospecto")}, foreignKey = @ForeignKey(name = "FK_evaluacion_imposicion_prospecto"))
    private ProspectoEntity prospectoEntity;
    @ManyToOne(targetEntity = ParRespuestaCotizacionPrevisionalEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_codigo_servicio", referencedColumnName = "id_codigo_respuesta")}, foreignKey = @ForeignKey(name = "par_respuesta_cotizacion_previsional_fk1"))
    private ParRespuestaCotizacionPrevisionalEntity codigoServicio;
    @ManyToOne(targetEntity = ParRespuestaCotizacionPrevisionalEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_codigo_aut", referencedColumnName = "id_codigo_respuesta")}, foreignKey = @ForeignKey(name = "par_respuesta_cotizacion_previsional_fk2"))
    private ParRespuestaCotizacionPrevisionalEntity codigoAut;
    @ManyToOne(targetEntity = ParRespuestaCotizacionPrevisionalEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_codigo_detalle", referencedColumnName = "id_codigo_respuesta")}, foreignKey = @ForeignKey(name = "par_respuesta_cotizacion_previsional_fk"))
    private ParRespuestaCotizacionPrevisionalEntity codigoDetalle;
    @Column(name = "folio", nullable = false, length = 50)
    private String folio;
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;
    @Column(name = "codigo_autorizacion", nullable = false, length = 50)
    private String codigoAutorizacion;
    @Column(name = "tipo_servicio", nullable = false, length = 4)
    private String tipoServicio;
    @Column(name = "firma", length = 200)
    private String firma;
    @Column(name = "llave", length = 200)
    private String llave;
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
