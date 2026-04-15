package za.co.entelect.java_devcamp.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FulfilmentRequest implements Serializable {
    private Long customerId;
    private String customerIdNumber;
    private String fulfillmentType;
    private Long orderId;
    private String correlationId;
}
