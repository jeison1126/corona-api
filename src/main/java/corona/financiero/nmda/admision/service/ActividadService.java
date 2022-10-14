package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ActividadResponseDTO;
import corona.financiero.nmda.admision.entity.ParActividadEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    @Autowired
    private ParActividadRepository actividadRepository;


    public List<ActividadResponseDTO> listarActividades() {

        List<ParActividadEntity> actividadEntities = actividadRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if (actividadEntities == null || actividadEntities.isEmpty()) {
            throw new NoContentException();
        }

        return actividadEntities.stream().map(a -> {
            ActividadResponseDTO actividadResponseDTO = new ActividadResponseDTO();
            actividadResponseDTO.setActividadId(a.getActividadId());
            actividadResponseDTO.setDescripcion(a.getDescripcion());

            return actividadResponseDTO;
        }).collect(Collectors.toList());
    }
}
