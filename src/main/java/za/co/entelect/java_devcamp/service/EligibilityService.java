package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.entity.Eligibility;
import za.co.entelect.java_devcamp.repository.EligibilityRepository;

@Service
public class EligibilityService implements IEligibilityService{
    private final EligibilityRepository eligibilityRepository;

    public EligibilityService(EligibilityRepository eligibilityRepository) {
        this.eligibilityRepository = eligibilityRepository;
    }

    @Override
    public Eligibility getEligibilityByProductIdAndCustomerId(Long customerId, Long productId) {
        return eligibilityRepository.findByCustomerIdAndProductId(customerId,productId);
    }
}
