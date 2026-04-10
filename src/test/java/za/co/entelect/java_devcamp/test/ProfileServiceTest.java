package za.co.entelect.java_devcamp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.entelect.java_devcamp.dto.ProfileDto;
import za.co.entelect.java_devcamp.entity.Profile;
import za.co.entelect.java_devcamp.entity.User;
import za.co.entelect.java_devcamp.exception.ResourceNotFoundException;
import za.co.entelect.java_devcamp.mapper.ProfileMapper;
import za.co.entelect.java_devcamp.repository.ProfileRepository;
import za.co.entelect.java_devcamp.repository.UserRepository;
import za.co.entelect.java_devcamp.service.ProfileService;
import za.co.entelect.java_devcamp.serviceinterface.INotificationService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private INotificationService iNotificationService;
    @InjectMocks
    private ProfileService profileService;

    private Profile mockProfileEntity;
    private ProfileDto mockProfileDto;
    private User existingUser;

    private String email = "testuser@email.com";

    @BeforeEach
    void setup(){
        profileService = new ProfileService(profileRepository, userRepository, profileMapper, iNotificationService);
        mockProfileEntity = Profile
                .builder()
                .profileId(1L)
                .emailAddress(email)
                .firstName("Test")
                .lastName("Unitt")
                .idNumber("0949484776746")
                .customerTypeId(12L)
                .build();

        mockProfileDto = new ProfileDto("newuser@mail.com", "Novo", "Pessoa", "04948239904034",1L);

        existingUser = new User();
        existingUser.setUsername("newuser@mail.com");
    }

    @Test
    public void testGetProfileByEmailAddress_WhenProfileEmailIsPresent_ThenReturnEmployee(){
        when(profileRepository.findByEmailAddress(email)).thenReturn(Optional.ofNullable(mockProfileEntity));

        Profile returnedProfile = profileService.getProfileByUserName(email);

        assertNotNull(returnedProfile);
        assertThat((returnedProfile.getEmailAddress()).equals(mockProfileEntity.getEmailAddress()));

        verify(profileRepository,only()).findByEmailAddress(email);
    }

    @Test
    public void testGetProfileByEmailAddress_WhenProfileEmailIsNotPresent_ThenThrowException(){
        when(profileRepository.findByEmailAddress(email)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            profileService.getProfileByUserName(email);
        });

        assertEquals("Profile not found with username: testuser@email.com", exception.getMessage());
    }

    @Test
    public void testCreateProfile_WhenUserExists_ShouldSaveProfile() {

        existingUser = new User();
        existingUser.setUsername("newuser@mail.com");

        Profile newProfileEntity = Profile
                .builder()
                .profileId(1L)
                .emailAddress("newuser@mail.com")
                .firstName("Novo")
                .lastName("Pessoa")
                .idNumber("0949484776746")
                .customerTypeId(12L)
                .build();

        when(userRepository.existsByUsername("newuser@mail.com"))
                .thenReturn(true);

        when(userRepository.findByUsername("newuser@mail.com"))
                .thenReturn(Optional.of(existingUser));

        when(profileMapper.toProfileEntity(mockProfileDto))
                .thenReturn(newProfileEntity);

        when(profileRepository.save(any(Profile.class)))
                .thenReturn(newProfileEntity);

        profileService.createUserProfile(mockProfileDto);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository).save(captor.capture());

        Profile saved = captor.getValue();
        assertEquals("newuser@mail.com", saved.getEmailAddress());
    }

}