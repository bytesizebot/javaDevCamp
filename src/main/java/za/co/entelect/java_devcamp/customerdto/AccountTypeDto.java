package za.co.entelect.java_devcamp.customerdto;

import java.math.BigDecimal;

public class AccountTypeDto {

    private Integer id;
    private String name;
    private String description;

    public AccountTypeDto(){}

    public AccountTypeDto(String name, String description){
        this.name = name;
        this.description = description;
    }

    public AccountTypeDto(BigDecimal bigDecimal, String name, String description) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }
}
