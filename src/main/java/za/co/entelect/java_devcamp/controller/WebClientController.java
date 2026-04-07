package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.api.DhaApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.serviceinterface.IWebService;
import za.co.entelect.java_devcamp.soap.CreditClient;
import za.co.entelect.java_devcamp.webclientdto.DuplicateIdCheckDto;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;
import za.co.entelect.java_devcamp.webclientdto.LivingStatusCheckDto;
import za.co.entelect.java_devcamp.webclientdto.MaritalStatusCheckDto;
import za.co.entelect.java_devcamp.wsdl.CreditCheckResponse;

@RestController
@RequestMapping("web-client")
@Tag(name = "Web Client", description = "endpoint to communicate with CIS")
public class WebClientController implements DhaApi {

    private final IWebService IWebService;
    private final CreditClient creditClient;

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

    @GetMapping("dha/marital/{customerIdNumber}")
    public ResponseEntity<MaritalStatusCheckDto> getDHAMaritalStatus(@PathVariable String customerIdNumber){
        MaritalStatusCheckDto maritalStatusCheck = IWebService.getCustomerMaritalStatus(customerIdNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(maritalStatusCheck);
    }

    @GetMapping("dha/living/{customerIdNumber}")
    public ResponseEntity<LivingStatusCheckDto> getDHALivingStatus(@PathVariable String customerIdNumber){
        LivingStatusCheckDto livingStatusCheckDto = IWebService.getCustomerLivingStatus(customerIdNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(livingStatusCheckDto);
    }

    @GetMapping("dha/duplicateId/{customerIdNumber}")
    public ResponseEntity<DuplicateIdCheckDto> getDHADuplicateIDStatus(@PathVariable String customerIdNumber){
        DuplicateIdCheckDto duplicateIdCheckDto = IWebService.getCustomerDuplicateIDStatus(customerIdNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(duplicateIdCheckDto);
    }

    @GetMapping("creditCheck/{customerId}")
    @SecurityRequirements()
    public ResponseEntity<CreditCheckResponse> getCreditStatus(@PathVariable Integer customerId){
        CreditCheckResponse creditCheckResponse = creditClient.getCreditCheck(customerId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(creditCheckResponse);
    }


}
