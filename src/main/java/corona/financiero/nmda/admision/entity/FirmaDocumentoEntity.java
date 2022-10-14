package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "firma_documento")
public class FirmaDocumentoEntity {

    @Column(name = "id_firma", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long firmaId;
    @ManyToOne(targetEntity = SolicitudAdmisionEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_solicitud", referencedColumnName = "id_solicitud")}, foreignKey = @ForeignKey(name = "solicitud_admision_fk"))
    private SolicitudAdmisionEntity solicitudAdmisionEntity;
    @ManyToOne(targetEntity = ClienteEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "rut", referencedColumnName = "rut")}, foreignKey = @ForeignKey(name = "FK_firma_documento_cliente"))
    private ClienteEntity clienteEntity;
    @ManyToOne(targetEntity = ParTipoDocumentoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id_tipo_documento")}, foreignKey = @ForeignKey(name = "FK_firma_documento_par_tipo_documento"))
    private ParTipoDocumentoEntity parTipoDocumentoEntity;
    @ManyToOne(targetEntity = ParTasasDocumentoEntity.class)
    @org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
    @JoinColumns(value = {@JoinColumn(name = "id_tasa", referencedColumnName = "id_tasa")}, foreignKey = @ForeignKey(name = "FK_firma_documento_tasas_dcoumento"))
    private ParTasasDocumentoEntity parTasasDocumentoEntity;
    @Column(name = "id_usuario_ecert_tmp")
    private Integer usuarioEcertTmpId;
    @Column(name = "url_login_firma_ecert_tmp", length = 500)
    private String urlLoginEcertTmp;
    @Column(name = "id_documento_ecert")
    private Integer documentoEcertId;
    @Column(name = "firmado")
    private Boolean firmado;
    @Column(name = "razon_rechazo", length = 500)
    private String razonRechazo;
    @Column(name = "nombre_documento", length = 100)
    private String nombreDocumento;
    @Column(name = "documento")
    private String documento;
    @Column(name = "documento_firmado")
    private String documentoFirmado;
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
