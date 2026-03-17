package za.co.entelect.java_devcamp.mapper;

import org.springframework.stereotype.Component;
import za.co.entelect.java_devcamp.dto.UserDto;
import za.co.entelect.java_devcamp.entity.User;

@Component
public class UserMapper {

    public UserDto toDto(User user){
        return new UserDto(
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

    public User toEntity(UserDto userDto){
        return new User(
                userDto.username(),
                userDto.password(),
                userDto.role()
        );
    }
}
