package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.NacionalidadDTO;
import corona.financiero.nmda.admision.entity.ParNacionalidadEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParNacionalidadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NacionalidadService {

    @Autowired
    private ParNacionalidadRepository parNacionalidadRepository;

    public List<NacionalidadDTO> listarNacionalidades() {
        List<ParNacionalidadEntity> lista = parNacionalidadRepository.findAllByVigenciaOrderByDescripcion(true);

        if (lista.isEmpty()) {
            throw new NoContentException();
        }

        return lista.stream().map(n -> {
            NacionalidadDTO nacionalidadDTO = new NacionalidadDTO();
            nacionalidadDTO.setNacionalidadId(n.getNacionalidadId());
            nacionalidadDTO.setDescripcion(n.getDescripcion());

            return nacionalidadDTO;
        }).collect(Collectors.toList());

    }
}
