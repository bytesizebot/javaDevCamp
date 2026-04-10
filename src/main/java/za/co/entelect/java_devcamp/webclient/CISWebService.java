package za.co.entelect.java_devcamp.webclient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.util.MaskingUtils;
import za.co.entelect.java_devcamp.webclientdto.CustomerAccountDto;
import za.co.entelect.java_devcamp.webclientdto.CustomerDto;

@Slf4j
@Service
public class CISWebService {

    private final WebClient webClient;
    private final HttpServletRequest request;

    public CISWebService(WebClient.Builder webClientBuilder, HttpServletRequest request) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.request = request;
    }

    public CustomerDto getCustomerByEmail(String email) {
        log.info("Attempting to retrieve customer with email: " + MaskingUtils.maskEmail(email));

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/customer")
                            .queryParam("emailAddress", email)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> {
                                if (response.statusCode() == HttpStatus.NOT_FOUND) {
                                    return Mono.empty();
                                }
                                return response.createException();
                            }
                    )
                    .bodyToMono(CustomerDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

    public CustomerDto getCustomerById(Long Id) {
        log.info("Attempting to retrieve customer Id");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/customer/")
                            .queryParam("customer_id", Id)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError(),
                            response -> {
                                if (response.statusCode() == HttpStatus.NOT_FOUND) {
                                    return Mono.empty();
                                }
                                return response.createException();
                            }
                    )
                    .bodyToMono(CustomerDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

    public ProfileDto registerCustomer(ProfileDto profileDto) {
        log.info("Registering a profile with the cis");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Bearer token used:" + authHeader);

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/customer")
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(profileDto)
                    .retrieve()
                    .bodyToMono(ProfileDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }

    public CustomerAccountDto addAccountToCustomer(CustomerAccountDto customerAccountDto) {
        log.info("Registering a profile with the cis");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Bearer token used:" + authHeader);

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/customer/{customerId}/accounts/{accountTypeId}")
                            .build(
                                    customerAccountDto.getCustomerId(),
                                    customerAccountDto.getAccountId()
                            ))
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(CustomerAccountDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }
}

