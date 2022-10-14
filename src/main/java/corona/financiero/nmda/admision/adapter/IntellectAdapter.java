package corona.financiero.nmda.admision.adapter;

import corona.financiero.nmda.admision.dto.intellect.NonFinancialDetailsDTO;
import corona.financiero.nmda.admision.ex.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class IntellectAdapter {

    @Value("${intellect.client.host}")
    private String host;

    @Value("${intellect.client.uri.non-financial-details}")
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

    public Boolean validateIfClientExist(String rut) {

        boolean isClientExist = false;
        String errorMessage = "Se produjo un problema con la respuesta del servicio remoto";

        //TODO se debe quitar validacion cuando integracion con Intellect se encuentre disponible
        if (true) {
            if (rut.equalsIgnoreCase("222222222") || rut.equalsIgnoreCase("555555555") || rut.equalsIgnoreCase("777777777")) {
                isClientExist = true;
            } else
                isClientExist = false;


            return isClientExist;
        }


        try {
            NonFinancialDetailsDTO response = getNonFinancialDetailsDTO(rut);

            if (response != null) {

                log.debug("Response: {}", response.toString());
                isClientExist = true;
            }

        } catch (WebClientResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Not found: {}", HttpStatus.NOT_FOUND);
            }
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error("Bad request: {}", HttpStatus.BAD_REQUEST);
                throw new BadRequestException(errorMessage);
            }
            if (e.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.debug("No content: {}", HttpStatus.NO_CONTENT);
            }
            if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                log.error("Problemas con servicio remoto: {}", HttpStatus.INTERNAL_SERVER_ERROR);
                throw new BadRequestException(errorMessage);
            }
        } catch (Exception e) {
            log.error("Problemas con servicio remoto (intellect)");
            throw new BadRequestException(errorMessage);
        }

        return isClientExist;
    }

    private NonFinancialDetailsDTO getNonFinancialDetailsDTO(String rut) {
        return webClientBuilder()
                .get()
                .uri(uri + "/{rut}", rut)
                .retrieve()
                .bodyToMono(NonFinancialDetailsDTO.class)
                .timeout(Duration.ofSeconds(timeout))
                .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                .block();
    }


    private WebClient webClientBuilder() {

        return WebClient.builder()
                .baseUrl(host)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(admisionApiKeyName, admisionApiKey)
                .build();
    }

}
