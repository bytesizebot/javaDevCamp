package za.co.entelect.java_devcamp.serviceinterface;

import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.request.FulfillmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.webclientdto.DuplicateIdCheckDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.webclientdto.LivingStatusCheckDto;
import za.co.entelect.java_devcamp.webclientdto.MaritalStatusCheckDto;

public interface IFulfilmentService {
    void determineFulfillmentCheck(Order order, Long customerId, String customerIdNumber);
    void doTypeAChecks(FulfillmentRequest request);
    void doTypeBChecks(FulfillmentRequest request);
    void doTypeCChecks(FulfillmentRequest request);

    //process fulfilment check responses

    void processTypeCChecks();
    void processTypeBChecks();
    void processTypeACheck(KYCCheckDto response, FulfilmentResponse fulfilmentResponse);
    void processFraudCheckResponse();
    void processLivingStatusCheck(LivingStatus response, FulfilmentResponse fulfilmentResponse);
    void processDuplicateIdCheck(DuplicateIDDocumentCheck response, FulfilmentResponse fulfilmentResponse);
    void processMaritalStatusCheck(MaritalStatusResponse response, FulfilmentResponse fulfilmentResponse);
    void processCreditCheck();

}
