package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProfileMapper;
import za.co.entelect.java_devcamp.response.LogInResponse;
import za.co.entelect.java_devcamp.service.IProfileService;

@RestController
@RequestMapping("profiles")
@Tag(name = "Profile", description = "User profile management API")
public class ProfileController {

    private final IProfileService iProfileService;
    private final ProfileMapper profileMapper;

    public ProfileController(IProfileService iProfileService, ProfileMapper profileMapper) {
        this.iProfileService = iProfileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<ProfileDto> createUserProfile(@RequestBody ProfileDto profileDto) {
        iProfileService.createUserProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDto> getUserProfile(@PathVariable String username) {
        try {
            Profile profile = iProfileService.getProfileByUserName(username);
            ProfileDto profileDto = profileMapper.toProfileDto(profile);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(profileDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    //To do: Update customer profile implementation
}
