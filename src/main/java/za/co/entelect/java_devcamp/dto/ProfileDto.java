package za.co.entelect.java_devcamp.dto;

public record ProfileDto(
        String username,
        String firstName,
        String lastName,
        String idNumber,
        Long customerTypeId
) {
}
