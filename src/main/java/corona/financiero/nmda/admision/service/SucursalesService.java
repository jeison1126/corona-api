package corona.financiero.nmda.admision.service;

import corona.financiero.nmda.admision.dto.SucursalDTO;
import corona.financiero.nmda.admision.dto.ZonaGeograficaDTO;
import corona.financiero.nmda.admision.entity.SucursalesEntity;
import corona.financiero.nmda.admision.ex.NoContentException;
import corona.financiero.nmda.admision.repository.SucursalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SucursalesService {

    @Autowired
    private SucursalesRepository sucursalesRepository;

    public List<SucursalDTO> listarSucursales(String zona) {
        List<SucursalesEntity> sucursales = null;
        if (zona == null || zona.trim().isEmpty()) {
            sucursales = sucursalesRepository.findAllByVigenciaIsTrueOrderByDescripcionSucursalAsc();
        } else {
            sucursales = sucursalesRepository.findAllByVigenciaIsTrueAndDescripcionZonaGeograficaOrderByDescripcionSucursalAsc(zona);
        }


        if (sucursales.isEmpty()) {
            throw new NoContentException();
        }

        return sucursales.stream().map(s -> {
            SucursalDTO sucursalDTO = new SucursalDTO();
            sucursalDTO.setNombre(s.getDescripcionSucursal());
            sucursalDTO.setCodigoSucursal(s.getCodigoSucursal());
            sucursalDTO.setZonaGeografica(s.getDescripcionZonaGeografica());
            return sucursalDTO;
        }).collect(Collectors.toList());

    }

    public List<ZonaGeograficaDTO> listarZonasGeograficasSucursal() {
        List<String> zonas = sucursalesRepository.listarZonasGeograficasSucursal();

        if (zonas.isEmpty()) {
            throw new NoContentException();
        }

        return zonas.stream().map(s -> {
            ZonaGeograficaDTO zonaGeograficaDTO = new ZonaGeograficaDTO();
            zonaGeograficaDTO.setZonaGeografica(s);

            return zonaGeograficaDTO;
        }).collect(Collectors.toList());
    }
}
