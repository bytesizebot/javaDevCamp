package za.co.entelect.java_devcamp.customerdto;

public class CustomerTypesDto {

    private Integer Id;
    private String name;
    private String description;

    public CustomerTypesDto(){}

    public CustomerTypesDto(String name, String description){
        this.name = name;
        this.description = description;
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
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
