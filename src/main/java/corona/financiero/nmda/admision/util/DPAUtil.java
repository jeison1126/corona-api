package corona.financiero.nmda.admision.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DPAUtil {

    public List<DPAResponseDTO> regionesDesdeArchivo() throws IOException {
        log.debug("Servicio DPA de regiones no disponible, se procesa con datos locales...");
        File resource = new ClassPathResource("regiones.json").getFile();
        String regiones = new String(Files.readAllBytes(resource.toPath()), StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();

        List<DPAResponseDTO> listaRegiones = objectMapper.readValue(regiones, new TypeReference<>() {
        });

        log.debug("Lista regiones: {}", listaRegiones.toString());

        return listaRegiones;
    }

    public List<DPAResponseDTO> comunasDesdeArchivo(String codigoRegion) throws IOException {
        log.debug("Servicio DPA de comunas no disponible, se procesa con datos locales...");
        File resource = new ClassPathResource("comunas.json").getFile();
        String comunas = new String(Files.readAllBytes(resource.toPath()), StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();

        List<DPAResponseDTO> listaComunas = objectMapper.readValue(comunas, new TypeReference<>() {
        });


        return listaComunas.stream().filter(l -> l.getCodigoPadre().startsWith(codigoRegion)).collect(Collectors.toList());
    }
}
