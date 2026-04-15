package za.co.entelect.java_devcamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.entity.User;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProfileMapper;
import za.co.entelect.java_devcamp.model.Notification;
import za.co.entelect.java_devcamp.repository.ProfileRepository;
import za.co.entelect.java_devcamp.repository.UserRepository;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;
import za.co.entelect.java_devcamp.serviceinterface.IProfileService;
import za.co.entelect.java_devcamp.util.NotificationContent;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final INotificationService iNotificationService;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public void createUserProfile(ProfileDto profileDto) {
        logger.info("Creating a new user profile");
        if (!userRepository.existsByUsername(profileDto.username())) {
            log.info("You need to register as a user first");
            throw new ResourceNotFoundException("User does not exist. Please register first.");
        }
        User user = userRepository.findByUsername(profileDto.username())
                .orElseThrow(() -> new ResourceNotFoundException(("User not found with username: ") + profileDto.username()));

        Profile profile = profileMapper.toProfileEntity(profileDto);
        profile.setUser(user);
        Profile savedProfile = profileRepository.save(profile);
        profileMapper.toProfileDto(savedProfile);

        String subject = "Welcome " + user.getUsername() + " !";
        Notification notification = new Notification(user.getUsername(), subject, NotificationContent.Welcome_message);
        iNotificationService.sendNotification(notification);
    }

    @Override
    public void updateUser(Long id, ProfileDto profileDto) {

    }

    @Override
    public List<ProfileDto> getAllProfiles() {
        return null;
    }

    @Override
    public Profile getProfileByUserName(String username) {
        return profileRepository.findByEmailAddress(username)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with username: " + username));
    }

    @Override
    public Profile getProfileByIdNumber(String idNumber) {
        return profileRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with ID Number: " + idNumber));
    }
}
