package za.co.entelect.java_devcamp.customerdto;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class CustomerDto {

    private Integer Id;
    private String username;
    private String firstName;
    private String lastName;
    private String idNumber;
    private Integer customerTypeId;
    private CustomerTypesDto customerType;
    private List<AccountsDto> customerAccounts;

}
