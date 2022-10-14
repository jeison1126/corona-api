package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.EstadoSolicitudDTO;
import corona.financiero.nmda.admision.entity.ParEstadoSolicitudEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParEstadoSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoSolicitudService {

    @Autowired
    private ParEstadoSolicitudRepository parEstadoSolicitudRepository;

    public List<EstadoSolicitudDTO> listarEstados() {

        List<ParEstadoSolicitudEntity> estados = parEstadoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if (estados == null || estados.isEmpty()) {
            throw new NoContentException();
        }

        return estados.stream().map(e -> {
            EstadoSolicitudDTO estadoSolicitudDTO = new EstadoSolicitudDTO();
            estadoSolicitudDTO.setEstadoId(e.getEstadoSolicitudId());
            estadoSolicitudDTO.setNombre(e.getDescripcion());
            return estadoSolicitudDTO;
        }).collect(Collectors.toList());

    }
}
