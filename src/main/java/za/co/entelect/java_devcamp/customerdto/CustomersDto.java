package za.co.entelect.java_devcamp.customerdto;

import java.util.List;

public class CustomersDto {

    private List<CustomerDto> customers;

    public CustomersDto(){}

    public CustomersDto(List<CustomerDto> customer){
        this.customers = customer;
    }
    public CustomerDto [] customerDto;

    public List<CustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }
}
