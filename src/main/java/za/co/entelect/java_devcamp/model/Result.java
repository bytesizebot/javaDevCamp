package za.co.entelect.java_devcamp.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String requestID;
    private String service;
    private String fulfillmentCheck;
    private String status;

    private Long orderId;
    private String correlationId;
    private Long customerId;
    private String fulfillmentType;
    private boolean successful;
}
