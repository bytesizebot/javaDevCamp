package za.co.entelect.java_devcamp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FulfilmentResponse {
    private Long orderId;
    private String correlationId;
    private Long customerId;
    private boolean successful;
}
