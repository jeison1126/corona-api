package corona.financiero.nmda.admision.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import corona.financiero.nmda.admision.dto.equifax.*;
import corona.financiero.nmda.admision.util.Constantes;
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
import reactor.util.retry.Retry;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EquifaxAdapter {

    @Value("${equifax.base.uri}")
    private String uri;

    @Value("${equifax.informe.uri}")
    private String pathInforme;

    @Value("${equifax.score.uri}")
    private String pathScore;

    private static final String PLATINUM = "WL5G3N2$PLATINUMOutput";
    private static final String IDENTIFICACION_PERSONA = "identificacionPersona";
    private static final String REGISTRO_MOROSIDADES_PROTESTOS = "registroMorosidadesYProtestos";
    private static final String RESUMEN_MOROSIDADES_PROTESTOS_IMPAGOS = "resumenMorosidadesYProtestosImpagos";

    private static final String ERRORES = "soap-env$Fault";

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

    public Map<String, Object> infoProspectoEquifax(EquifaxRequestDTO request) {
        Map<String, Object> resultado = null;
        try {
            Map<String, Object> hashMap = conexionInfoProspecto(request);

            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(hashMap);
            log.debug("<<<<<<<<<<JSON Respuesta Equifax>>>>>>>>>>>>");
            log.debug(s);


            //primer filtro
            Map<String, Object> platinumOutput = revisaHash(hashMap, PLATINUM);
            log.debug("platinumOutput: {}", platinumOutput);

            //verificando si existen errores:
            Map<String, Object> errores = revisaHash(platinumOutput, ERRORES);
            if (errores != null && !errores.isEmpty()) {
                resultado = new HashMap<>();
                resultado.put("errores", errores);

                return resultado;
            }

            //segundo filtro, en busqueda de respuesta ok
            Map<String, Object> identificacionPersona = null;
            if (platinumOutput.containsKey(IDENTIFICACION_PERSONA)) {
                identificacionPersona = (Map<String, Object>) platinumOutput.get(IDENTIFICACION_PERSONA);
            }

            Map<String, Object> registroMorosidadesYProtestos = null;
            if (platinumOutput.containsKey(REGISTRO_MOROSIDADES_PROTESTOS)) {
                registroMorosidadesYProtestos = (Map<String, Object>) platinumOutput.get(REGISTRO_MOROSIDADES_PROTESTOS);
            }

            Map<String, Object> resumenMorosidadesYProtestosImpagos = null;
            if (platinumOutput.containsKey(RESUMEN_MOROSIDADES_PROTESTOS_IMPAGOS)) {
                resumenMorosidadesYProtestosImpagos = (Map<String, Object>) platinumOutput.get(RESUMEN_MOROSIDADES_PROTESTOS_IMPAGOS);
            }


            resultado = new HashMap<>();
            resultado.put(IDENTIFICACION_PERSONA, identificacionPersona);
            resultado.put(REGISTRO_MOROSIDADES_PROTESTOS, registroMorosidadesYProtestos);
            resultado.put(RESUMEN_MOROSIDADES_PROTESTOS_IMPAGOS, resumenMorosidadesYProtestosImpagos);

        } catch (WebClientResponseException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return resultado;
    }

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

    private Map conexionInfoProspecto(EquifaxRequestDTO request) {
        log.debug("Consultar a servicio info prospecto con datos: {}", request.toString());
        request.setNumeroSerie("");
        request.setICom("");

        Map response = null;
        String uri = this.uri.concat(pathInforme);
        log.debug("URL Servicio informe: {}", uri);
        try {
            response = webClient(uri)
                    .post()
                    .body(Mono.just(request), EquifaxRequestDTO.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio info prospecto: {}", response.toString());
        } catch (WebClientResponseException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.debug("RESPONSE: {}", response);

        return response;
    }

    private Map conexionScoreProspecto(EquifaxScoreRequestDTO request) {
        log.debug("Consultar a servicio Scoring con datos: {}", request.toString());

        Map response = null;
        String uri = this.uri.concat(pathScore);
        log.debug("URL Servicio score: {}", uri);
        try {
            response = webClient(uri)
                    .post()
                    .body(Mono.just(request), EquifaxScoreRequestDTO.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(delay)))
                    .block();

            log.debug("Respuesta servicio scoring: {}", response.toString());
        } catch (WebClientResponseException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return response;
    }

    public int infoScoreEquifax(String rut) {
        EquifaxScoreRequestDTO request = new EquifaxScoreRequestDTO();
        EquifaxApplicantsRequestDTO equifaxApplicantsRequestDTO = new EquifaxApplicantsRequestDTO();
        EquifaxPrimaryConsumerRequestDTO equifaxPrimaryConsumerRequestDTO = new EquifaxPrimaryConsumerRequestDTO();
        EquifaxPeronalInformationRequestDTO datos = new EquifaxPeronalInformationRequestDTO();
        List<EquifaxIdentifierRequestDTO> lName = new ArrayList<>();
        EquifaxIdentifierRequestDTO name = new EquifaxIdentifierRequestDTO();
        name.setIdentifier("Current");
        lName.add(name);
        datos.setName(lName);
        datos.setAddresses(lName);
        datos.setChileanRut(rut);
        datos.setChileanSerialNumber("");
        EquifaxAdditionalAttributeRequestDTO additionalAttribute = new EquifaxAdditionalAttributeRequestDTO();
        additionalAttribute.setUsuActividad("");
        additionalAttribute.setUsuDescLegales("");
        additionalAttribute.setUsuIngresoMinimo("");
        additionalAttribute.setUsuOtrosDesc("");
        additionalAttribute.setUsuTotalHaberes("");
        datos.setAdditionalAttribute(additionalAttribute);
        equifaxPrimaryConsumerRequestDTO.setPersonalInformation(datos);
        equifaxApplicantsRequestDTO.setPrimaryConsumer(equifaxPrimaryConsumerRequestDTO);
        request.setApplicants(equifaxApplicantsRequestDTO);

        Map map = conexionScoreProspecto(request);

        if (map != null) {
            Map<String, Object> applicants = (HashMap) map.get("applicants");
            Map<String, Object> primaryConsumer = (HashMap) applicants.get("primaryConsumer");

            List decisions = (ArrayList) primaryConsumer.get("decisions");
            HashMap hashMap = (HashMap) decisions.get(0);

            List smart = (ArrayList) hashMap.get("smart-decisionId");
            HashMap hashScore = (HashMap) smart.get(0);
            log.debug("score: {}", hashScore.get("SCORE"));

            return (int) hashScore.get("SCORE");
        }

        return Constantes.ERROR_SCORING;
    }

    private Map<String, Object> revisaHash(Map<String, Object> hash, String buscarValorKey) {
        for (Map.Entry<String, Object> entry : hash.entrySet()) {
            String key = entry.getKey();

            if (key.equalsIgnoreCase(buscarValorKey)) {
                return (Map<String, Object>) hash.get(key);
            }

            if (hash.get(key) instanceof Map) {
                Map<String, Object> nuevo = (Map<String, Object>) hash.get(key);
                return revisaHash(nuevo, buscarValorKey);
            }
        }
        return null;
    }

    private <T> String dtoToString(T dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
        }
        return null;
    }
}
