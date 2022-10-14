package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmisionFaseResponseDTO {

    private long admisionFaseId;
    private long faseId;
    private String descripcionFase;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fechaRegistroFase;
    private boolean error;
    private String mensajeInterno;
    private String mensajeEjecutivo;
}
