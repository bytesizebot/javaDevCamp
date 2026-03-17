package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.UserDto;
import za.co.entelect.java_devcamp.request.LogInRequest;
import za.co.entelect.java_devcamp.response.LogInResponse;

public interface IUserService {
    void createUser(UserDto userDto);
    LogInResponse logIn(LogInRequest request);
}
