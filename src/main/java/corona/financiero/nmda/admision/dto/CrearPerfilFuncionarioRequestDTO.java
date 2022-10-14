package corona.financiero.nmda.admision.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPerfilFuncionarioRequestDTO {
    
    private FuncionarioRequestDTO funcionario;

    private List<Long> perfiles;
}