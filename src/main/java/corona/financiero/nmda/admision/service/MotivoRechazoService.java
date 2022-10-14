package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.MotivoRechazoTarjetaDTO;
import corona.financiero.nmda.admision.entity.ParMotivoRechazoEntity;
import corona.financiero.nmda.admision.repository.ParMotivoRechazoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MotivoRechazoService {

    @Autowired
    private ParMotivoRechazoRepository parMotivoRechazoRepository;
    
    public List<MotivoRechazoTarjetaDTO> listarMotivosRechazoVigentes() {

        List<ParMotivoRechazoEntity> allByVigenciaIsTrueOrderByDescripcion = parMotivoRechazoRepository.findAllByVigenciaIsTrueOrderByDescripcion();
        
        List<MotivoRechazoTarjetaDTO> motivosRechazo = new ArrayList<>();

        if (!allByVigenciaIsTrueOrderByDescripcion.isEmpty()) {
        	motivosRechazo = allByVigenciaIsTrueOrderByDescripcion.stream().map(mr -> {
            	MotivoRechazoTarjetaDTO m = new MotivoRechazoTarjetaDTO();
                m.setMotivoRechazoId(mr.getMotivoRechazoId());
                m.setDescripcion(mr.getDescripcion());
                return m;
            }).collect(Collectors.toList());
        }
        return motivosRechazo;

    }
}
