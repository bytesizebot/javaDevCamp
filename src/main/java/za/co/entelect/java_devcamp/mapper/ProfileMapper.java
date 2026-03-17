package za.co.entelect.java_devcamp.mapper;

import org.springframework.stereotype.Component;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;

@Component
public class ProfileMapper {

    public ProfileDto toProfileDto(Profile profile){
        return new ProfileDto(
                profile.getUser().getUsername(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getIdNumber(),
                profile.getPhoneNumber()
        );
    }

    public Profile toProfileEntity(ProfileDto profileDto){
        Profile profile = new Profile();
        profile.setFirstName(profileDto.firstName());
        profile.setLastName(profileDto.lastName());
        profile.setIdNumber(profileDto.idNumber());
        profile.setEmailAddress(profileDto.username());
        profile.setPhoneNumber(profileDto.phoneNumber());
        return profile;
    }
}
