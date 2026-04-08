package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.api.DhaApi;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.serviceinterface.IWebService;
import za.co.entelect.java_devcamp.soap.CreditClient;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.wsdl.CreditCheckResponse;

@RestController
@RequestMapping("web-client")
@Tag(name = "Web Client", description = "endpoint to communicate with CIS")
public class WebClientController implements DhaApi  {

    private final IWebService IWebService;
    private final CreditClient creditClient;

    @Value("${remote.api.url}")
    private String baseUrl;


    public WebClientController(IWebService IWebService, CreditClient creditClient) {
        this.IWebService = IWebService;
        this.creditClient = creditClient;
    }

    @PostMapping("/cis/register-profile")
    public ResponseEntity<ProfileDto> registerUserProfile(@RequestBody ProfileDto profileDto){
        IWebService.createCISCustomer(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileDto);
    }

    @GetMapping("/kyc/{customerId}")
    public ResponseEntity<KYCCheckDto> getKYCCheck(@PathVariable Long customerId){
        KYCCheckDto kycCheckDto = IWebService.getCustomerKYC(customerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(kycCheckDto);
    }

    @Override
    public ResponseEntity<MaritalStatusResponse> getMaritalStatusById(String idNumber){
        MaritalStatusResponse maritalStatus = IWebService.getCustomerMaritalStatus(idNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(maritalStatus);
    }


    @Override
    public ResponseEntity<LivingStatus> getLivingStatusById(String idNumber){
        LivingStatus livingStatus = IWebService.getCustomerLivingStatus(idNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(livingStatus);
    }

    @Override
    public ResponseEntity<DuplicateIDDocumentCheck> getDuplicateIdDocumentStatusById(String idNumber){
        DuplicateIDDocumentCheck duplicateIDDocumentCheck = IWebService.getCustomerDuplicateIDStatus(idNumber);
        return ResponseEntity.ok(duplicateIDDocumentCheck);
    }

    @GetMapping("creditCheck/{customerId}")
    @SecurityRequirements()
    public ResponseEntity<CreditCheckResponse> getCreditStatus(@PathVariable Integer customerId){
        CreditCheckResponse creditCheckResponse = creditClient.getCreditCheck(customerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(creditCheckResponse);
    }


}
