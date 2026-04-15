package za.co.entelect.java_devcamp.serviceinterface;

import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;

import java.util.List;


public interface IProfileService {
    void createUserProfile(ProfileDto profileDto);
    void updateUser(Long id, ProfileDto profileDto);

    List<ProfileDto> getAllProfiles();

    Profile getProfileByUserName(String username);
    Profile getProfileByIdNumber(String username);

}
