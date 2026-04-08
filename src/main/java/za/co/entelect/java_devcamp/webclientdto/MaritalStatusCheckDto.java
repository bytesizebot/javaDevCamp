package za.co.entelect.java_devcamp.webclientdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaritalStatusCheckDto {
    private MaritalStatusDto currentStatus;
    private MaritalStatusDto previousStatus;
}
