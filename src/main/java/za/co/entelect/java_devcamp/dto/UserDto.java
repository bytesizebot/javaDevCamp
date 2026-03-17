package za.co.entelect.java_devcamp.dto;

import za.co.entelect.java_devcamp.entity.Role;

public record UserDto(String username,
                      String password,
                      Role role) {
}
