package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.mapper.ProductMapper;
import za.co.entelect.java_devcamp.response.EligibilityResponse;
import za.co.entelect.java_devcamp.serviceinterface.IProductService;

import java.util.List;

@RestController
@RequestMapping("products")
@Tag(name = "Product", description = "Product management API")
@SecurityRequirements()
public class ProductController {
    private final IProductService iProductService;
    private final ProductMapper productMapper;

    public ProductController(IProductService iProductService, ProductMapper productMapper) {
        this.iProductService = iProductService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Get All Products")
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDto = iProductService.getProducts();
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Get Product By Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productMapper.toDto(iProductService.getProductById(id));
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Check if a customer is eligible for a product")
    @GetMapping("/eligibility")
    @SecurityRequirement(name = "bearerAuth")
    public EligibilityResponse checkProductEligibility(@RequestParam String email, @RequestParam Long productId) {

        boolean isEligible = iProductService.isEligibleForProduct(email, productId);

        return isEligible
                ? new EligibilityResponse(true, "User is eligible to take up this product")
                : new EligibilityResponse(false, "User is not eligible to take up this product");
    }
}
