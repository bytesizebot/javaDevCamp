package za.co.entelect.java_devcamp.webclientdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaritalStatusDto {
    private MaritalStatus status;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
