package za.co.entelect.java_devcamp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FulfilmentType", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class FulfilmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fulfilmentTypeId", nullable = false)
    private Long FulfilmentTypeId;

    @Column
    private String Name;

    @Column
    private String Description;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "ProductId")
    private Product product;


}
