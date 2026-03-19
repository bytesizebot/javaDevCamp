package za.co.entelect.java_devcamp.webclient;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KYCService {

    private final WebClient kycClient;

    public KYCService(WebClient.Builder kycClientBuilder){
        this.kycClient = kycClientBuilder
                .baseUrl("")
                .build();
    }


}
