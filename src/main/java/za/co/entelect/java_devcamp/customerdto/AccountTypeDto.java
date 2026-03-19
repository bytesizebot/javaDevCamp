package za.co.entelect.java_devcamp.customerdto;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeDto {

    private Integer id;
    private String name;
    private String description;

    public AccountTypeDto(String name, String description){
        this.name = name;
        this.description = description;
    }
}
