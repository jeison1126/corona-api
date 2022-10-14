package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "cmf_r04")
public class CMFR04Entity {
    @Column(name = "rut", length = 12, nullable = false)
    @Id
    private String rut;
    @Column(name = "nombre_razon_social", length = 50)
    private String nombreRazonSocial;
    @Column(name = "creditos_directos_impagos_menos_30dias", nullable = false)
    private Long creditosDirectosImpagosMenos30Dias;
    @Column(name = "creditos_directos_impagos_30_a_90dias", nullable = false)
    private Long creditosDirectosImpagos30a90Dias;
    @Column(name = "creditos_directos_impagos_180_a_3anios", nullable = false)
    private Long creditosDirectosImpagos180a3anios;
    @Column(name = "operaciones_financieras", nullable = false)
    private Long operacionesFinancieras;
    @Column(name = "instrumentos_deudas_adquiridos", nullable = false)
    private Long instrumentosDeudasAdquiridos;
    @Column(name = "creditos_indirectos_impagos_menos_30dias", nullable = false)
    private Long creditosIndirectosImpagosMenos30Dias;
    @Column(name = "creditos_indirectos_impagos_30_a_3anios", nullable = false)
    private Long creditosIndirectosImpagos30a3anios;
    @Column(name = "creditos_comerciales", nullable = false)
    private Long creditosComerciales;
    @Column(name = "creditos_consumo", nullable = false)
    private Long creditosConsumo;
    @Column(name = "cantidad_entidades_creditos", nullable = false)
    private Integer cantidadEntidadesCreditos;
    @Column(name = "creditos_vivienda", nullable = false)
    private Long creditosVivienda;
    @Column(name = "creditos_directos_impagos_igual_mayor_3anios", nullable = false)
    private Long creditosDirectosImpagosIgualMayor3anios;
    @Column(name = "creditos_indirectos_impagos_igual_mayor_3anios", nullable = false)
    private Long creditosIndirectosImpagosIgualMayor3anios;
    @Column(name = "monto_linea_credito_disponible", nullable = false)
    private Long montoLineaCreaditoDisponible;
    @Column(name = "creditos_contingentes", nullable = false)
    private Long creditosContingentes;
    @Column(name = "creditos_directos_impagos_90_a_180dias", nullable = false)
    private Long creditosDirectosImpagos90a180dias;
    @Column(name = "institucion_registra_deuda19", nullable = false)
    private Long institucion_registra_deuda19;
    @Column(name = "institucion_registra_deuda20", nullable = false)
    private Long institucion_registra_deuda20;
    @Column(name = "institucion_registra_deuda21", nullable = false)
    private Long institucion_registra_deuda21;
    @Column(name = "institucion_registra_deuda22", nullable = false)
    private Long institucion_registra_deuda22;
    @Column(name = "institucion_registra_deuda23", nullable = false)
    private Long institucion_registra_deuda23;
    @Column(name = "institucion_registra_deuda24", nullable = false)
    private Long institucion_registra_deuda24;
    @Column(name = "institucion_registra_deuda25", nullable = false)
    private Long institucion_registra_deuda25;
    @Column(name = "institucion_registra_deuda26", nullable = false)
    private Long institucion_registra_deuda26;
    @Column(name = "institucion_registra_deuda27", nullable = false)
    private Long institucion_registra_deuda27;
    @Column(name = "institucion_registra_deuda28", nullable = false)
    private Long institucion_registra_deuda28;
    @Column(name = "institucion_registra_deuda29", nullable = false)
    private Long institucion_registra_deuda29;
    @Column(name = "institucion_registra_deuda30", nullable = false)
    private Long institucion_registra_deuda30;
    @Column(name = "institucion_registra_deuda31", nullable = false)
    private Long institucion_registra_deuda31;
    @Column(name = "institucion_registra_deuda32", nullable = false)
    private Long institucion_registra_deuda32;
    @Column(name = "cantidad_entidades_credito_comercial", nullable = false)
    private Long cantidadEntidadesCreditoComercial;
    @Column(name = "creditos_leasing_al_dia", nullable = false)
    private Long creditosLeasingAlDia;
    @Column(name = "creditos_leasing_impagos", nullable = false)
    private Long creditosLeasingImpagos;
    @Column(name = "prestamos_estudiantiles_directos_dia_impagos_30dias", nullable = false)
    private Long prestamosEstudiantilesDirectosDiaImpagos30dias;
    @Column(name = "prestamos_estudiantiles_directos_impagos_30_a_90dias", nullable = false)
    private Long prestamosEstudiantilesDirectosImpagos30a90dias;
    @Column(name = "prestamos_estudiantiles_directos_impagos_90_a_180dias", nullable = false)
    private Long prestamosEstudiantilesDirectosImpagos90a180dias;
    @Column(name = "prestamos_estudiantiles_directos_impagos_180_a_3anios", nullable = false)
    private Long prestamosEstudiantilesDirectosImpagos180a3anios;
    @Column(name = "prestamos_estudiantiles_directos_impagos_desde_3anios", nullable = false)
    private Long prestamosEstudiantilesDirectosImpagosDesde3anios;
    @Column(name = "prestamos_estudiantiles_indirectos_impagos_menor_30dias", nullable = false)
    private Long prestamosEstudiantilesIndirectoImpagosMenor30dias;
    @Column(name = "prestamos_estudiantiles_indirectos_impagos_30_a_3anios", nullable = false)
    private Long prestamosEstudiantilesIndirectosImpagos30a3anios;
    @Column(name = "prestamos_estudiantiles_indirectos_impagos_mayor_3anios", nullable = false)
    private Long prestamosEstudiantilesIndirectosImpagosMayor3anios;
    @Column(name = "creditos_contingentes_prestamos_estudiantiles", nullable = false)
    private Long creditosContingentesPrestamosEstudiantiles;
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
