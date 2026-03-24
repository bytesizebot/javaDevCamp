package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.entity.Eligibility;

public interface IEligibilityService {
    Eligibility getEligibilityByProductIdAndCustomerId(Long customerId, Long productId);
}
