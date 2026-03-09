package za.co.entelect.java_devcamp.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long ProductId;

    @Getter
    @Column
    private String Name;

    @Column
    private String Description;

    @Column
    private Float Price;

    @Column
    private String ImageUrl;

    @OneToOne(cascade = CascadeType.ALL)
    private FulfilmentType fulfilmentType;

    @OneToMany(mappedBy = "QualifyingAccounts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QualifyingAccounts> qualifyingAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "QualifyingCustomerTypes", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QualifyingCustomerTypes> qualifyingCustomerTypes = new ArrayList<>();

    public Product(String name, String description, Float price, String ImageUrl) {
        this.Name = name;
        this.Description = description;
        this.Price = price;
        this.ImageUrl = ImageUrl;
    }
}
