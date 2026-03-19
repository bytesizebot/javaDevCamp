package za.co.entelect.java_devcamp.request;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class EligibilityRequest {
    private String customerEmail;
    private Long productId;
    private String webToken;
}
