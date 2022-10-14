package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.notificacion.EmailPropertiesDTO;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Component
@Slf4j
public class EmailAdapter {
    @Value("${email.uri}")
    private String uri;

    @Value("${request.timeout:5000}")
    private Integer timeout;

    @Value("${request.retry.max.attempts:2}")
    protected Integer maxAttempts;

    @Value("${request.retry.delay:2}")
    protected Integer delay;

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
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(admisionApiKeyName, admisionApiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Async
    public void notificar(EmailPropertiesDTO emailPropertiesDTO) {
        log.debug("Notificando.... {}", emailPropertiesDTO.toString());
        try {
            webClient()
                    .post()
                    .body(Mono.just(emailPropertiesDTO), EmailPropertiesDTO.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    // .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Notificacion enviada");
        } catch (WebClientResponseException e) {
            log.error("1 Se produjo un error al intentar enviar notificacion...");
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error("2 Se produjo un error al intentar enviar notificacion...");
            log.error(e.getMessage(), e);
        }
    }
}
