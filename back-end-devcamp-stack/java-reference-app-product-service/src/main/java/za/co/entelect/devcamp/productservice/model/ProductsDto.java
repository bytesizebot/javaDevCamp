package za.co.entelect.devcamp.productservice.model;

public class ProductsDto {
    private Integer Id;
    private String Name;
    private String Description;
    private String EligibilityCriteria;
    private Float Pricing;
    private String Benefits;

    public ProductsDto(){}

    public ProductsDto(String name,String description, String eligibilityCriteria, String benefits, Float pricing){
        this.Name = name;
        this.Description = description;
        this.Pricing = pricing;
        this.Benefits = benefits;
        this.EligibilityCriteria = eligibilityCriteria;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEligibilityCriteria() {
        return EligibilityCriteria;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        EligibilityCriteria = eligibilityCriteria;
    }

    public Float getPricing() {
        return Pricing;
    }

    public void setPricing(Float pricing) {
        Pricing = pricing;
    }

    public String getBenefits() {
        return Benefits;
    }

    public void setBenefits(String benefits) {
        Benefits = benefits;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
