package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.webclientdto.DuplicateIdCheckDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.webclientdto.LivingStatusCheckDto;
import za.co.entelect.java_devcamp.webclientdto.MaritalStatusCheckDto;

public interface IWebService {
    void createCISCustomer(ProfileDto profileDto);
    KYCCheckDto getCustomerKYC(Long customerId);
    MaritalStatusCheckDto getCustomerMaritalStatus(String customerIdNumber);
    LivingStatusCheckDto getCustomerLivingStatus(String customerIdNumber);
    DuplicateIdCheckDto getCustomerDuplicateIDStatus(String customerIdNumber);
}
