package za.co.entelect.devcamp.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.devcamp.productservice.model.ProductsDto;
import za.co.entelect.devcamp.productservice.service.ProductsService;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductsDto> getProductByName(@PathVariable String productName){
        return productsService.getProductByName(productName);
    }
}
