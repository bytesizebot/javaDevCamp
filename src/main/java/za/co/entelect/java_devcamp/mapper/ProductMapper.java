package za.co.entelect.java_devcamp.mapper;

import org.springframework.stereotype.Component;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Product;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public Product toEntity(ProductDto productDto){
        return new Product(
                productDto.Name(),
                productDto.Description(),
                productDto.Price(),
                productDto.imageUrl()
        );
    }
}
