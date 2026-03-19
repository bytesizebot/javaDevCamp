package za.co.entelect.java_devcamp.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import za.co.entelect.java_devcamp.customerdto.CustomerDto;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Slf4j
@Service
public class CISWebService {

    private final WebClient webClient;
    String username = "admin@entelect.co.za";
    String password = "password";

    public CISWebService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(basicAuthentication(username, password))
                .build();
    }

    public CustomerDto getCustomerByEmail(String email, String token){
        log.info("Attempting to retrieve customer with email: " + email);
        try {
            return webClient.get()
                    .uri("/v1/customer?emailAddress={email}", email)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(CustomerDto.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
            throw e;
        }
    }
}
