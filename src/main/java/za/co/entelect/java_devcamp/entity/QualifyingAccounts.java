package za.co.entelect.java_devcamp.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Qualifying_Accounts", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class QualifyingAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long QualifyingAccountsId;

    @Column private Long accountId;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

}
