package za.co.entelect.java_devcamp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.entelect.java_devcamp.dto.UserDto;
import za.co.entelect.java_devcamp.exception.IncorrectPasswordException;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.request.LogInRequest;
import za.co.entelect.java_devcamp.response.LogInResponse;
import za.co.entelect.java_devcamp.service.IUserService;

@RestController
@RequestMapping("auth")
@SecurityRequirements()
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final IUserService iuserService;

    public AuthController(IUserService iuserService) {
        this.iuserService = iuserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        try {
            iuserService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User :" + userDto.username() + " has been registered successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/log-in")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LogInRequest userDto) {
        try {
            LogInResponse response = iuserService.logIn(userDto);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LogInResponse(e.getMessage()));
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LogInResponse(e.getMessage()));
        }
    }
}
