package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidarFirmaDocumentoRequestDTO {

    private String rut;
    private long solicitudId;
    private int usuarioEcertId;
}
