package za.co.entelect.java_devcamp.customerdto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Embeddable
public class CustomerAccountsDto {
    @Column(name = "account_type_id")
    private Long accountTypeId;

    @Column(name = "customer_id")
    private Long customerId;
}


