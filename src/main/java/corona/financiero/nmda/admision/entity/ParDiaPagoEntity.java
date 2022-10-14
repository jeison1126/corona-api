package corona.financiero.nmda.admision.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "par_dia_pago")
public class ParDiaPagoEntity implements Serializable {
    @Column(name = "id_dia_pago", nullable = false)
    @Id
    private long diaPagoId;
    @Column(name = "vigencia", nullable = false)
    private boolean vigencia;
}
