package za.co.entelect.java_devcamp.webclientdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountDto {
    private Long customerId;
    private Long accountId;
}
