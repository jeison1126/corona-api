package corona.financiero.nmda.admision.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPerfilFuncionarioRequestDTO {
    private String rutFuncionario;
    private List<Long> perfiles;
}