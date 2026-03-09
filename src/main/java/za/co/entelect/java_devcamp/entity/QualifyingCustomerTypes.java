package za.co.entelect.java_devcamp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Product", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class QualifyingCustomerTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long qualifyingCustomerTypesId;

    @Column
    private Long customerTypesId;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;
}
