package za.co.entelect.java_devcamp.request;
import lombok.*;

@Getter
@Setter
public class OrderRequest {
    String customerEmail;
    Long productId;
}
