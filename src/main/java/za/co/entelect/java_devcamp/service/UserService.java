package za.co.entelect.java_devcamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import za.co.entelect.java_devcamp.configs.TokenStore;
import za.co.entelect.java_devcamp.dto.UserDto;
import za.co.entelect.java_devcamp.entity.User;
import za.co.entelect.java_devcamp.exception.IncorrectPasswordException;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.UserMapper;
import za.co.entelect.java_devcamp.repository.UserRepository;
import za.co.entelect.java_devcamp.request.LogInRequest;
import za.co.entelect.java_devcamp.response.LogInResponse;
import za.co.entelect.java_devcamp.serviceinterface.IUserService;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestTemplate restTemplate;
    private final TokenStore tokenStore;
    private volatile String token;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${auth.service.client.username}")
    private String authServiceUsername;

    @Value("${auth.service.client.password}")
    private String authServicePassword;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDto userDto) {
        log.info("Registering a new user");

        if (userRepository.existsByUsername(userDto.username())) {
            throw new RuntimeException("User already exists.");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userMapper.toDto(savedUser);

    }

    private String fetchRsToken(String username) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/token"; // your unsecured endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("client-username", "client-password");
        Map<String, String> request = Map.of(
                "username", username
        );

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public String generateToken(String loginUserName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(authServiceUsername, authServicePassword);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(
                Map.of("username", loginUserName), headers);

        ResponseEntity<String> authResponse =
                restTemplate.postForEntity(authServiceUrl, entity, String.class);
        tokenStore.setToken(authResponse.getBody());

        return authResponse.getBody();
    }

    @Override
    public LogInResponse logIn(LogInRequest request) {
        log.info("Logging in...");
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException(("User not found with username: ") + request.username()));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IncorrectPasswordException(("Incorrect password provided"));
        }
        String token = generateToken(request.username());
        tokenStore.setRole(user.getRole().name());
        return new LogInResponse(token, user.getUsername(), "Login successful");
    }
}
