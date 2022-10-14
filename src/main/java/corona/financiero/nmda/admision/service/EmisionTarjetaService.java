package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.EmisionMotivoResponseDTO;
import corona.financiero.nmda.admision.entity.ParTipoSolicitudEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.ParTipoSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmisionTarjetaService {

    @Autowired
    private ParTipoSolicitudRepository parTipoSolicitudRepository;

    public List<EmisionMotivoResponseDTO> listarMotivos() {

        List<ParTipoSolicitudEntity> parTipoSolicitudEntities = parTipoSolicitudRepository.findAllByVigenciaIsTrueOrderByDescripcion();

        if (parTipoSolicitudEntities == null || parTipoSolicitudEntities.isEmpty()) {
            throw new NoContentException();
        }

        return parTipoSolicitudEntities.stream().map(t -> {
            EmisionMotivoResponseDTO emisionMotivoResponseDTO = new EmisionMotivoResponseDTO();

            emisionMotivoResponseDTO.setMotivoId(t.getTipoSolicitudId());
            emisionMotivoResponseDTO.setDescripcion(t.getDescripcion());

            return emisionMotivoResponseDTO;
        }).collect(Collectors.toList());

    }
}
