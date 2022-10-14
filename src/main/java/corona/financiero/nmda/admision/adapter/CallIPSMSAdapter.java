package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.sms.CallIPSMSRequestDTO;
import corona.financiero.nmda.admision.dto.sms.CallIPSMSResponseDTO;
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
import reactor.util.retry.Retry;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Component
@Slf4j
public class CallIPSMSAdapter {

    @Value("${sms.uri}")
    private String uri;

    @Value("${sms.api.key.name}")
    private String apiKeyName;

    @Value("${sms.api.key}")
    private String apiKey;

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

    protected WebClient webClient() throws SSLException {
        log.debug("apiKeyName: {}", apiKeyName);
        log.debug("apiKey: {}", apiKey);
        log.debug("admisionApiKeyName: {}", admisionApiKeyName);
        log.debug("admisionApiKey: {}", admisionApiKey);
        log.debug("uri: {}", uri);

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
                .defaultHeader(apiKeyName, apiKey)
                .defaultHeader(admisionApiKeyName, admisionApiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public void enviarSMS(CallIPSMSRequestDTO request) {
        log.debug("Enviar SMS a: {} con codigo: {}", request.getDni(), request.getMessage());
        try {
            CallIPSMSResponseDTO response = webClient()
                    .post()
                    .body(Mono.just(request), CallIPSMSRequestDTO.class)
                    .retrieve()
                    .bodyToMono(CallIPSMSResponseDTO.class)
                    .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio call ip: {}", response.toString());
            log.debug("Notificacion SMS  enviada!");
        } catch (Exception e) {
            log.error("Se produjo un error al enviar notificacion por SMS...");
            log.error(e.getMessage(), e);
        }
    }
}
