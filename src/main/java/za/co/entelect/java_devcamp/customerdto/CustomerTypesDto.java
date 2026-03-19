package za.co.entelect.java_devcamp.customerdto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CustomerTypesDto {

    private Integer Id;
    private String name;
    private String description;


    public CustomerTypesDto(String name, String description){
        this.name = name;
        this.description = description;
    }
}
