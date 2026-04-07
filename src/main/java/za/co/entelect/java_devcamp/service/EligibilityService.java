package za.co.entelect.java_devcamp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.entity.Eligibility;
import za.co.entelect.java_devcamp.repository.EligibilityRepository;
import za.co.entelect.java_devcamp.serviceinterface.IEligibilityService;

@RequiredArgsConstructor
@Service
public class EligibilityService implements IEligibilityService {

    private final EligibilityRepository eligibilityRepository;

    @Override
    public Eligibility getEligibilityByProductIdAndCustomerId(Long customerId, Long productId) {
        return eligibilityRepository.findByCustomerIdAndProductId(customerId,productId);
    }
}
