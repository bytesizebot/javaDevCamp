package za.co.entelect.devcamp.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productDescription;

    @Column(nullable = false)
    private Float pricing;

    @Column(nullable = false)
    private String eligibilityCriteria;

    @Column(nullable = false)
    private String benefits;

    public Products(ProductsDto productsDto){
        this.productDescription = productsDto.getDescription();
        this.eligibilityCriteria = productsDto.getEligibilityCriteria();
        this.pricing = productsDto.getPricing();
        this.benefits = productsDto.getBenefits();
    }

    public Products(String name, String description, Float pricing, String benefits, String eligibilityCriteria){
        this.productName = name;
        this.productDescription = description;
        this.pricing = pricing;
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public ProductsDto toProductsDto(){
        ProductsDto productsDto = new ProductsDto(this.productName, this.productDescription, this.benefits, this.eligibilityCriteria, this.pricing);
        productsDto.setId(this.productId.intValue());
        return productsDto;
    }

}
