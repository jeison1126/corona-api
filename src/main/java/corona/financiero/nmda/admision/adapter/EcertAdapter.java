package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.ecert.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
public class EcertAdapter {
    @Value("${admision.api.key.name}")
    private String admisionApiKeyName;

    @Value("${admision.api.key}")
    private String admisionApiKey;

    @Value("${ecert.base.uri}")
    private String uri;

    @Value("${ecert.authenticate.uri}")
    private String pathAuthenticate;

    @Value("${ecert.preinscripcion.uri}")
    private String pathPreinscripcion;

    @Value("${ecert.subir.documentos.uri}")
    private String pathDocumentos;

    @Value("${ecert.username}")
    private String username;

    @Value("${ecert.password}")
    private String pass;


    protected WebClient webClient(String uri) throws SSLException {
        //Forma de saltar verificacion de certificado SSL
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

        return WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(admisionApiKeyName, admisionApiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


    private EcertLoginResponseDTO authenticate() {

        EcertLoginRequestDTO request = new EcertLoginRequestDTO();
        request.setUserName(username);
        request.setPassword(pass);
        String uri = this.uri.concat(pathAuthenticate);
        EcertLoginResponseDTO response = null;
        try {
            response = webClient(uri)
                    .post()
                    .body(Mono.just(request), EcertLoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(EcertLoginResponseDTO.class)
                    //.retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio ecert authenticate: {}", response.toString());
        } catch (Exception e) {
            log.error("Error en authenticate!!!!");
            log.error(e.getMessage(), e);
        }

        return response;
    }

    @Cacheable(value = "ecertAuthenticate", unless = "#result==null")
    private EcertLoginResponseDTO ecertAuthenticate() {

        return authenticate();
    }

    public EcertPreinscripcionResponseDTO preinscripcion(EcertPreinscripcionRequestDTO request) {

        EcertLoginResponseDTO responseAuthenticate = ecertAuthenticate();


        String uri = this.uri.concat(pathPreinscripcion);
        EcertPreinscripcionResponseDTO response = null;
        try {
            response = webClient(uri)
                    .post()
                    .headers(h -> {
                        h.add("Authorization", responseAuthenticate.getObjetoGenerico().getToken());
                    })
                    .body(Mono.just(request), EcertPreinscripcionRequestDTO.class)
                    .retrieve()
                    .bodyToMono(EcertPreinscripcionResponseDTO.class)
                    //.retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio ecert preinscripcion: {}", response.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return response;
    }

    public EcertSubirDocumentoResponseDTO subirDocumento(EcertSubirDocumentoRequestDTO request) {
        EcertLoginResponseDTO responseAuthenticate = ecertAuthenticate();


        String uri = this.uri.concat(pathDocumentos);
        EcertSubirDocumentoResponseDTO response = null;
        try {
            response = webClient(uri)
                    .post()
                    .headers(h -> {
                        h.add("Authorization", responseAuthenticate.getObjetoGenerico().getToken());
                    })
                    .body(Mono.just(request), EcertPreinscripcionRequestDTO.class)
                    .retrieve()
                    .bodyToMono(EcertSubirDocumentoResponseDTO.class)
                    //.retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio ecert subir documento: {}", response.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return response;
    }
}
