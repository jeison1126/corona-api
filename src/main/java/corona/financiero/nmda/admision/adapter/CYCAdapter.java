package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.cyc.CYCCrearCuentaRequestDTO;
import corona.financiero.nmda.admision.dto.cyc.CYCCrearCuentaResponseDTO;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Component
@Slf4j
public class CYCAdapter {
    @Value("${cyc.base.uri}")
    private String endpoint;

    @Value("${cyc.crear.cuenta}")
    private String crearCuentaPath;

    @Value("${admision.api.key.name}")
    private String admisionApiKeyName;

    @Value("${admision.api.key}")
    private String admisionApiKey;

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


    public CYCCrearCuentaResponseDTO crearCuentaCYC(CYCCrearCuentaRequestDTO request) {
        log.debug("Crear cuenta CYC");
        log.debug("request: {}",request.toString());
        CYCCrearCuentaResponseDTO response = null;
        try {
            response = webClient()
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(crearCuentaPath).build())
                    .body(Mono.just(request), CYCCrearCuentaRequestDTO.class)
                    .retrieve()
                    .bodyToMono(CYCCrearCuentaResponseDTO.class)
                    .block();

            log.debug("Respuesta servicio cyc: {}", response.toString());

        } catch (Exception e) {
            log.error("Se produjo un error al crear la cuenta en cyc...");
            log.error(e.getMessage(), e);
        }


        return response;
    }
}
