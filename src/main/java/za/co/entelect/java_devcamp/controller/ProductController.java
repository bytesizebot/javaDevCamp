package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProductDto;
import za.co.entelect.java_devcamp.request.EligibilityRequest;
import za.co.entelect.java_devcamp.response.EligibilityResponse;
import za.co.entelect.java_devcamp.service.IProductService;

import java.util.List;

@RestController
@RequestMapping("products")
@Tag(name = "Product", description = "Product management API")

public class ProductController {
    private final IProductService iProductService;

    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @Operation(summary="Get All Products")
    @SecurityRequirements()
    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> productDto = iProductService.getProducts();
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary="Get Product By Id")
    @SecurityRequirements()
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
       ProductDto productDto = iProductService.getProductById(id);
       return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Check if a customer is eligible for a product")
    @GetMapping("/check-eligibility")
    public EligibilityResponse checkProductEligibility(@RequestBody EligibilityRequest eligibilityRequest){
        boolean isEligible = iProductService.isEligibleForProduct(eligibilityRequest.getCustomerEmail(), eligibilityRequest.getProductId(), eligibilityRequest.getWebToken());
        return isEligible
                ? new EligibilityResponse(true, "User is eligible to take up this product")
                : new EligibilityResponse(false, "User is not eligible to take up this product");
    }
}
