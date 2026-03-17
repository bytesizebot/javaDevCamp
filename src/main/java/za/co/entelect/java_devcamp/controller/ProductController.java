package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.service.IProductService;

import java.util.List;

@RestController
@RequestMapping("products")
@Tag(name = "Product", description = "Product management API")
@SecurityRequirements()
public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @Operation(summary="Get All Products")
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDto = iProductService.getProducts();
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary="Get Product By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
       ProductDto productDto = iProductService.getProductById(id);
       return ResponseEntity.ok(productDto);
    }
}
