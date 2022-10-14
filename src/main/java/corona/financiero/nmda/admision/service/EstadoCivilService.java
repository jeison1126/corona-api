package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.EstadoCivilResponseDTO;
import corona.financiero.nmda.admision.entity.ParEstadoCivilEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParEstadoCivilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoCivilService {

    @Autowired
    private ParEstadoCivilRepository parEstadoCivilRepository;

    public List<EstadoCivilResponseDTO> listarEstadoCivil() {

        List<ParEstadoCivilEntity> estadoCivilEntities = parEstadoCivilRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if(estadoCivilEntities == null || estadoCivilEntities.isEmpty()){
            throw new NoContentException();
        }

        return estadoCivilEntities.stream().map(e -> {
            EstadoCivilResponseDTO estadoCivilResponseDTO = new EstadoCivilResponseDTO();
            estadoCivilResponseDTO.setEstadoCivilId(e.getEstadoCivilId());
            estadoCivilResponseDTO.setDescripcion(e.getDescripcion());
            return estadoCivilResponseDTO;
        }).collect(Collectors.toList());
    }
}
