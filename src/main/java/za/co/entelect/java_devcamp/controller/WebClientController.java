package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.api.DhaApi;
import org.openapitools.model.DuplicateIDDocumentCheck;
import org.openapitools.model.LivingStatus;
import org.openapitools.model.MaritalStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.creditcheck.CreditCheckResponse;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.fraudcheck.FraudCheckResponse;
import za.co.entelect.java_devcamp.serviceinterface.IWebService;
import za.co.entelect.java_devcamp.soap.CreditClient;
import za.co.entelect.java_devcamp.soap.FraudClient;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

@RestController
@RequestMapping("web-client")
@Tag(name = "Web Client", description = "endpoint to communicate with CIS")
public class WebClientController implements DhaApi  {

    private final IWebService IWebService;
    private final CreditClient creditClient;
    private final FraudClient fraudClient;

    public WebClientController(za.co.entelect.java_devcamp.serviceinterface.IWebService iWebService, CreditClient creditClient, FraudClient fraudClient) {
        IWebService = iWebService;
        this.creditClient = creditClient;
        this.fraudClient = fraudClient;
    }
//    private final SoapClient soapClient;
//
//    @Value("${remote.api.url}")
//    private String baseUrl;
//
//
//    public WebClientController(IWebService IWebService, SoapClient soapClient) {
//        this.IWebService = IWebService;
//        this.soapClient = soapClient;
//    }

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

    @GetMapping("fraudCheck/{customerId}/{IdNumber}")
    @SecurityRequirements()
    public ResponseEntity<FraudCheckResponse> getFraudStatus(@PathVariable Integer customerId,@PathVariable String IdNumber){
        FraudCheckResponse fraudCheckResponse = fraudClient.getFraudCheck(customerId, IdNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(fraudCheckResponse);
    }




}
