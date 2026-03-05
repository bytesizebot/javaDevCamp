package za.co.entelect.devcamp.productservice.service;

import org.springframework.http.ResponseEntity;
import za.co.entelect.devcamp.productservice.model.ProductsDto;

public interface IProductsService {
    ResponseEntity<ProductsDto> getProductByName(String name);
}
