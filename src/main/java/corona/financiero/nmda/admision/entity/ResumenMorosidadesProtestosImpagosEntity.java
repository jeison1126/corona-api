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
@Table(name = "resumen_morosidades_protestos_impagos")
public class ResumenMorosidadesProtestosImpagosEntity {

    @Column(name = "id_resumen_morosidades_protestos_impagos", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long resumenMorosidadesProtestosImpagosId;
    @ManyToOne(targetEntity = ConsultaScoringEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_consulta_scoring", referencedColumnName = "id_consulta_scoring")}, foreignKey = @ForeignKey(name = "consulta_scoring_fk"))
    private ConsultaScoringEntity consultaScoringEntity;
    @Column(name = "acceso_camara_comercio")
    private Boolean accesoCamaraComercio;
    @Column(name = "cantidad_documentos_12_a_24_meses", length = 5)
    private String cantidadDocumentos12A24Meses;
    @Column(name = "cantidad_documentos_6_a_12_meses", length = 5)
    private String cantidadDocumentos6A12Meses;
    @Column(name = "cantidad_documentos_acumulados_12_meses", length = 5)
    private String cantidadDocumentosAcumulados12Meses;
    @Column(name = "cantidad_documentos_acumulados_24_meses", length = 5)
    private String cantidadDocumentosAcumulados24Meses;
    @Column(name = "cantidad_documentos_acumulados_mas_de_24_meses", length = 5)
    private String cantidadDocumentosAcumuladosMasDe24Meses;
    @Column(name = "cantidad_documentos_acumulados_ultimos_6_meses", length = 5)
    private String cantidadDocumentosAcumuladosUltimos6Meses;
    @Column(name = "cantidad_documentos_mas_de_24_meses", length = 5)
    private String cantidadDocumentosMasDe24Meses;
    @Column(name = "cantidad_documentos_ultimos_6_meses", length = 5)
    private String cantidadDocumentosUltimos6Meses;
    @Column(name = "cantidad_total_impagos", length = 10)
    private String cantidadTotalImpagos;
    @Column(name = "fecha_vencimiento_ultimo_impago")
    private LocalDate fechaVencimientoUltimoImpago;
    @Column(name = "indicador_error_conectarse_alcss")
    private Boolean indicadorErrorConectarseAlCSS;
    @Column(name = "indicador_existencia_informacion_para_seccion")
    private Boolean indicadorExistenciaInformacionParaSeccion;
    @Column(name = "monto_documentos_12_a_24_meses", length = 10)
    private String montoDocumentos12A24Meses;
    @Column(name = "monto_documentos_6_a_12_meses", length = 10)
    private String montoDocumentos6A12Meses;
    @Column(name = "monto_documentos_mas_de_24_meses", length = 10)
    private String montoDocumentosMasDe24Meses;
    @Column(name = "monto_documentos_ultimos_6_meses", length = 10)
    private String montoDocumentosUltimos6Meses;
    @Column(name = "monto_total_impagos", length = 15)
    private String montoTotalImpagos;
    @Column(name = "monto_ultimo_impago", length = 15)
    private String montoUltimoImpago;
    @Column(name = "tipo_deuda_ultimo_impago", length = 20)
    private String tipoDeudaUltimoImpago;


}
