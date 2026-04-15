//package za.co.entelect.java_devcamp.rabbitmq;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rabbitmq.client.Channel;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.openapitools.model.DuplicateIDDocumentCheck;
//import org.openapitools.model.LivingStatus;
//import org.openapitools.model.MaritalStatusResponse;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClientRequestException;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import za.co.entelect.java_devcamp.configs.RabbitConfig;
//import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
//import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;
//import za.co.entelect.java_devcamp.request.FulfilmentRequest;
//import za.co.entelect.java_devcamp.response.FulfilmentResponse;
//import za.co.entelect.java_devcamp.serviceinterface.IFulfilmentService;
//import za.co.entelect.java_devcamp.soap.CreditClient;
//import za.co.entelect.java_devcamp.soap.FraudClient;
//import za.co.entelect.java_devcamp.util.MaskingUtils;
//import za.co.entelect.java_devcamp.webclient.DHAWebService;
//import za.co.entelect.java_devcamp.webclient.KYCWebService;
//import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Objects;
//
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class MessageConsumer {
//
//    private final KYCWebService kycWebService;
//    private final DHAWebService dhaWebService;
//    private final IFulfilmentService iFulfilmentService;
//    private final ObjectMapper objectMapper;
//    private final CreditClient creditClient;
//    private final FraudClient fraudClient;
//
//    @RabbitListener(queues = RabbitConfig.QUEUE_NAME, ackMode = "MANUAL")
//    public void listen(Message message, Channel channel) throws Exception {
//        try {
//            String body = new String(message.getBody(), StandardCharsets.UTF_8);
//            System.out.println("Received message: " + body);
//
//            // Optional delay for debugging
//            Thread.sleep(3000); // 3 seconds
//
//            // Acknowledge message
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            // Requeue if something goes wrong
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }
//    }
//
//    @RabbitListener(queues = RabbitConfig.KYC_QUEUE, ackMode = "MANUAL")
//    public void listenToKYCQueue(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//            log.info("Processing KYC check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//            KYCCheckDto kycResponse = null;
//            int maxRetries = 3;
//            int attempt = 0;
//
//            while (attempt < maxRetries) {
//                try {
//                    kycResponse = kycWebService.getCustomerKYC(request.getCustomerId());
//                    break;
//
//                } catch (WebClientResponseException ex) {
//                    if (ex.getStatusCode().is5xxServerError()) {
//                        attempt++;
//                        log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                        Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                    } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                        log.info("KYC not found for customerId={}", request.getCustomerId());
//                        break;
//
//                    } else {
//                        log.error("Client error calling KYC service: {}", ex.getResponseBodyAsString());
//                        throw ex;
//                    }
//
//                } catch (WebClientRequestException ex) {
//                    attempt++;
//                    log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                    Thread.sleep(1000 * attempt);
//
//                } catch (Exception ex) {
//                    log.error("Unexpected error calling KYC service", ex);
//                    throw ex;
//                }
//            }
//
//            channel.basicAck(tag, false);
//            FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//                iFulfilmentService.processKYCCheck(kycResponse, fulfilmentResponse);
//
//        } catch (Exception e) {
//            log.error("Failed to process message after retries. This service is currently unavailable", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//
//    @RabbitListener(queues = RabbitConfig.DHA_QUEUE, ackMode = "MANUAL")
//    public void listenToDHAQueue(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        doLivingStatusCheck(message, channel, tag);
//        doDuplicateIDCheck(message, channel, tag);
//        doMaritalStatusCheck(message, channel, tag);
//    }
//
//    @RabbitListener(queues = RabbitConfig.CC_QUEUE, ackMode = "MANUAL")
//    public void listenToCCQueue(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        doCreditCheck(message, channel, tag);
//    }
//
//    @RabbitListener(queues = RabbitConfig.FRAUD_QUEUE, ackMode = "MANUAL")
//    public void listenToFraudQueue(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        doFraudCheck(message, channel, tag);
//    }
//
//    public void doLivingStatusCheck(String message, Channel channel, long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//            log.info("Processing DHA Living status check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//            LivingStatus livingStatusResponse = null;
//            int maxRetries = 3;
//            int attempt = 0;
//
//            while (attempt < maxRetries) {
//                try {
//                    livingStatusResponse = dhaWebService.getLivingStatus(request.getCustomerIdNumber());
//                    break;
//
//                } catch (WebClientResponseException ex) {
//                    if (ex.getStatusCode().is5xxServerError()) {
//                        attempt++;
//                        log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                        Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                    } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                        log.info("Living status information not found for customer with idNumber={}", MaskingUtils.maskId(request.getCustomerIdNumber()));
//                        break;
//
//                    } else {
//                        log.error("Client error calling DHA service: {}", ex.getResponseBodyAsString());
//                        throw ex;
//                    }
//
//                } catch (WebClientRequestException ex) {
//                    attempt++;
//                    log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                    Thread.sleep(1000 * attempt);
//
//                } catch (Exception ex) {
//                    log.error("Unexpected error calling DHA service", ex);
//                    throw ex;
//                }
//            }
//
//            channel.basicAck(tag, false);
//            FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//            iFulfilmentService.processLivingStatusCheck(livingStatusResponse, fulfilmentResponse);
//
//        } catch (Exception e) {
//            log.error("Failed to process living status message after retries", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//
//    public void doDuplicateIDCheck(String message, Channel channel, long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//            log.info("Processing DHA Duplicate ID check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//            DuplicateIDDocumentCheck duplicateIDResponse = null;
//            int maxRetries = 3;
//            int attempt = 0;
//
//            while (attempt < maxRetries) {
//                try {
//                    duplicateIDResponse = dhaWebService.getDuplicateId(request.getCustomerIdNumber());
//                    break;
//
//                } catch (WebClientResponseException ex) {
//                    if (ex.getStatusCode().is5xxServerError()) {
//                        attempt++;
//                        log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                        Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                    } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                        log.info("Living status information not found for customer with idNumber={}", MaskingUtils.maskId(request.getCustomerIdNumber()));
//                        duplicateIDResponse = null;
//                        break;
//
//                    } else {
//                        log.error("Client error calling DHA service: {}", ex.getResponseBodyAsString());
//                        throw ex;
//                    }
//
//                } catch (WebClientRequestException ex) {
//                    attempt++;
//                    log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                    Thread.sleep(1000 * attempt);
//
//                } catch (Exception ex) {
//                    log.error("Unexpected error calling DHA service", ex);
//                    throw ex;
//                }
//            }
//
//            channel.basicAck(tag, false);
//            FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//            iFulfilmentService.processDuplicateIdCheck(duplicateIDResponse, fulfilmentResponse);
//
//        } catch (Exception e) {
//            log.error("Failed to process message for duplicate Ids after retries", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//
//    public void doMaritalStatusCheck(String message, Channel channel, long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//            if (Objects.equals(request.getFulfillmentType(), "C")) {
//                log.info("Processing DHA Marital Status check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//                MaritalStatusResponse maritalStatusResponse = null;
//                int maxRetries = 3;
//                int attempt = 0;
//
//                while (attempt < maxRetries) {
//                    try {
//                        maritalStatusResponse = dhaWebService.getMaritalStatus(request.getCustomerIdNumber());
//                        break;
//
//                    } catch (WebClientResponseException ex) {
//                        if (ex.getStatusCode().is5xxServerError()) {
//                            attempt++;
//                            log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                            Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                        } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                            log.info("Marital status information not found for customer with idNumber={}", MaskingUtils.maskId(request.getCustomerIdNumber()));
//                            maritalStatusResponse = null;
//                            break;
//
//                        } else {
//                            log.error("Client error calling DHA service: {}", ex.getResponseBodyAsString());
//                            throw ex;
//                        }
//
//                    } catch (WebClientRequestException ex) {
//                        attempt++;
//                        log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                        Thread.sleep(1000 * attempt);
//
//                    } catch (Exception ex) {
//                        log.error("Unexpected error calling DHA service", ex);
//                        throw ex;
//                    }
//                }
//
//                channel.basicAck(tag, false);
//                FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//                iFulfilmentService.processMaritalStatusCheck(maritalStatusResponse, fulfilmentResponse);
//            }
//        } catch (Exception e) {
//            log.error("Failed to process message after retries", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//
//    public void doCreditCheck(String message, Channel channel, long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//            if (Objects.equals(request.getFulfillmentType(), "C")) {
//                log.info("Processing Credit check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//                CreditCheckResponse creditCheckResponse = null;
//                int maxRetries = 3;
//                int attempt = 0;
//
//                while (attempt < maxRetries) {
//                    try {
//                        creditCheckResponse = creditClient.getCreditCheck(request.getCustomerId().intValue());
//                        break;
//
//                    } catch (WebClientResponseException ex) {
//                        if (ex.getStatusCode().is5xxServerError()) {
//                            attempt++;
//                            log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                            Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                        } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                            log.info("Credit information not found for customer with idNumber={}", MaskingUtils.maskId(request.getCustomerIdNumber()));
//                            break;
//
//                        } else {
//                            log.error("Client error calling DHA service: {}", ex.getResponseBodyAsString());
//                            throw ex;
//                        }
//
//                    } catch (WebClientRequestException ex) {
//                        attempt++;
//                        log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                        Thread.sleep(1000 * attempt);
//
//                    } catch (Exception ex) {
//                        log.error("Unexpected error calling DHA service", ex);
//                        throw ex;
//                    }
//                }
//
//                channel.basicAck(tag, false);
//                FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//                iFulfilmentService.processCreditCheck(creditCheckResponse, fulfilmentResponse);
//            }
//        } catch (Exception e) {
//            log.error("Failed to process message after retries", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//
//    public void doFraudCheck(String message, Channel channel, long tag) {
//        try {
//            FulfilmentRequest request = objectMapper.readValue(message, FulfilmentRequest.class);
//
//                log.info("Processing Fraud check for customer orderId: {}", MaskingUtils.maskId(request.getCorrelationId()));
//
//                FraudCheckResponse fraudCheckResponse = null;
//                int maxRetries = 3;
//                int attempt = 0;
//
//                while (attempt < maxRetries) {
//                    try {
//                        fraudCheckResponse = fraudClient.getFraudCheck(request.getCustomerId().intValue(),request.getCustomerIdNumber());
//                        break;
//
//                    } catch (WebClientResponseException ex) {
//                        if (ex.getStatusCode().is5xxServerError()) {
//                            attempt++;
//                            log.warn("Server error (attempt {}/{}): {}", attempt, maxRetries, ex.getResponseBodyAsString());
//                            Thread.sleep((long) Math.pow(2, attempt) * 1000);
//
//                        } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                            log.info("Fraud information not found for customer with idNumber={}", MaskingUtils.maskId(request.getCustomerIdNumber()));
//
//                            break;
//
//                        } else {
//                            log.error("Client error calling Fraud service: {}", ex.getResponseBodyAsString());
//                            throw ex;
//                        }
//
//                    } catch (WebClientRequestException ex) {
//                        attempt++;
//                        log.warn("Network error (attempt {}/{}): {}", attempt, maxRetries, ex.getMessage());
//                        Thread.sleep(1000 * attempt);
//
//                    } catch (Exception ex) {
//                        log.error("Unexpected error calling Fraud service", ex);
//                        throw ex;
//                    }
//                }
//
//                channel.basicAck(tag, false);
//                FulfilmentResponse fulfilmentResponse = new FulfilmentResponse(request.getOrderId(), request.getCorrelationId(), request.getCustomerId(), request.getFulfillmentType(), false);
//                iFulfilmentService.processFraudCheck(fraudCheckResponse, fulfilmentResponse);
//
//        } catch (Exception e) {
//            log.error("Failed to process message after retries", e);
//            try {
//                channel.basicNack(tag, false, false);
//            } catch (IOException ioEx) {
//                log.error("Failed to nack message", ioEx);
//            }
//        }
//    }
//}
