package za.co.entelect.java_devcamp.configs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;

@Configuration
@EnableRabbit
@EnableRetry
public class RabbitConfig {

    public static final String QUEUE_NAME = "ProductFulfilment";

    //rabbit queues
    public static final String KYC_QUEUE = "kycCheckQueue";
    public static final String DHA_QUEUE = "dhaCheckQueue";
    public static final String CC_QUEUE = "creditCheckQueue";
    public static final String EXCHANGE_NAME = "FulfilmentExchange";

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
    public Queue mainQueue() {
        return QueueBuilder.durable("mainQueue")
                .withArgument("x-dead-letter-exchange", "dlxExchange")
                .withArgument("x-dead-letter-routing-key", "dlq")
                .build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("routing.key.#");
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable("delayed-queue")
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "my-queue")
                .withArgument("x-message-ttl", 5000) // 5000 ms = 5 seconds
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }



}
