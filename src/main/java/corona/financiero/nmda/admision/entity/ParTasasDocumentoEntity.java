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
@Table(name = "par_tasas_documento")
public class ParTasasDocumentoEntity {
    @Column(name = "id_tasa", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tasaId;
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "t_mes_ccuota_3_48", length = 10)
    private String tMesCCuota3_48;
    @Column(name = "t_anual_ccuota_3_48", length = 10)
    private String tAnualCCuota3_48;
    @Column(name = "t_mes_ccuota_3_48_1", length = 10)
    private String tMesCCuota3_48_1;
    @Column(name = "t_anual_ccuota_3_48_1", length = 10)
    private String tAnualCCuota3_48_1;
    @Column(name = "t_mes_ccuota_3_48_2", length = 10)
    private String tMesCCuota3_48_2;
    @Column(name = "t_anual_ccuota_3_48_2", length = 10)
    private String tAnualCCuota3_48_2;
    @Column(name = "t_mes_ccuota_3", length = 10)
    private String tMesCCuota3;
    @Column(name = "t_anual_ccuota_3", length = 10)
    private String tAnualCCuota3;
    @Column(name = "t_mes_ccuota_1_3", length = 10)
    private String tMesCCuota1_3;
    @Column(name = "t_anual_ccuota_1_3", length = 10)
    private String tAnualCCuota1_3;
    @Column(name = "t_mes_ccuota_2_3", length = 10)
    private String tMesCCuota2_3;
    @Column(name = "t_anual_ccuota_2_3", length = 10)
    private String tAnualCCuota2_3;
    @Column(name = "t_mes_acuota_3_48", length = 10)
    private String tMesACuota3_48;
    @Column(name = "t_anual_acuota_3_48", length = 10)
    private String tAnualACuota3_48;
    @Column(name = "t_mes_acuota_3_48_1", length = 10)
    private String tMesACuota3_48_1;
    @Column(name = "t_anual_acuota_3_48_1", length = 10)
    private String tAnualACuota3_48_1;
    @Column(name = "t_mes_acuota_3_48_2", length = 10)
    private String tMesACuota3_48_2;
    @Column(name = "t_anual_acuota_3_48_2", length = 10)
    private String tAnualACuota3_48_2;
    @Column(name = "t_mes_saldo_ref", length = 10)
    private String tMesSaldoRef;
    @Column(name = "t_anual_saldo_mes", length = 10)
    private String tAnualSaldoMes;
    @Column(name = "t_mes_refinan_3_48", length = 10)
    private String tMesRefinan3_48;
    @Column(name = "t_anual_refinan_3_48", length = 10)
    private String tAnualRefinan3_48;
    @Column(name = "t_mes_refinan_3_48_1", length = 10)
    private String tMesRefinan3_48_1;
    @Column(name = "t_anual_refinan_3_48_1", length = 10)
    private String tAnualRefinan3_48_1;
    @Column(name = "t_mes_refinan_3_48_2", length = 10)
    private String tMesRefinan3_48_2;
    @Column(name = "t_anual_refinan_3_48_2", length = 10)
    private String tAnualRefinan3_48_2;
    @Column(name = "t_mes_rene_3_48", length = 10)
    private String tMesRene3_48;
    @Column(name = "t_anual_rene_3_48", length = 10)
    private String tAnualRene3_48;
    @Column(name = "t_mes_rene_3_48_1", length = 10)
    private String tMesRene3_48_1;
    @Column(name = "t_anual_rene_3_48_1", length = 10)
    private String tAnualRene3_48_1;
    @Column(name = "t_mes_rene_3_48_2", length = 10)
    private String tMesRene3_48_2;
    @Column(name = "t_anual_rene_3_48_2", length = 10)
    private String tAnualRene3_48_2;
    @Column(name = "t_tmc", length = 10)
    private String tTmc;
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
