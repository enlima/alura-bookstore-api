package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.UserDetailsDto;
import br.com.alura.bookstore.dto.UserDto;
import br.com.alura.bookstore.dto.UserFormDto;
import br.com.alura.bookstore.dto.UserUpdateFormDto;
import br.com.alura.bookstore.infra.email.EmailService;
import br.com.alura.bookstore.model.Profile;
import br.com.alura.bookstore.model.User;
import br.com.alura.bookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileService profileService;

    @Mock
    private EmailService emailService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    public User createUser() {

        Profile profile = new Profile(1L, "ROLE_ADMIN");
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);

        return new User(1L, "Gimli", "lockbearer", "2879", "dwarf@mail.com", profiles);
    }

    public UserFormDto createUserFormDto() {
        return new UserFormDto("Gimli", "lockbearer", "dwarf@mail.com", 1L);
    }

    @Test
    public void shouldRegisterNewUserIfValidData() {

        UserFormDto formDto = createUserFormDto();
        User user = createUser();

        when(modelMapper.map(formDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class))
                .thenReturn(new UserDto(user.getId(), user.getName(), user.getLogin(), user.getEmail()));
        when(encoder.encode(any(String.class))).thenReturn("123456");

        UserDto dto = userService.register(formDto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(any(String.class), any(String.class), any(String.class));

        assertEquals(1L, dto.getId());
        assertEquals(formDto.getName(), dto.getName());
        assertEquals(formDto.getLogin(), dto.getLogin());
        assertEquals(formDto.getEmail(), dto.getEmail());
    }

    @Test
    public void shouldNotRegisterNewUserIfLoginAlreadyExists() {

        UserFormDto formDto = createUserFormDto();

        when(userRepository.existsByLogin(formDto.getLogin())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> userService.register(formDto));
    }

    @Test
    public void shouldNotRegisterNewUserIfEmailAlreadyExists() {

        UserFormDto formDto = createUserFormDto();

        when(userRepository.existsByEmail(formDto.getEmail())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> userService.register(formDto));
    }

    @Test
    public void shouldReturnUserDetails() {

        User user = createUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDetailsDto.class))
                .thenReturn(new UserDetailsDto(user.getId(), user.getName(), user.getLogin(), user.getEmail(), user.getProfiles()));

        UserDetailsDto detailsDto = userService.detail(1L);

        assertEquals(user.getId(), detailsDto.getId());
        assertEquals(user.getName(), detailsDto.getName());
        assertEquals(user.getLogin(), detailsDto.getLogin());
        assertEquals(user.getEmail(), detailsDto.getEmail());
        assertEquals(user.getProfiles(), detailsDto.getProfiles());
    }

    @Test
    public void shouldNotReturnUserDetailsIfIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> userService.detail(1L));
    }

    @Test
    public void shouldUpdateUserInfoIfValidData() {

        Profile profile = new Profile(2L, "ROLE_COMMON");
        List<Long> profilesId = List.of(2L);
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);

        User user = createUser();
        UserUpdateFormDto formDto = new UserUpdateFormDto(1L, "lockbearer", "dwarf", "dwarf@mail.com", profilesId);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDetailsDto.class))
                .thenReturn(new UserDetailsDto(user.getId(), formDto.getName(), formDto.getLogin(), formDto.getEmail(), profiles));
        when(profileService.getProfile(any(Long.class))).thenReturn(profile);

        UserDetailsDto detailsDto = userService.update(formDto);

        verify(emailService, times(1)).sendEmail(any(String.class), any(String.class), any(String.class));

        assertEquals(formDto.getId(), detailsDto.getId());
        assertEquals(formDto.getName(), detailsDto.getName());
        assertEquals(formDto.getLogin(), detailsDto.getLogin());
        assertEquals(formDto.getEmail(), detailsDto.getEmail());
        assertEquals(profilesId.get(0), detailsDto.getProfiles().get(0).getId());
        assertEquals("ROLE_COMMON", detailsDto.getProfiles().get(0).getName());
    }

    @Test
    public void shouldNotUpdateUserInfoIfInformedLoginAlreadyExists() {

        List<Long> profilesId = new ArrayList<>();
        profilesId.add(1L);

        User userA = createUser();
        UserUpdateFormDto formDto = new UserUpdateFormDto(1L, "Gimli", "dwarf", "dwarf@mail.com", profilesId);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userA));
        when(userRepository.existsByLogin(formDto.getLogin())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(formDto));
    }

    @Test
    public void shouldNotUpdateUserInfoIfInformedEmailAlreadyExists() {

        List<Long> profilesId = new ArrayList<>();
        profilesId.add(1L);

        User userA = createUser();
        UserUpdateFormDto formDto = new UserUpdateFormDto(1L, "Gimli", "lockbearer", "dwarf@axe.com", profilesId);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userA));
        when(userRepository.existsByEmail(formDto.getEmail())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(formDto));
    }

    @Test
    public void shouldDeleteUser() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
