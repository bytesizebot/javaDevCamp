//package za.co.entelect.java_devcamp.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.openapitools.model.DuplicateIDDocumentCheck;
//import org.openapitools.model.LivingStatus;
//import org.openapitools.model.MaritalStatusResponse;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import za.co.entelect.java_devcamp.configs.RabbitConfig;
//import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
//import za.co.entelect.java_devcamp.entity.Order;
//import za.co.entelect.java_devcamp.entity.OrderItem;
//import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;
//import za.co.entelect.java_devcamp.model.Result;
//import za.co.entelect.java_devcamp.request.FulfilmentRequest;
//import za.co.entelect.java_devcamp.response.FulfilmentResponse;
//import za.co.entelect.java_devcamp.serviceinterface.IFulfilmentService;
//import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
//import za.co.entelect.java_devcamp.webclientdto.TaxCompliance;
//
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@AllArgsConstructor
//@Slf4j
//@Service
//public class FulfilmentService implements IFulfilmentService {
//
//    private final RabbitTemplate rabbitTemplate;
//    private final ApplicationEventPublisher eventPublisher;
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void determineFulfillmentCheck(Order order, Long customerId, String customerIdNumber) {
//        List<OrderItem> orderItems = order.getOrderItems();
//        String correlationId = UUID.randomUUID().toString();
//
//        for (OrderItem product : orderItems) {
//            String checkType = product.getProduct().getFulfilmentType().getName();
//            FulfilmentRequest fulfilmentRequest = new FulfilmentRequest(customerId, customerIdNumber, "", order.getOrderId(), correlationId);
//
//            switch (checkType) {
//                case "A":
//                    fulfilmentRequest.setFulfillmentType("A");
//                    doTypeAChecks(fulfilmentRequest);
//                    break;
//                case "B":
//                    fulfilmentRequest.setFulfillmentType("B");
//                    doTypeBChecks(fulfilmentRequest);
//                    break;
//                case "C":
//                    fulfilmentRequest.setFulfillmentType("C");
//                    doTypeCChecks(fulfilmentRequest);
//                    break;
//                default:
//                    throw new IllegalStateException("Unknown fulfillment check type.");
//            }
//        }
//    }
//
//    @Override
//    public void doTypeAChecks(FulfilmentRequest request) {
//
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(request);
//            rabbitTemplate.convertAndSend(RabbitConfig.KYC_QUEUE, jsonMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void doTypeBChecks(FulfilmentRequest request) {
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(request);
//            rabbitTemplate.convertAndSend(RabbitConfig.KYC_QUEUE, jsonMessage);
//            rabbitTemplate.convertAndSend(RabbitConfig.DHA_QUEUE, jsonMessage);
//            rabbitTemplate.convertAndSend(RabbitConfig.FRAUD_QUEUE, jsonMessage);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void doTypeCChecks(FulfilmentRequest request) {
//        try {
//            String jsonMessage = objectMapper.writeValueAsString(request);
//            rabbitTemplate.convertAndSend(RabbitConfig.KYC_QUEUE, jsonMessage);
//            rabbitTemplate.convertAndSend(RabbitConfig.DHA_QUEUE, jsonMessage);
//            rabbitTemplate.convertAndSend(RabbitConfig.CC_QUEUE, jsonMessage);
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    //Process all the messages from queues
//    @Override
//    public void processKYCCheck(KYCCheckDto response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            Set<TaxCompliance> compliant = Set.of(TaxCompliance.amber, TaxCompliance.green);
//
//            Result kycResult = new Result(fulfilmentResponse.getCorrelationId(), "KYC Service", "KYC check", "",
//                    fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if (response.isPrimaryIndicator() && compliant.contains(response.getTaxCompliance())) {
//                log.info("Customer is tax compliant. Check passes");
//                kycResult.setStatus("PASS");
//            } else {
//                kycResult.setStatus("FAIL");
//                log.info("Customer is not tax compliant. Check failed");
//            }
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(kycResult);
//
//                switch (fulfillmentType) {
//                    case "A":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_A_QUEUE, jsonMessage);
//                        break;
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        log.info("Something went with processing the kyc status. Try again later");
//
//    }
//
//    @Override
//    public void processFraudCheck(FraudCheckResponse response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            Result fraudResult = new Result(fulfilmentResponse.getCorrelationId(), "Fraud  Service", "Fraud check", "",
//                    fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if (response.getBankStatus().equals("Active") && response.getNationalStatus().equals("Valid")) {
//                fraudResult.setStatus("PASS");
//                log.info("Customer has no fraudulent activities. Check passes");
//            }else{
//                fraudResult.setStatus("FAIL");
//                log.info("Customer has been flagged for fraudulent activities. Check fails");
//            }
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(fraudResult);
//                switch (fulfillmentType) {
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        log.info("Something went with processing the fraud status. Try again later");
//    }
//
//    @Override
//    public void processLivingStatusCheck(LivingStatus response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            Result livingResult = new Result(fulfilmentResponse.getCorrelationId(), "DHA Service", "Living status check", "",
//                    fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if (response.getLivingStatus().getValue().equals("Alive")) {
//                livingResult.setStatus("PASS");
//                log.info("Customer is alive and hopefully well. Check passes");
//
//            }else {
//                livingResult.setStatus("FAIL");
//                log.info("Customer living status check is undesirable. Check fails");
//            }
//
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(livingResult);
//                switch (fulfillmentType) {
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        log.info("Something went with processing the living status. Try again later");
//    }
//
//    @Override
//    public void processDuplicateIdCheck(DuplicateIDDocumentCheck response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            Result duplicateResult = new Result(fulfilmentResponse.getCorrelationId(), "DHA Service", "Duplicate Id check", "",
//                    fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if (response.getHasDuplicateId().equals(false)) {
//                duplicateResult.setStatus("PASS");
//                log.info("Customer has no duplicate Id. Check passes");
//            }else {
//                duplicateResult.setStatus("FAIL");
//                log.info("Customer has a duplicate Id number. Check fails");
//            }
//
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(duplicateResult);
//                switch (fulfillmentType) {
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        log.info("Something went wrong with processing the duplicate Id. Please try again later");
//    }
//
//    @Override
//    public void processMaritalStatusCheck(MaritalStatusResponse response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            String status = response.getCurrentStatus() != null ? String.valueOf(response.getCurrentStatus().getStatus()) : "";
//
//            Result maritalResult = new Result(fulfilmentResponse.getCorrelationId(), "DHA Service", "Marital status check", "", fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if ("Married".equals(status != null ? status.trim() : "")) {
//                log.info("Customer is married. Check passes");
//                maritalResult.setStatus("PASS");
//            }else{
//                log.info("Customer is not married. Check fails");
//                maritalResult.setStatus("FAIL");
//            }
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(maritalResult);
//                switch (fulfillmentType) {
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        log.info("Something went wrong with the martial status check. Please try again later");
//    }
//
//    @Override
//    public void processCreditCheck(CreditCheckResponse response, FulfilmentResponse fulfilmentResponse) {
//        if (response != null) {
//            Result creditResult = new Result(fulfilmentResponse.getCorrelationId(), "Credit Service", "Credit status check", "",
//                    fulfilmentResponse.getOrderId(), fulfilmentResponse.getCorrelationId(), fulfilmentResponse.getCustomerId(), fulfilmentResponse.getFulfillmentType(), fulfilmentResponse.isSuccessful());
//            String fulfillmentType = fulfilmentResponse.getFulfillmentType();
//
//            if (response.getCreditCheckResult().equals("RED")) {
//                creditResult.setStatus("FAIL");
//                log.info("Customer has undesirable credit status. Check fails");
//            }else {
//                creditResult.setStatus("PASS");
//                log.info("Customer has a desirable credit status. Check passes");
//            }
//
//            try {
//                String jsonMessage = objectMapper.writeValueAsString(creditResult);
//                switch (fulfillmentType) {
//                    case "B":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_B_QUEUE, jsonMessage);
//                        break;
//                    case "C":
//                        rabbitTemplate.convertAndSend(RabbitConfig.RESULT_C_QUEUE, jsonMessage);
//                        break;
//                    default:
//                        throw new IllegalStateException("Unknown fulfillment check type.");
//                }
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            log.info("Something went wrong with credit check. Please try again later");
//        }
//
//    }
//}
