package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "consulta_scoring_registro_morosidad_protesto")
public class ConsultaScoringRegistroMorosidadProtestoEntity {

    @Column(name = "id_registro_morosidad_protesto", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String registroMorosidadProtestoId;
    @ManyToOne(targetEntity = ConsultaScoringEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_consulta_scoring", referencedColumnName = "id_consulta_scoring")}, foreignKey = @ForeignKey(name = "consulta_scoring_fk"))
    private ConsultaScoringEntity consultaScoringEntity;
    @Column(name = "cant_doc_boletin_protesto_impagos", length = 10)
    private String cantidadDocumentoBoletinProtestoImpagos;
    @Column(name = "cant_doc_icom", length = 10)
    private String cantidadDocumentoIcom;
    @Column(name = "cant_impagos_informados", length = 10)
    private String cantidadImpagosInformados;
    @Column(name = "cant_morosos_comercio", length = 10)
    private String cantidadMorososComercio;
    @Column(name = "cant_multas_infrac_laboral_previsional", length = 10)
    private String cantidadMultasInfgracLaboralPrevisional;
    @Column(name = "monto_total_impago", length = 20)
    private String montoTotalImpago;
    @Column(name = "bed_codigo_moneda", length = 5)
    private String bedCodigoMoneda;
    @Column(name = "bed_fecha_ingreso_efx")
    private LocalDate bedFechaIngresoEfx;
    @Column(name = "bed_fecha_vencimiento")
    private LocalDate bedFechaVencimiento;
    @Column(name = "bed_justificacion_descripcion", length = 500)
    private String bedJustificacionDescripcion;
    @Column(name = "bed_justificacion_fecha")
    private LocalDate bedJustificacionFecha;
    @Column(name = "bed_mercado_codigo", length = 10)
    private String bedMercadoCodigo;
    @Column(name = "bed_mercado_descripcion", length = 500)
    private String bedMercadoDescripcion;
    @Column(name = "bed_monto_impago", length = 20)
    private String bedMontoImpago;
    @Column(name = "bed_nombre_librador", length = 500)
    private String bedNombreLibrador;
    @Column(name = "bed_nombre_localidad", length = 500)
    private String bedNombreLocalidad;
    @Column(name = "bed_nro_cheque_operacion", length = 20)
    private String bedNroChequeOperacion;
    @Column(name = "bed_tipo_deuda", length = 20)
    private String bedTipoDeuda;
    @Column(name = "bed_tipo_documento", length = 3)
    private String bedTipoDocumento;
    @Column(name = "bolcom_codigo_moneda", length = 3)
    private String bolcomCodigoMoneda;
    @Column(name = "bolcom_fecha_ingreso")
    private LocalDate bolcomFechaIngreso;
    @Column(name = "bolcom_fecha_vencimiento")
    private LocalDate bolcomFechaVencimiento;
    @Column(name = "bolcom_justificacion_descripcion", length = 500)
    private String bolcomJustificacionDescripcion;
    @Column(name = "bolcom_justificacion_fecha")
    private LocalDate bolcomJustificacionFecha;
    @Column(name = "bolcom_mercado_codigo", length = 10)
    private String bolcomMercadoCodigo;
    @Column(name = "bolcom_mercado_descripcion", length = 500)
    private String bolcomMercadoDescripcion;
    @Column(name = "bolcom_monto_impago", length = 20)
    private String bolcomMontoImpago;
    @Column(name = "bolcom_nombre_librador", length = 100)
    private String bolcomNombreLibrador;
    @Column(name = "bolcom_nombre_localidad", length = 100)
    private String bolcomNombreLocalidad;
    @Column(name = "bolcom_nombre_notario", length = 500)
    private String bolcomNombreNotario;
    @Column(name = "bolcom_nro_boletin", length = 10)
    private String bolcomNroBoletin;
    @Column(name = "bolcom_nro_cheque_operacion", length = 20)
    private String bolcomNroChequeOperacion;
    @Column(name = "bolcom_tipo_credito", length = 100)
    private String bolcomTipoCredito;
    @Column(name = "bolcom_tipo_deuda", length = 20)
    private String bolcomTipoDeuda;
    @Column(name = "bolcom_tipo_documento", length = 5)
    private String bolcomTipoDocumento;
    @Column(name = "bolcom_tipo_motivo", length = 5)
    private String bolcomTipoMotivo;
    @Column(name = "icom_codigo_moneda", length = 5)
    private String icomCodigoMoneda;
    @Column(name = "icom_fecha_vencimiento")
    private LocalDate icomFechaVencimiento;
    @Column(name = "icom_mercado_codigo", length = 10)
    private String icomMercadoCodigo;
    @Column(name = "icom_mercado_descripcion", length = 500)
    private String icomMercadodDescripcion;
    @Column(name = "icom_monto_impago", length = 20)
    private String icomMontoImpago;
    @Column(name = "icom_nombre_librador", length = 100)
    private String icomNombreLibrador;
    @Column(name = "icom_nombre_localidad", length = 500)
    private String icomNombreLocalidad;
    @Column(name = "icom_nro_cheque_operacion", length = 20)
    private String icomNroChequeOperacion;
    @Column(name = "icom_tipo_credito", length = 100)
    private String icomTipoCredito;
    @Column(name = "icom_tipo_deuda", length = 10)
    private String icomTipoDeuda;
    @Column(name = "icom_tipo_documento", length = 100)
    private String icomTipoDocumento;
    @Column(name = "icom_tipo_motivo", length = 5)
    private String icomTipoMotivo;

}
