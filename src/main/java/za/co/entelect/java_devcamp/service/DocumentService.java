package za.co.entelect.java_devcamp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.serviceinterface.IDocumentService;
import za.co.entelect.java_devcamp.webclientdto.CustomerDto;

import java.util.List;

@AllArgsConstructor
@Service
public class DocumentService implements IDocumentService {

    @Override
    public void generateCustomerContract(List<ProductDto> orderItemDtoList, CustomerDto customerDto) {

        for(ProductDto order: orderItemDtoList){
            //generate document
            //generate url
            //send notification to customer
        }
    }
}
