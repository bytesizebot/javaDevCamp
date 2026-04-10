package za.co.entelect.java_devcamp.configs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRabbit
@EnableRetry
public class RabbitConfig {

    public static final String QUEUE_NAME = "ProductFulfilment";

    //rabbit queues
    public static final String KYC_QUEUE = "kycCheckQueue";
    public static final String DHA_QUEUE = "dhaCheckQueue";
    public static final String CC_QUEUE = "creditCheckQueue";
    public static final String FRAUD_QUEUE = "fraudCheckQueue";
    public static final String RETRY_QUEUE = "fulfilmentCheck.retry.queue";
    public static final String DLQ = "Fulfilment.dlq";
    public static final String RESULT_A_QUEUE = "fulfillmentTypeAQueue";
    public static final String RESULT_B_QUEUE = "fulfillmentTypeBQueue";
    public static final String RESULT_C_QUEUE = "fulfillmentTypeCQueue";

    //exchanges
    public static final String EXCHANGE_NAME = "FulfilmentExchange";
    public static final String DLX_EXCHANGE = "dlx-exchange";

    //routing key
    public static final String ROUTING_KEY = "fulfill.checks";

    //Queues
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue kycQueue() {
        return new Queue(KYC_QUEUE, true);
    }

    @Bean
    public Queue dhaQueue() {
        return new Queue(DHA_QUEUE, true);
    }

    @Bean
    public Queue ccQueue() {
        return new Queue(CC_QUEUE, true);
    }

    @Bean
    public Queue fraudQueue() {
        return new Queue(FRAUD_QUEUE, true);
    }

    @Bean
    public Queue resultAQueue() {
        return new Queue(RESULT_A_QUEUE, true);
    }
    @Bean
    public Queue resultBQueue() {
        return new Queue(RESULT_B_QUEUE, true);
    }
    @Bean
    public Queue resultCQueue() {
        return new Queue(RESULT_C_QUEUE, true);
    }




    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(RETRY_QUEUE).withArgument("x-dead-letter-exchange", EXCHANGE_NAME).withArgument("x-dead-letter-routing-key", ROUTING_KEY).withArgument("x-message-ttl", 5000) // 5 sec retry delay
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue()).to(dlxExchange()).with("retry");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(dlxExchange()).with("dlq");
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable("delayed-queue").withArgument("x-dead-letter-exchange", "").withArgument("x-dead-letter-routing-key", "my-queue").withArgument("x-message-ttl", 5000) // 5000 ms = 5 seconds
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
