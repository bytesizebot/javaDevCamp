package za.co.entelect.java_devcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.configs.JwtUtils;
import za.co.entelect.java_devcamp.dto.UserDto;
import za.co.entelect.java_devcamp.entity.User;
import za.co.entelect.java_devcamp.exception.IncorrectPasswordException;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.UserMapper;
import za.co.entelect.java_devcamp.repository.UserRepository;
import za.co.entelect.java_devcamp.request.LogInRequest;
import za.co.entelect.java_devcamp.response.LogInResponse;
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void createUser(UserDto userDto) {
        if(userRepository.existsByUsername(userDto.username())){
            throw new RuntimeException("User already exists.");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userMapper.toDto(savedUser);
    }

    @Override
    public LogInResponse logIn(LogInRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException(("User not found with username: ") + request.username()));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new IncorrectPasswordException(("Incorrect password provided"));
        }
        String token = jwtUtils.generateToken(user.getUsername());

        return new LogInResponse(token, user.getUsername(), "Login successful");
    }
}
