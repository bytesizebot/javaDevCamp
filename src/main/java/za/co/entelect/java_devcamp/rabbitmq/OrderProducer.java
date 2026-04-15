package za.co.entelect.java_devcamp.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.RabbitConfig;
import za.co.entelect.java_devcamp.request.FulfilmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;

@AllArgsConstructor
@Slf4j
@Service
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public FulfilmentResponse sendOrderForFulfilment(FulfilmentRequest request){
        log.info("Sending order to be fulfilled...");
        return (FulfilmentResponse) rabbitTemplate.convertSendAndReceive(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, request);
    }

}
