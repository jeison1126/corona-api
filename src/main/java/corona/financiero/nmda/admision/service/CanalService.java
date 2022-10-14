package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.CanalDTO;
import corona.financiero.nmda.admision.entity.ParCanalEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParCanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanalService {

    @Autowired
    private ParCanalRepository parCanalRepository;

    public List<CanalDTO> listarCanales() {

        List<ParCanalEntity> canales = parCanalRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if (canales == null || canales.isEmpty()) {
            throw new NoContentException();
        }

        return canales.stream().map(c -> {
            CanalDTO canalDTO = new CanalDTO();
            canalDTO.setCanalId(c.getCanalId());
            canalDTO.setNombre(c.getDescripcion());
            return canalDTO;
        }).collect(Collectors.toList());

    }
}
