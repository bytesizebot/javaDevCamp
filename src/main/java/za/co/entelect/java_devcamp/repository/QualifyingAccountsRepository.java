package za.co.entelect.java_devcamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.java_devcamp.entity.QualifyingAccounts;

@Repository
public interface QualifyingAccountsRepository extends JpaRepository<QualifyingAccounts, Long> {
    QualifyingAccounts findByAccountIdAndProductProductId(Long accountId, Long productId);
}
