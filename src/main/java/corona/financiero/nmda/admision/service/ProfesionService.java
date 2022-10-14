package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ProfesionResponseDTO;
import corona.financiero.nmda.admision.entity.ParProfesionEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParProfesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesionService {

    @Autowired
    private ParProfesionRepository parProfesionRepository;

    public List<ProfesionResponseDTO> listarProfesion() {

        List<ParProfesionEntity> profesionEntities = parProfesionRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if (profesionEntities == null || profesionEntities.isEmpty()) {
            throw new NoContentException();
        }

        return profesionEntities.stream().map(p -> {
            ProfesionResponseDTO profesionResponseDTO = new ProfesionResponseDTO();
            profesionResponseDTO.setProfesionId(p.getProfesionId());
            profesionResponseDTO.setDescripcion(p.getDescripcion());
            return profesionResponseDTO;
        }).collect(Collectors.toList());
    }
}
