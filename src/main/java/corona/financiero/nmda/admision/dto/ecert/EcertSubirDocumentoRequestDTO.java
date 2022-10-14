package corona.financiero.nmda.admision.dto.ecert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcertSubirDocumentoRequestDTO {

    @JsonProperty("RutUsuario")
    private String rutUsuario;
    @JsonProperty("IdUsuarioECert")
    private int idUsuarioEcert;
    @JsonProperty("NombreDocumento")
    private String nombreDocumento;
    @JsonProperty("RequiereCustodia")
    private boolean requiereCustodia;
    @JsonProperty("PosicionFirmaX")
    private int posicionFirmaX;
    @JsonProperty("PosicionFirmaY")
    private int posicionFirmaY;
    @JsonProperty("PosicionFirmaPagina")
    private int posicionFirmaPagina;
    @JsonProperty("DocumentoBase64")
    private String documentoBase64;
}
