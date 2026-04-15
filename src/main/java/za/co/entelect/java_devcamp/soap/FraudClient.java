package za.co.entelect.java_devcamp.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckRequest;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;

import java.util.Random;


@Slf4j
@Component
public class FraudClient extends WebServiceGatewaySupport {
    @Value("${fraud.callback}")
    private String soapActionCallback;
    @Value("${fraud.url}")
    private String url;

    private Random random = new Random();

    @Retryable(retryFor = {ResourceAccessException.class, HttpServerErrorException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500))
    public FraudCheckResponse getFraudCheck(Integer customerId, String IdNumber){
        FraudCheckRequest request = new FraudCheckRequest();
        request.setCustomerId(customerId);
        request.setIdNumber(IdNumber);
        log.info("Requesting fraud check for customer");

//        FraudCheckResponse response = (FraudCheckResponse) getWebServiceTemplate()
//                .marshalSendAndReceive(url, request);

FraudCheckResponse response = new FraudCheckResponse();
        if (random.nextInt(100) < 80) {
            response.setBankStatus("Active");
            response.setNationalStatus("Valid");
        } else {
            response.setBankStatus("Overdrawn");
            response.setNationalStatus("Invalid");
        }

        return response;
    }
}
