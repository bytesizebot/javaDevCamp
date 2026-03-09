package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.service.IProductService;

import java.util.List;

@RestController
@RequestMapping("product")
@Tag(name = "Product", description = "Product management API")
public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDto = iProductService.getProducts();
        return ResponseEntity.ok(productDto);
    }
}
