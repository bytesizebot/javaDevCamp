package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.webclientdto.CustomerDto;

import java.util.List;

public interface IDocumentService {
    void generateCustomerContract(List<ProductDto> orderItemDtoList, CustomerDto customerDto);
}
