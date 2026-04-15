package za.co.entelect.java_devcamp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import za.co.entelect.java_devcamp.configs.TokenStore;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.exception.DuplicateResourceException;
import za.co.entelect.java_devcamp.exception.TransientWebClientException;
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

        if (cisWebService.getCustomerByEmail(profileDto.username()) != null) {
            throw new DuplicateResourceException("User already exists. You cannot register them");
        }
        cisWebService.registerCustomer(profileDto);
    }


    @Override
    public void addCustomerAccount(CustomerAccountDto accountDto) throws IllegalAccessException {
        if (tokenStore.getAuthority().equals("ADMIN")) {
            cisWebService.addAccountToCustomer(accountDto);
        } else {
            throw new IllegalAccessException("User does not have permission to access this resource");
        }
    }

    @Retryable(value = {ResourceAccessException.class, TransientWebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Override
    public KYCCheckDto getCustomerKYC(Long customerId) {
        try {
        return kycWebService.getCustomerKYC(customerId);
        } catch (WebClientResponseException ex) {

            if (ex.getStatusCode().is5xxServerError()) {
                throw new TransientWebClientException(ex);
            }
            throw ex;
        }
    }

    @Retryable(value = {ResourceAccessException.class, TransientWebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Override
    public MaritalStatusResponse getCustomerMaritalStatus(String customerIdNumber) {
        try {
            return dhaWebService.getMaritalStatus(customerIdNumber);
        } catch (WebClientResponseException ex) {

            if (ex.getStatusCode().is5xxServerError()) {
                throw new TransientWebClientException(ex);
            }
            throw ex;
        }
    }

    @Retryable(value = {ResourceAccessException.class, TransientWebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Override
    public LivingStatus getCustomerLivingStatus(String customerIdNumber) {
        try {
            LivingStatus rep =  dhaWebService.getLivingStatus(customerIdNumber);
            return rep;
        } catch (WebClientResponseException ex) {

            if (ex.getStatusCode().is5xxServerError()) {
                throw new TransientWebClientException(ex);
            }
            throw ex; 
        }
    }

    @Retryable(value = {ResourceAccessException.class, TransientWebClientException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Override
    public DuplicateIDDocumentCheck getCustomerDuplicateIDStatus(String customerIdNumber) {
        try {
            return dhaWebService.getDuplicateId(customerIdNumber);
        } catch (WebClientResponseException ex) {

            if (ex.getStatusCode().is5xxServerError()) {
                throw new TransientWebClientException(ex);
            }
            throw ex;
        }
    }

}
