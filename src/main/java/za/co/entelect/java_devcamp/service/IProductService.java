package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.ProductDto;

import java.util.List;

public interface IProductService {
 List<ProductDto> getProducts();
 ProductDto getProductById(Long id);
 boolean canTakeUpProduct(Long userId, Long productId);

}
