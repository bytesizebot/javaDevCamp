package za.co.entelect.java_devcamp.customerdto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomersDto {

    private List<CustomerDto> customers;

}
