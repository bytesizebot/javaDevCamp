package za.co.entelect.java_devcamp.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import za.co.entelect.java_devcamp.creditcheck.CreditCheck;
import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
import za.co.entelect.java_devcamp.creditcheck.ICreditCheckService;
import za.co.entelect.java_devcamp.creditcheck.ICreditCheckService_Service;

@Slf4j
@Component
public class CreditClient extends WebServiceGatewaySupport {
    @Value("${credit.callback}")
    private String soapActionCallback;
    @Value("${credit.url}")
    private String url;

    public CreditCheckResponse getCreditCheck(Integer customerId) {
        CreditCheck request = new CreditCheck();
        request.setCustomerId(customerId);

        log.info("Requesting credit check for customer");

        CreditCheckResponse response = (CreditCheckResponse) getWebServiceTemplate()
                .marshalSendAndReceive(url, request,
                        new SoapActionCallback(soapActionCallback
                        ));
        return response;
    }
}
