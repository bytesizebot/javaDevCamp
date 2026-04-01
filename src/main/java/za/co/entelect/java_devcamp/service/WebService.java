package za.co.entelect.java_devcamp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.exception.DuplicateResourceException;
import za.co.entelect.java_devcamp.webclient.CISWebService;
import za.co.entelect.java_devcamp.webclient.KYCWebService;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

@Slf4j
@AllArgsConstructor
@Service
public class WebService implements IWebService {

    private final CISWebService cisWebService;
    private final KYCWebService kycWebService;


    @Override
    public ProfileDto createCISCustomer(ProfileDto profileDto) {
        log.info("Creating a customer in the CIS");

        if(cisWebService.getCustomerByEmail(profileDto.username()) != null){
            throw new DuplicateResourceException("User already exists. You cannot register them");
        }
        return cisWebService.registerCustomer(profileDto);
    }

    @Override
    public KYCCheckDto getCustomerKYC(Long customerId) {

        return kycWebService.getCustomerKYC(customerId);
    }
}
