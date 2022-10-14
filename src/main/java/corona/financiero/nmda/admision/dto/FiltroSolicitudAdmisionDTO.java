package corona.financiero.nmda.admision.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import corona.financiero.nmda.admision.enumeration.DireccionOrdenaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroSolicitudAdmisionDTO {

    private int pagina;
    private String rut;
    private Long estadoEvaluacion;
    private Long faseEvaluacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaDesde;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date fechaHasta;
    private Long canalAtencion;
    private Long sucursal;
    private String zonaGeografica;
    private int columnaOrden;
    private DireccionOrdenaEnum orden;
}
