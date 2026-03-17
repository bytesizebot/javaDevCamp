package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.response.LogInResponse;
import za.co.entelect.java_devcamp.service.IProfileService;

@RestController
@RequestMapping("profiles")
@Tag(name = "Profile", description = "User profile management API")
public class ProfileController {

    private final IProfileService iProfileService;

    public ProfileController(IProfileService iProfileService) {
        this.iProfileService = iProfileService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileDto> createUserProfile(@RequestBody ProfileDto profileDto){
            iProfileService.createUserProfile(profileDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(profileDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable String username){
        try {
            ProfileDto profile = iProfileService.getProfileByUserName(username);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(profile);
        } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }
    }
}
