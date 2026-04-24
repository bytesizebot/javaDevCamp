package za.co.entelect.java_devcamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.java_devcamp.entity.ContractDocument;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<ContractDocument, UUID> {
    Optional<ContractDocument> findByFileName(String fileName);
}
