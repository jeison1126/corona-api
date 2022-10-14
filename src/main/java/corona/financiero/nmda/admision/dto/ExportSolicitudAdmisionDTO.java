package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportSolicitudAdmisionDTO {

    private long solicitudId;
    private String origen;
    private String rutCliente;
    private String nombreCliente;
    private String apellidoPaternoCliente;
    private String apellidoMaternoCliente;
    private Integer edad;
    private Integer movil;
    private String emailCliente;
    private String estadoSolicitud;
    private String faseEvaluacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fechaSolicitud;
    private String canalAtencion;
    private String sucursal;
    private String zonaGeograficaSucursal;
    private String reglaNegocio;
    private String rutEjecutivo;
    private String nombreEjecutivo;
}
