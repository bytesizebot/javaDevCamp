package za.co.entelect.java_devcamp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Eligibility", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Eligibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eligibility_id", nullable = false)
    private Long eligibilityId;
    private Long productId;
    private Long customerId;
    private Boolean result;

    public Eligibility(Long productId, Long customerId, Boolean result) {
        this.productId = productId;
        this.customerId = customerId;
        this.result = result;
    }

}
