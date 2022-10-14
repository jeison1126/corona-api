package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleSolicitudAdmisionResponseDTO {

    private long solicitudId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate fechaSolicitud;
    private String canal;
    private String estadoSolicitud;
    private String rut;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer edad;
    private long movil;
    private String email;
    private DireccionDTO direccion;
    private FuncionarioDTO ejecutivo;
    private SucursalDTO sucursal;
    private List<DetalleEvaluacionComercialResponsDTO> evaluacionComercial;
}
