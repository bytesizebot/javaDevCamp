package za.co.entelect.java_devcamp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EligibilityResponse {
    private String message;
    private String failureReason;
}
