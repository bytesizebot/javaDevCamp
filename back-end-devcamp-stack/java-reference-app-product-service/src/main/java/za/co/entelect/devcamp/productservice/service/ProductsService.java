package za.co.entelect.devcamp.productservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.entelect.devcamp.productservice.model.Products;
import za.co.entelect.devcamp.productservice.model.ProductsDto;
import za.co.entelect.devcamp.productservice.repository.ProductsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProductsService implements IProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public ResponseEntity<ProductsDto> getProductByName(String name){
        Optional<Products> productByName = productsRepository.getProductByName(name);
        return productByName.map(products -> ResponseEntity.ok(products.toProductsDto())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
