package za.co.entelect.java_devcamp.serviceinterface;

import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.webclientdto.CustomerAccountDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

public interface IWebService {
    void createCISCustomer(ProfileDto profileDto);
    void addCustomerAccount(CustomerAccountDto accountDto) throws IllegalAccessException;
    KYCCheckDto getCustomerKYC(Long customerId);
    MaritalStatusResponse getCustomerMaritalStatus(String customerIdNumber);
    LivingStatus getCustomerLivingStatus(String customerIdNumber);
    DuplicateIDDocumentCheck getCustomerDuplicateIDStatus(String customerIdNumber);
}
