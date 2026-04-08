package za.co.entelect.java_devcamp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.RabbitConfig;
import za.co.entelect.java_devcamp.entity.Order;
import za.co.entelect.java_devcamp.entity.OrderItem;
import za.co.entelect.java_devcamp.request.FulfillmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.serviceinterface.IFulfilmentService;
import za.co.entelect.java_devcamp.util.ActionCompletedFulfilmentChecks;
import za.co.entelect.java_devcamp.webclientdto.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class FulfilmentService implements IFulfilmentService {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    @Override
    public void determineFulfillmentCheck(Order order, Long customerId, String customerIdNumber) {
        List<OrderItem> orderItems = order.getOrderItems();
        String correlationId = UUID.randomUUID().toString();

        for (OrderItem product : orderItems) {
            String checkType = product.getProduct().getFulfilmentType().getName();
            FulfillmentRequest fulfillmentRequest = new FulfillmentRequest(customerId, customerIdNumber,"",order.getOrderId(), correlationId);

            switch (checkType) {
                case "A":
                    fulfillmentRequest.setFulfillmentType("A");
                    doTypeAChecks(fulfillmentRequest);
                    break;
                case "B":
                    fulfillmentRequest.setFulfillmentType("C");
                    doTypeBChecks(fulfillmentRequest);
                    break;
                case "C":
                    fulfillmentRequest.setFulfillmentType("C");
                    doTypeCChecks(fulfillmentRequest);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void doTypeAChecks(FulfillmentRequest request) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(request);
            rabbitTemplate.convertAndSend(RabbitConfig.KYC_QUEUE, jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doTypeBChecks(FulfillmentRequest request) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(request);
            doTypeAChecks(request);
            rabbitTemplate.convertAndSend(RabbitConfig.DHA_QUEUE, jsonMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //Fraud
    }

    @Override
    public void doTypeCChecks(FulfillmentRequest request) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(request);
            doTypeBChecks(request);
            rabbitTemplate.convertAndSend(RabbitConfig.CC_QUEUE, request);
            //Credit check
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processTypeCChecks() {

    }

    @Override
    public void processTypeBChecks() {

    }

    @Override
    public void processTypeACheck(KYCCheckDto response, FulfilmentResponse fulfilmentResponse) {
        Set<TaxCompliance> compliant = Set.of(TaxCompliance.amber, TaxCompliance.green);

        if(response.isPrimaryIndicator() && compliant.contains(response.getTaxCompliance())){
            fulfilmentResponse.setSuccessful(true);
            //notification placeholder
            log.info("Continue process");
        }
        //notification placeholder
        eventPublisher.publishEvent(new ActionCompletedFulfilmentChecks(fulfilmentResponse));
    }

    @Override
    public void processFraudCheckResponse() {

    }

    @Override
    public void processLivingStatusCheck(LivingStatus response, FulfilmentResponse fulfilmentResponse) {

    }

    @Override
    public void processDuplicateIdCheck(DuplicateIDDocumentCheck response, FulfilmentResponse fulfilmentResponse) {

    }

    @Override
    public void processMaritalStatusCheck(MaritalStatusResponse response, FulfilmentResponse fulfilmentResponse) {

    }

    @Override
    public void processCreditCheck() {

    }
}
