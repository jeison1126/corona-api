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
@Table(name = "no_recomendados")
public class NoRecomendadosEntity {

    @Column(name = "rut", length = 12, nullable = false)
    @Id
    private String rut;
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name = "apellido_paterno", length = 50)
    private String apellidoPaterno;
    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;
    @Column(name = "sucursal_origen")
    private Long sucursalOrigen;
    @Column(name = "motivo_no_recomendado", length = 50)
    private String motivoNoRecomendado;
    @Column(name = "fecha_ing_reg", nullable = false)
    private LocalDateTime fechaRegistro;
    @Column(name = "usuario_ing_reg", length = 20, nullable = false)
    private String usuarioRegistro;
    @Column(name = "fecha_ult_mod_reg")
    private LocalDateTime fechaModificacion;
    @Column(name = "usuario_ult_mod_reg", length = 20)
    private String usuarioModificacion;
}
