package corona.financiero.nmda.admision.dto.ecert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcertPreinscripcionRequestDTO {

    @JsonProperty("RutUsuario")
    private String rutUsuario;
    @JsonProperty("Nombre")
    private String nombre;
    @JsonProperty("ApellidoPaterno")
    private String apellidoPaterno;
    @JsonProperty("ApellidoMaterno")
    private String apellidoMaterno;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("RutEmpresa")
    private String rutEmpresa;
    @JsonProperty("CantidadDoctos")
    private int cantidadDocumentos;
    @JsonProperty("UrlCallback")
    private String urlCallback;
    @JsonProperty("UrlWebHook")
    private String urlWebhook;
    @JsonProperty("TipoFirma")
    private int tipoFirma;
    @JsonProperty("RutSponsor")
    private String rutSponsor;
    @JsonProperty("ProductoCombinadoId")
    private String productoCombinado;
    @JsonProperty("CorreoEnvioDocumentoFirmado")
    private String correoEndioDocumentoFirmado;
}
