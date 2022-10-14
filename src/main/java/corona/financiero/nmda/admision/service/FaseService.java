package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.ParFaseDTO;
import corona.financiero.nmda.admision.dto.ParFaseHijaDTO;
import corona.financiero.nmda.admision.entity.ParFaseEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParFaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FaseService {

    @Autowired
    private ParFaseRepository parFaseRepository;

    public List<ParFaseDTO> listarFasesVigentes() {

        List<ParFaseEntity> allByEstadoOrderByDescripcion = parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdIsNullOrderByFaseId();

        if (allByEstadoOrderByDescripcion == null || allByEstadoOrderByDescripcion.isEmpty()) {
            throw new NoContentException();
        }
        return allByEstadoOrderByDescripcion.stream().map(f -> {

            ParFaseDTO p = new ParFaseDTO();
            p.setFaseId(f.getFaseId());
            p.setDescripcion(f.getDescripcion());
            List<ParFaseEntity> fasesHijas = parFaseRepository.findAllByVigenciaIsTrueAndFasePadreIdOrderByFaseId(f.getFaseId());

            if (!fasesHijas.isEmpty()) {
                p.setSubfase(fasesHijas.stream().map(h -> {
                    ParFaseHijaDTO parFaseHijaDTO = new ParFaseHijaDTO();
                    parFaseHijaDTO.setFaseId(h.getFaseId());
                    parFaseHijaDTO.setDescripcion(h.getDescripcion());
                    return parFaseHijaDTO;
                }).collect(Collectors.toList()));
            }

            return p;
        }).collect(Collectors.toList());
    }
}
