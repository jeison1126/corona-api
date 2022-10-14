package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.cotizacion.CotizacionRequestDTO;
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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Map;

@Component
@Slf4j
public class CotizacionAdapter {
    @Value("${cotizaciones.uri}")
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
        //Forma de saltar verificacion de certificado SSL
        log.debug("URI cotizaciones: {}",uri);
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

    private Map conexion(CotizacionRequestDTO request) {
        log.debug("Request: {}",request.toString());

        Map response = null;
        try {
            response = webClient()
                    .post()
                    .body(Mono.just(request), CotizacionRequestDTO.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    //.retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio cotizaciones: {}", response.toString());
        } catch (WebClientResponseException e) {
            log.error("1 Se produjo un error al consultar por las cotizaciones del prospecto");
            //log.error("Body: {}",e.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("2 Se produjo un error al consultar por las cotizaciones del prospecto");
        }

        return response;
    }

    public Map consultarCotizaciones(CotizacionRequestDTO request) {
        log.debug("Consultar cotizaciones request: {}", request.toString());

        Map conexion = conexion(request);


        return conexion;
    }
}
