package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.adapter.RegionComunaAdapter;
import corona.financiero.nmda.admision.dto.ComunaDTO;
import corona.financiero.nmda.admision.dto.RegionDTO;
import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import corona.financiero.nmda.admision.ex.BadRequestException;
import corona.financiero.nmda.admision.ex.NoContentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RegionComunaService {

    @Autowired
    private RegionComunaAdapter regionComunaAdapter;

    public List<RegionDTO> listarRegiones() {

        List<DPAResponseDTO> regiones = regionComunaAdapter.regiones();
        if (regiones == null || regiones.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("Regiones: {}", regiones);
        return regiones.stream().map(r -> {
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.setCodigo(r.getCodigo());
            regionDTO.setDescripcion(r.getNombre());
            return regionDTO;
        }).collect(Collectors.toList());
    }

    public List<ComunaDTO> listarComunas(String codigoRegion) {
        if (codigoRegion == null || codigoRegion.trim().isEmpty()) {
            throw new BadRequestException("falta indicar codigo de region");
        }
        List<DPAResponseDTO> comunas = regionComunaAdapter.comunas(codigoRegion);
        if (comunas == null || comunas.isEmpty()) {
            throw new NoContentException();
        }
        log.debug("comunas: {}", comunas);
        return comunas.stream().map(c -> {
            ComunaDTO comunaDTO = new ComunaDTO();
            comunaDTO.setCodigo(c.getCodigo());
            comunaDTO.setDescripcion(c.getNombre());
            return comunaDTO;
        }).collect(Collectors.toList());
    }

    public RegionDTO obtenerRegion(String codigoRegion) {

        List<RegionDTO> regiones = regionComunaAdapter.regiones().stream().filter(r -> r.getCodigo().equals(codigoRegion)).map(r -> {
            RegionDTO regionDTO = new RegionDTO();
            regionDTO.setCodigo(r.getCodigo());
            regionDTO.setDescripcion(r.getNombre());
            return regionDTO;
        }).collect(Collectors.toList());

        return regiones.get(0);
    }

    public ComunaDTO obtenerComuna(String codigoRegion, String codigoComuna) {
        List<ComunaDTO> comunas = regionComunaAdapter.comunas(codigoRegion).stream().filter(c -> c.getCodigo().equals(codigoComuna)).map(c -> {
            ComunaDTO comunaDTO = new ComunaDTO();
            comunaDTO.setCodigo(c.getCodigo());
            comunaDTO.setDescripcion(c.getNombre());

            return comunaDTO;
        }).collect(Collectors.toList());

        return comunas.get(0);
    }
}
