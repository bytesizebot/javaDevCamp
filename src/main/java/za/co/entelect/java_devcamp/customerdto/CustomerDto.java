package za.co.entelect.java_devcamp.customerdto;

import java.util.List;

public class CustomerDto {

    private Integer Id;
    private String username;
    private String firstName;
    private String lastName;
    private String idNumber;
    private Integer customerTypeId;

    private CustomerTypesDto customerType;
    private List<AccountTypeDto> customerAccounts;

    public CustomerDto(){}

    public CustomerDto(String idnumber, String email, String firstname, String lastName){
        this.idNumber = idnumber;
        this.firstName = firstname;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdNumber() {
        return idNumber;
    }


    public Integer getId() {
        return Id;
    }

    public void setcustomerTypeId(Integer id) {
        Id = id;
    }

    public Integer getcustomerTypeId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public CustomerTypesDto getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerTypesDto customerType) {
        this.customerType = customerType;
    }

    public List<AccountTypeDto> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(List<AccountTypeDto> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    public void customerTypeId(Integer customerTypeId) {
    }

    public Integer getCustomerTypeId() {
        return customerTypeId;
    }
}
