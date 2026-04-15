package za.co.entelect.java_devcamp.rabbitmq;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.RabbitConfig;

@Service
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, message);
    }

    public void sendTestMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_FULFILMENT, message);
        System.out.println("Sent test message directly to queue: " + message);
    }
}
