package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Product;

import java.util.List;

public interface IProductService {
 List<ProductDto> getProducts();
 Product getProductById(Long id);
 boolean isEligibleForProduct(String customerEmail, Long productId);

}
