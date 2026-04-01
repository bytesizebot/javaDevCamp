package za.co.entelect.java_devcamp.webclient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import za.co.entelect.java_devcamp.configs.TokenStore;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

@Slf4j
@Service
public class KYCWebService {

    private final WebClient kycClient;
    private final HttpServletRequest request;
    private final TokenStore tokenStore;

    public KYCWebService(WebClient.Builder kycClientBuilder, HttpServletRequest request, TokenStore tokenStore){
        this.kycClient = kycClientBuilder
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.request = request;
        this.tokenStore = tokenStore;
    }

    public KYCCheckDto getCustomerKYC(Long customerId){
        log.info("Getting customer KYC information");


       // String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try{
            return kycClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/kyc/{customerId}")
                            .build(customerId))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenStore.getToken())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> response.createException() // always throw
                    )
                    .onStatus(
                            status -> status.is5xxServerError(),
                            response ->
                                    response.bodyToMono(String.class)
                                            .flatMap(body -> {
                                                log.error("5xx error: {}", body);
                                                return response.createException(); // keep consistent
                                            })
                    )
                    .bodyToMono(KYCCheckDto.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {

                        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.empty(); // 👈 correct place for 404 handling
                        }

                        if (ex.getStatusCode().is5xxServerError()) {
                            log.error("Server error: {}", ex.getResponseBodyAsString());
                            return Mono.empty(); // or fallback
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
