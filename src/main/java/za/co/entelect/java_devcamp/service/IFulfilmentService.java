package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.request.FulfillmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.webclientdto.DuplicateIdCheckDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.webclientdto.LivingStatusCheckDto;
import za.co.entelect.java_devcamp.webclientdto.MaritalStatusDto;

public interface IFulfilmentService {
    void determineFulfillmentCheck(Order order, Long customerId);
    void doTypeAChecks(FulfillmentRequest request);
    void doTypeBChecks(FulfillmentRequest request);
    void doTypeCChecks(FulfillmentRequest request);

    //process fulfilment check responses
    void processKYCCheckResponse(KYCCheckDto response, FulfilmentResponse fulfilmentResponse);
    void processFraudCheckResponse();
    void processLivingStatusCheck(LivingStatusCheckDto response);
    void processDuplicateIdCheck(DuplicateIdCheckDto response);
    void processMaritalStatusCheck(MaritalStatusDto response);
    void processCreditCheck();

}
