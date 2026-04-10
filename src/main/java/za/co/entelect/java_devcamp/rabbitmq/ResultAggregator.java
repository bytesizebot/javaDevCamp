package za.co.entelect.java_devcamp.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.RabbitConfig;
import za.co.entelect.java_devcamp.model.Result;
import za.co.entelect.java_devcamp.model.TypeAMessage;
import za.co.entelect.java_devcamp.request.FulfillmentRequest;
import za.co.entelect.java_devcamp.response.FulfilmentResponse;
import za.co.entelect.java_devcamp.serviceinterface.IOrderService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ResultAggregator {
    private final Map<String, Map<String, Result>> store = new ConcurrentHashMap<>();
    private final IOrderService iOrderService;
    private final ObjectMapper objectMapper;

    public ResultAggregator(IOrderService iOrderService, ObjectMapper objectMapper) {
        this.iOrderService = iOrderService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitConfig.RESULT_A_QUEUE, ackMode = "MANUAL")
    public void handleTypeAResult(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            // Deserialize message
            Result result = objectMapper.readValue(message, Result.class);

            FulfilmentResponse response = new FulfilmentResponse(
                    result.getOrderId(),
                    result.getCorrelationId(),
                    result.getCustomerId(),
                    result.getFulfillmentType(),
                    "PASS".equals(result.getStatus())
            );

            iOrderService.completeOrder(response);
            channel.basicAck(tag, false);

        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false);
            } catch (IOException ioException) {
                log.error("Failed to nack message", ioException);
            }
            log.error("Failed to process message: " + message, e);
        }
    }

    @RabbitListener(queues = RabbitConfig.RESULT_B_QUEUE, ackMode = "MANUAL")
    public void handleTypeBResult(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            // Deserialize message
            Result result = objectMapper.readValue(message, Result.class);

            // Build response
            FulfilmentResponse response = new FulfilmentResponse(
                    result.getOrderId(),
                    result.getCorrelationId(),
                    result.getCustomerId(),
                    result.getFulfillmentType(),
                    result.isSuccessful()
            );

            String requestId = result.getRequestID();

            // Initialize results map for this requestId if not present
            store.putIfAbsent(requestId, new ConcurrentHashMap<>());
            Map<String, Result> results = store.get(requestId);

            // Store current result
            results.put(result.getFulfillmentCheck(), result);

            // Check if all results for this request are in
            if (results.size() == 4) {
                boolean allPassed = results.values().stream()
                        .allMatch(r -> "PASS".equals(r.getStatus()));

                response.setSuccessful(allPassed);
                iOrderService.completeOrder(response);

                log.info("FulfilmentType B {}",
                        allPassed ? "passes" : "at least one failure");

                // Cleanup
                store.remove(requestId);
            }

            // Acknowledge message after successful processing
            channel.basicAck(tag, false);

        } catch (Exception e) {
            // NACK the message, optionally requeue for retry
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                channel.basicNack(tag, false, false);
                log.error("Failed to nack message", ioException);
            }
            log.error("Failed to process message", e);
        }
    }


    @RabbitListener(queues = RabbitConfig.RESULT_C_QUEUE, ackMode = "MANUAL")
    public void handleTypeCResult(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            Result result = null;
            FulfilmentResponse response = null;
            try {
                result = objectMapper.readValue(message, Result.class);
                response = new FulfilmentResponse(result.getOrderId(), result.getCorrelationId(), result.getCustomerId(), result.getFulfillmentType(), result.isSuccessful());

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String requestId = result.getRequestID();

            store.putIfAbsent(requestId, new ConcurrentHashMap<>());
            Map<String, Result> results = store.get(requestId);

            results.put(result.getFulfillmentCheck(), result);

            if (results.size() == 6) {

                boolean allPassed = results.values().stream()
                        .allMatch(r -> "PASS".equals(r.getStatus()));

                if (allPassed) {
                    log.info("FulfilmentType C passes");
                    response.setSuccessful(true);
                    iOrderService.completeOrder(response);
                } else {
                    log.info("FulfilmentType C at least one failure");
                    response.setSuccessful(false);
                    iOrderService.completeOrder(response);
                }
                store.remove(requestId);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}