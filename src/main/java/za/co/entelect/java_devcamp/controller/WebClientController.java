package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.service.IWebService;
import za.co.entelect.java_devcamp.webclientdto.KYCCheckDto;

@RestController
@RequestMapping("web-client")
@Tag(name = "Web Client", description = "endpoint to communicate with CIS")
public class WebClientController {

    private final IWebService IWebService;

    public WebClientController(IWebService IWebService) {
        this.IWebService = IWebService;
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

}
