package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.ProductDto;

import java.util.List;

public interface IProductService {
 List<ProductDto> getProducts();
 ProductDto getProductById(Long id);
 boolean isEligibleForProduct(String customerEmail, Long productId, String jwtToken);

}
