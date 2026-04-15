package za.co.entelect.java_devcamp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;
import za.co.entelect.java_devcamp.rabbitmq.OrderProducer;
import za.co.entelect.java_devcamp.request.FulfilmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.serviceinterface.IOrderOrchestrationService;
import za.co.entelect.java_devcamp.serviceinterface.IOrderService;
import za.co.entelect.java_devcamp.soap.CreditClient;
import za.co.entelect.java_devcamp.soap.FraudClient;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.webclientdto.TaxCompliance;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class OrderOrchestrationService implements IOrderOrchestrationService {
    private final CreditClient creditClient;
    private final FraudClient fraudClient;
    private final WebService webService;
    private final OrderProducer orderProducer;

    @Override
    public FulfilmentResponse completeTypeAProcess(FulfilmentRequest request) {
        KYCCheckDto kycResponse = webService.getCustomerKYC(request.getCustomerId());
        boolean isSuccess = false;
        if (kycResponse != null) {
            isSuccess = processKYCResponse(kycResponse);
        }

        FulfilmentResponse response = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getCustomerIdNumber(), request.getFulfillmentType(), isSuccess, "");
        if (isSuccess) response.setFailureReason("");
        else response.setFailureReason("KYC check failed.");

        return response;
    }

    @Override
    public FulfilmentResponse completeTypeBProcess(FulfilmentRequest request) {
        String failureReason = "";
        FulfilmentResponse response = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getCustomerIdNumber(), request.getFulfillmentType(), false, "");

        KYCCheckDto kycResponse = webService.getCustomerKYC(request.getCustomerId());
        if (kycResponse == null || !processKYCResponse(kycResponse)) {
            failureReason = "KYC check failed. ";
            return response;
        }

        LivingStatus livingStatus = webService.getCustomerLivingStatus(request.getCustomerIdNumber());
        if (livingStatus == null || !processLivingResponse(livingStatus)) {
            failureReason = "Living status check failed. ";
            return response;
        }

        DuplicateIDDocumentCheck duplicateIDDocumentCheck = webService.getCustomerDuplicateIDStatus(request.getCustomerIdNumber());
        if (duplicateIDDocumentCheck == null || !processDuplicateIdCheck(duplicateIDDocumentCheck)) {
            failureReason = "Duplicate ID check failed. ";
            return response;
        }

        FraudCheckResponse fraudCheckResponse = fraudClient.getFraudCheck(request.getCustomerId().intValue(), request.getCustomerIdNumber());
        if (fraudCheckResponse == null || !processFraudCheckResponse(fraudCheckResponse)) {
            failureReason = "Fraud status check failed. ";
            return response;
        }

        response.setSuccessful(true);
        return response;
    }

    @Override
    public FulfilmentResponse completeTypeCProcess(FulfilmentRequest request) {
        String failureReason = "";
        FulfilmentResponse response = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getCustomerIdNumber(), request.getFulfillmentType(), false, "");

        KYCCheckDto kycResponse = webService.getCustomerKYC(request.getCustomerId());
        if (kycResponse == null || !processKYCResponse(kycResponse)) {
            failureReason = "KYC check failed. ";
            return response;
        }


        LivingStatus livingStatus = webService.getCustomerLivingStatus(request.getCustomerIdNumber());
        if (livingStatus == null || !processLivingResponse(livingStatus)) {
            failureReason = "Living status check failed. ";
            return response;
        }

        DuplicateIDDocumentCheck duplicateIDDocumentCheck = webService.getCustomerDuplicateIDStatus(request.getCustomerIdNumber());
        if (duplicateIDDocumentCheck == null || !processDuplicateIdCheck(duplicateIDDocumentCheck)) {
            failureReason = "Duplicate ID check failed. ";
            return response;
        }

        MaritalStatusResponse maritalStatusResponse = webService.getCustomerMaritalStatus(request.getCustomerIdNumber());
        if (maritalStatusResponse == null || !processMaritalStatusCheck(maritalStatusResponse)) {
            failureReason = "Marital status check failed. ";
            return response;
        }

        FraudCheckResponse fraudCheckResponse = fraudClient.getFraudCheck(request.getCustomerId().intValue(), request.getCustomerIdNumber());
        if (fraudCheckResponse == null || !processFraudCheckResponse(fraudCheckResponse)) {
            failureReason = "Fraud status check failed. ";
            return response;
        }

        CreditCheckResponse creditCheckResponse = creditClient.getCreditCheck(request.getCustomerId().intValue());
        if (creditCheckResponse == null || !processCreditCheck(creditCheckResponse)) {
            failureReason = "Credit check failed. ";
            return response;
        }

        response.setSuccessful(true);
        return response;
    }

    public boolean processKYCResponse(KYCCheckDto kycResponse) {
        Set<TaxCompliance> compliant = Set.of(TaxCompliance.amber, TaxCompliance.green);
        if (kycResponse.isPrimaryIndicator() && compliant.contains(kycResponse.getTaxCompliance())) {
            log.info("Customer is tax compliant. Check passes");
            return true;
        } else {
            log.info("Customer is not tax compliant. Check failed");
            return false;
        }
    }

    public boolean processLivingResponse(LivingStatus livingStatus) {
        if (livingStatus.getLivingStatus().getValue().equals("Alive")) {
            log.info("Customer is alive and hopefully well. Check passes");
            return true;
        } else {
            log.info("Customer living status check is undesirable. Check fails");
            return false;
        }
    }

    public boolean processFraudCheckResponse(FraudCheckResponse response) {
        if (response.getBankStatus().equals("Active") && response.getNationalStatus().equals("Valid")) {
            log.info("Customer has no fraudulent activities. Check passes");
            return true;
        } else {
            log.info("Customer has been flagged for fraudulent activities. Check fails");
            return false;
        }
    }

    public boolean processDuplicateIdCheck(DuplicateIDDocumentCheck response) {
        if (response.getHasDuplicateId().equals(false)) {
            log.info("Customer has no duplicate Id. Check passes");
            return true;
        } else {
            log.info("Customer has a duplicate Id number. Check fails");
            return false;
        }
    }

    public boolean processMaritalStatusCheck(MaritalStatusResponse response) {
        String status = response.getCurrentStatus() != null ? String.valueOf(response.getCurrentStatus().getStatus()) : "";

        if ("Married".equals(status != null ? status.trim() : "")) {
            log.info("Customer is married. Check passes");
            return true;
        } else {
            log.info("Customer is not married. Check fails");
            return false;
        }
    }

    public boolean processCreditCheck(CreditCheckResponse response) {
        if (response.getCreditCheckResult().equals("RED")) {
            log.info("Customer has undesirable credit status. Check fails");
            return false;
        } else {
            log.info("Customer has a desirable credit status. Check passes");
            return true;
        }
    }
}
