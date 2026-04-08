package za.co.entelect.java_devcamp.webclient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import za.co.entelect.java_devcamp.configs.TokenStore;
import za.co.entelect.java_devcamp.webclientdto.DuplicateIdCheckDto;
import za.co.entelect.java_devcamp.webclientdto.LivingStatusCheckDto;
import za.co.entelect.java_devcamp.webclientdto.MaritalStatusCheckDto;

@Slf4j
@Service
public class DHAWebService {

    private final WebClient dhaClient;
    private final TokenStore tokenStore;

    public DHAWebService(WebClient.Builder dhaClientBuilder, HttpServletRequest request, TokenStore tokenStore) {
        this.dhaClient = dhaClientBuilder
                .baseUrl("http://localhost:8082")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.tokenStore = tokenStore;
    }

    public DuplicateIDDocumentCheck getDuplicateId(String customerId) {
        log.info("Getting customer duplicate ID information");
        try {
            return dhaClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/status/duplicateId/{idNumber}")
                            .build(customerId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStore.getToken())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.createException()
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response ->
                                    response.bodyToMono(String.class)
                                            .flatMap(body -> {
                                                log.error("5xx error: {}", body);
                                                return response.createException();
                                            })
                    )
                    .bodyToMono(DuplicateIDDocumentCheck.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {

                        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.empty();
                        }

                        if (ex.getStatusCode().is5xxServerError()) {
                            log.error("Server error: {}", ex.getResponseBodyAsString());
                            return Mono.empty();
                        }
                        return Mono.error(ex);
                    })
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

    public LivingStatus getLivingStatus(String customerId) {
        log.info("Getting customer living status information");
        try {
            return dhaClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/status/living/{idNumber}")
                            .build(customerId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStore.getToken())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.createException()
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response ->
                                    response.bodyToMono(String.class)
                                            .flatMap(body -> {
                                                log.error("5xx error: {}", body);
                                                return response.createException();
                                            })
                    )
                    .bodyToMono(LivingStatus.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {

                        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.empty();
                        }

                        if (ex.getStatusCode().is5xxServerError()) {
                            log.error("Server error: {}", ex.getResponseBodyAsString());
                            return Mono.empty();
                        }
                        return Mono.error(ex);
                    })
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

    public MaritalStatusResponse getMaritalStatus(String customerId) {
        log.info("Getting customer marital status information");
        try {
            return dhaClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/status/marital/{idNumber}")
                            .build(customerId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStore.getToken())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.createException()
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response ->
                                    response.bodyToMono(String.class)
                                            .flatMap(body -> {
                                                log.error("5xx error: {}", body);
                                                return response.createException();
                                            })
                    )
                    .bodyToMono(MaritalStatusResponse.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.empty();
                        }

                        if (ex.getStatusCode().is5xxServerError()) {
                            log.error("Server error: {}", ex.getResponseBodyAsString());
                            return Mono.empty();
                        }
                        return Mono.error(ex);
                    })
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

}
