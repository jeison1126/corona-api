package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.dpa.DPAResponseDTO;
import corona.financiero.nmda.admision.util.DPAUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class RegionComunaAdapter {

    @Value("${api.base.dpa.uri}")
    private String endpoint;

    @Value("${api.dpa.region.uri}")
    private String regionPath;

    @Value("${api.dpa.comuna.uri}")
    private String comunaPath;

    @Value("${request.timeout:5000}")
    private Integer timeout;

    @Value("${request.retry.max.attempts:2}")
    private Integer maxAttempts;

    @Value("${request.retry.delay:2}")
    private Integer delay;

    @Value("${admision.api.key.name}")
    private String admisionApiKeyName;

    @Value("${admision.api.key}")
    private String admisionApiKey;

    @Autowired
    private DPAUtil dpaUtil;


    protected WebClient webClient() throws SSLException {

        //Forma de saltar verificacion de certificado SSL
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        return WebClient.builder()
                .baseUrl(endpoint)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(admisionApiKeyName, admisionApiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private List<DPAResponseDTO> listarRegiones() {
        log.debug("Listar regiones desde api DPA");
        List<DPAResponseDTO> response = null;
        try {
            response = webClient()
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(regionPath).build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<DPAResponseDTO>>() {
                    })
                    .block();

            log.debug("Respuesta servicio regiones: {}", response.toString());

        } catch (Exception e) {
            log.error("Se produjo un error al obtener las regiones...");
            log.error(e.getMessage(), e);
        }

        if (response == null || response.isEmpty()) {
            log.debug("Sin respuesta servicio DPA, se usan datos locales");
            try {
                response = dpaUtil.regionesDesdeArchivo();
            } catch (IOException e) {
                log.error("Se produjo un error en procesar el archivo de regiones");
            }
        }

        return response;
    }

    private List<DPAResponseDTO> listarComunas(String codigoRegion) {
        log.debug("Listar comunas de una region desde api DPA");
        List<DPAResponseDTO> response = null;
        try {
            response = webClient()
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(comunaPath).build(codigoRegion))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<DPAResponseDTO>>() {
                    })
                    .block();

            log.debug("Respuesta servicio comunas: {}", response.toString());

        } catch (Exception e) {
            log.error("Se produjo un error al obtener las comunas...");
            log.error(e.getMessage(), e);
        }

        if (response == null || response.isEmpty()) {
            log.debug("Sin respuesta servicio DPA, se usan datos locales");
            try {
                response = dpaUtil.comunasDesdeArchivo(codigoRegion);
            } catch (IOException e) {
                log.error("Se produjo un error en procesar el archivo de comunas");
            }
        }

        return response;
    }

    @Cacheable(value = "regiones", unless = "#result==null")
    public List<DPAResponseDTO> regiones() {

        return listarRegiones();
    }

    @Cacheable(value = "comunas", unless = "#result==null")
    public List<DPAResponseDTO> comunas(String codigoRegion) {

        return listarComunas(codigoRegion);
    }
}
