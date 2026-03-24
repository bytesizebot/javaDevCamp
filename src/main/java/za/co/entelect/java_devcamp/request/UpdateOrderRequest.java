package za.co.entelect.java_devcamp.request;
import lombok.*;
import za.co.entelect.java_devcamp.entity.Status;

@Getter
@Setter
public class UpdateOrderRequest {
    private Status newStatus;
    private Long orderId;
}
