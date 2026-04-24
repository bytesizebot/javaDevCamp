package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.response.EligibilityResponse;

import java.util.List;

public interface IProductService {
 List<ProductDto> getProducts();
 Product getProductById(Long id);
 EligibilityResponse isEligibleForProduct(String customerEmail, Long productId);

}
