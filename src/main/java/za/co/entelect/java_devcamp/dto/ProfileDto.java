package za.co.entelect.java_devcamp.dto;

import za.co.entelect.java_devcamp.entity.CustomerAccounts;

import java.util.List;

public record ProfileDto(
        String username,
        String firstName,
        String lastName,
        String idNumber,
        String phoneNumber
) {
}
