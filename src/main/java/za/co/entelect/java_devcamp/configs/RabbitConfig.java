package za.co.entelect.java_devcamp.configs;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRabbit
@EnableRetry
public class RabbitConfig {

    public static final String PRODUCT_FULFILMENT = "ProductFulfilment";
    public static final String REPLY_QUEUE = "ProductFulfilmentReply";

    //rabbit queues
//    public static final String KYC_QUEUE = "kycCheckQueue";
//    public static final String DHA_QUEUE = "dhaCheckQueue";
//    public static final String CC_QUEUE = "creditCheckQueue";
//    public static final String FRAUD_QUEUE = "fraudCheckQueue";
//    public static final String RESULT_A_QUEUE = "fulfillmentTypeAQueue";
//    public static final String RESULT_B_QUEUE = "fulfillmentTypeBQueue";
//    public static final String RESULT_C_QUEUE = "fulfillmentTypeCQueue";

    //exchanges
    public static final String EXCHANGE_NAME = "FulfilmentExchange";

    //routing key
    public static final String ROUTING_KEY = "fulfill.checks";

    //Queues
    @Bean
    public Queue fulfilmentQueue() {
        return new Queue(PRODUCT_FULFILMENT, true);
    }

//    @Bean
//    public Queue kycQueue() {
//        return new Queue(KYC_QUEUE, true);
//    }
//
//    @Bean
//    public Queue dhaQueue() {
//        return new Queue(DHA_QUEUE, true);
//    }
//
//    @Bean
//    public Queue ccQueue() {
//        return new Queue(CC_QUEUE, true);
//    }
//
//    @Bean
//    public Queue fraudQueue() {
//        return new Queue(FRAUD_QUEUE, true);
//    }
//
//    @Bean
//    public Queue resultAQueue() {
//        return new Queue(RESULT_A_QUEUE, true);
//    }
//
//    @Bean
//    public Queue resultBQueue() {
//        return new Queue(RESULT_B_QUEUE, true);
//    }
//
//    @Bean
//    public Queue resultCQueue() {
//        return new Queue(RESULT_C_QUEUE, true);
//    }

    @Bean
    public Queue replyQueue() {
        return new Queue(REPLY_QUEUE);
    }

    @Bean
    public TopicExchange fulfilmentExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue fulfilmentQueue, TopicExchange fulfilmentExchange) {
        return BindingBuilder.bind(fulfilmentQueue).to(fulfilmentExchange).with(ROUTING_KEY);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(60000); // 5 seconds
        return template;
    }


}
