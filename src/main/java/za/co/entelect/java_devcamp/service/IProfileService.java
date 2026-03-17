package za.co.entelect.java_devcamp.service;

import za.co.entelect.java_devcamp.dto.ProfileDto;

import java.util.List;


public interface IProfileService {
    void createUserProfile(ProfileDto profileDto);
    void updateUser(Long id, ProfileDto profileDto);

    List<ProfileDto> getAllProfiles();

    ProfileDto getProfileByUserName(String username);

}
