package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaSolicitudAdmisionDTO {

    private long solicitudId;
    private String rut;
    private String estadoSolicitud;
    private String faseEvaluacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fechaSolicitud;
    private String canalAtencion;
    private String sucursal;
    private String zonaGeograficaSucursal;
}
