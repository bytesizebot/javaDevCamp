package za.co.entelect.java_devcamp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.TokenStore;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.exception.DuplicateResourceException;
import za.co.entelect.java_devcamp.serviceinterface.IWebService;
import za.co.entelect.java_devcamp.webclient.CISWebService;
import za.co.entelect.java_devcamp.webclient.DHAWebService;
import za.co.entelect.java_devcamp.webclient.KYCWebService;
import za.co.entelect.java_devcamp.webclientdto.CustomerAccountDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

@Slf4j
@AllArgsConstructor
@Service
public class WebService implements IWebService {

    private final CISWebService cisWebService;
    private final KYCWebService kycWebService;
    private final DHAWebService dhaWebService;
    private final TokenStore tokenStore;


    @Override
    public void createCISCustomer(ProfileDto profileDto) {
        log.info("Creating a customer in the CIS");

        if(cisWebService.getCustomerByEmail(profileDto.username()) != null){
            throw new DuplicateResourceException("User already exists. You cannot register them");
        }
        cisWebService.registerCustomer(profileDto);
    }


    @Override
    public void addCustomerAccount(CustomerAccountDto accountDto) throws IllegalAccessException {
        if(tokenStore.getAuthority().equals("ADMIN")){
            cisWebService.addAccountToCustomer(accountDto);
        }else{
            throw new IllegalAccessException("User does not have permission to access this resource");
        }
    }

    @Override
    public KYCCheckDto getCustomerKYC(Long customerId) {

        return kycWebService.getCustomerKYC(customerId);
    }

    @Override
    public MaritalStatusResponse getCustomerMaritalStatus(String customerIdNumber) {
        return dhaWebService.getMaritalStatus(customerIdNumber);
    }

    @Override
    public LivingStatus getCustomerLivingStatus(String customerIdNumber) {
        return dhaWebService.getLivingStatus(customerIdNumber);
    }

    @Override
    public DuplicateIDDocumentCheck getCustomerDuplicateIDStatus(String customerIdNumber) {
        return dhaWebService.getDuplicateId(customerIdNumber);
    }

}
