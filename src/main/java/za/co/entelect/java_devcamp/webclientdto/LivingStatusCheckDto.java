package za.co.entelect.java_devcamp.webclientdto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LivingStatusCheckDto {
    private LivingStatus livingStatus;
    private LocalDate deceasedDate;
}
