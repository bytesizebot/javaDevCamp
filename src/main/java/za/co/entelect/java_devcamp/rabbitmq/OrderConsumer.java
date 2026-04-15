package za.co.entelect.java_devcamp.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import za.co.entelect.java_devcamp.configs.RabbitConfig;
import za.co.entelect.java_devcamp.request.FulfilmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.serviceinterface.IOrderOrchestrationService;

@Slf4j
@Component
public class OrderConsumer {
    private final IOrderOrchestrationService iOrderOrchestrationService;

    public OrderConsumer(IOrderOrchestrationService iOrderOrchestrationService) {
        this.iOrderOrchestrationService = iOrderOrchestrationService;
    }

    @RabbitListener(queues = RabbitConfig.PRODUCT_FULFILMENT)
    public FulfilmentResponse handleFulfilment(FulfilmentRequest request) {
        log.info("Fulfilling the request: " + request.getFulfillmentType());

        String fulfilmentType = request.getFulfillmentType();
        FulfilmentResponse fulfilmentResponse = null;
        switch (fulfilmentType) {
            case "A":
                fulfilmentResponse = iOrderOrchestrationService.completeTypeAProcess(request);
                break;
            case "B":
                fulfilmentResponse = iOrderOrchestrationService.completeTypeBProcess(request);
                break;
            case "C":
                fulfilmentResponse = iOrderOrchestrationService.completeTypeCProcess(request);
                break;
            default:
                throw new IllegalStateException("Unknown fulfillment check type.");
        }

        return fulfilmentResponse;
    }

}
