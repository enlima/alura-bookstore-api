package br.com.alura.bookstore.service;

import br.com.alura.bookstore.dto.UserDetailsDto;
import br.com.alura.bookstore.dto.UserDto;
import br.com.alura.bookstore.dto.UserFormDto;
import br.com.alura.bookstore.dto.UserUpdateFormDto;
import br.com.alura.bookstore.model.Profile;
import br.com.alura.bookstore.model.User;
import br.com.alura.bookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public UserDto register(UserFormDto dto) {

        checkIfUserAlreadyExists(dto.getLogin());

        User user = modelMapper.map(dto, User.class);
        user.setId(null);
        user.setPassword(encoder.encode(
                String.valueOf(new Random().nextInt(999999))));
        user.addProfile(profileService.getProfile(dto.getProfileId()));

        userRepository.save(user);

        return modelMapper.map(user, UserDto.class);
    }

    public Page<UserDto> list(Pageable paging) {

        Page<User> users = userRepository.findAll(paging);
        return users.map(t -> modelMapper.map(t, UserDto.class));
    }

    public UserDetailsDto detail(Long id) {

        User user = getUser(id);
        return modelMapper.map(user, UserDetailsDto.class);
    }

    @Transactional
    public UserDetailsDto update(UserUpdateFormDto dto) {

        User user = getUser(dto.getId());

        if (!user.getLogin().trim().equals(dto.getLogin().trim())) {
            checkIfUserAlreadyExists(dto.getLogin());
        }

        List<Profile> profiles = new ArrayList<>();

        for (Long profileId : dto.getProfilesId()) {
            profiles.add(profileService.getProfile(profileId));
        }

        user.updateInfo(dto.getName(), dto.getLogin(), dto.getEmail(), profiles);

        return modelMapper.map(user, UserDetailsDto.class);
    }

    public void checkIfUserAlreadyExists(String login) {

        if (userRepository.existsByLogin(login.trim())) {
            throw new DataIntegrityViolationException("Login '" + login.trim() + "' already exists " +
                    "associated with a different User ID!");
        }
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Informed user " +
                "(ID: " + id + ") not found!"));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
