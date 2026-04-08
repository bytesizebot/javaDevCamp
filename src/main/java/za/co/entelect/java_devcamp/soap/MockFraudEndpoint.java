package za.co.entelect.java_devcamp.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckRequest;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;

import java.util.Random;

@Endpoint
public class MockFraudEndpoint {

    private static final String NAMESPACE = "http://localhost:8085/fraudcheck";
    private Random random = new Random();

    @PayloadRoot(namespace = NAMESPACE, localPart = "fraudCheckRequest")
    @ResponsePayload
    public FraudCheckResponse handleCreditCheck(@RequestPayload FraudCheckRequest request) {
        FraudCheckResponse response = new FraudCheckResponse();

        // Randomized response
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