package za.co.entelect.java_devcamp.service;

import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.entity.User;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProfileMapper;
import za.co.entelect.java_devcamp.repository.ProfileRepository;
import za.co.entelect.java_devcamp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }


    @Override
    public void createUserProfile(ProfileDto profileDto) {
        if (!userRepository.existsByUsername(profileDto.username())) {
            throw new ResourceNotFoundException("User does not exist. Please register first.");
        }
        User user = userRepository.findByUsername(profileDto.username())
                .orElseThrow(() -> new ResourceNotFoundException(("User not found with username: ") + profileDto.username()));

        Profile profile = profileMapper.toProfileEntity(profileDto);
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);
        profileMapper.toProfileDto(savedProfile);
    }

    @Override
    public void updateUser(Long id, ProfileDto profileDto) {

    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        return null;
    }

    @Override
    public ProfileDto getProfileByUserName(String username) {
        Profile profile = profileRepository.findByEmailAddress(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + username));
        return profileMapper.toProfileDto(profile);
    }
}
