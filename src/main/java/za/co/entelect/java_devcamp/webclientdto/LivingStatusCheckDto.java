package za.co.entelect.java_devcamp.webclientdto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LivingStatusCheckDto {
    private LivingStatus livingStatus;
    private Date deceasedDate;
}
