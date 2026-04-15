//package za.co.entelect.java_devcamp.serviceinterface;
//
//import org.openapitools.model.DuplicateIDDocumentCheck;
//import org.openapitools.model.LivingStatus;
//import org.openapitools.model.MaritalStatusResponse;
//import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
//import za.co.entelect.java_devcamp.entity.Order;
//import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;
//import za.co.entelect.java_devcamp.request.FulfilmentRequest;
//import za.co.entelect.java_devcamp.response.FulfilmentResponse;
//import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
//
//public interface IFulfilmentService {
//    void determineFulfillmentCheck(Order order, Long customerId, String customerIdNumber);
//
//    //send the messages
//    void doTypeAChecks(FulfilmentRequest request);
//    void doTypeBChecks(FulfilmentRequest request);
//    void doTypeCChecks(FulfilmentRequest request);
//
//    //process fulfilment check responses
//    void processKYCCheck(KYCCheckDto response, FulfilmentResponse fulfilmentResponse);
//    void processFraudCheck(FraudCheckResponse response, FulfilmentResponse fulfilmentResponse);
//    void processLivingStatusCheck(LivingStatus response, FulfilmentResponse fulfilmentResponse);
//    void processDuplicateIdCheck(DuplicateIDDocumentCheck response, FulfilmentResponse fulfilmentResponse);
//    void processMaritalStatusCheck(MaritalStatusResponse response, FulfilmentResponse fulfilmentResponse);
//    void processCreditCheck(CreditCheckResponse response, FulfilmentResponse fulfilmentResponse);
//
//
//
//}
