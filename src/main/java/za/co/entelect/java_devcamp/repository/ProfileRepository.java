package za.co.entelect.java_devcamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.entelect.java_devcamp.entity.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByEmailAddress(String emailAddress);
    boolean existsByEmailAddress(String emailAddress);

    Optional<Profile> findByIdNumber(String idNumber);
}
