package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.mapper.ProductMapper;
import za.co.entelect.java_devcamp.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(productMapper::toDto).toList();
    }
}
