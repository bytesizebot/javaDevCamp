package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

public interface IWebService {
    ProfileDto createCISCustomer(ProfileDto profileDto);

    KYCCheckDto getCustomerKYC(Long customerId);
}
