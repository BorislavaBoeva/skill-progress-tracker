package app.service.user;

import app.exception.*;
import app.model.dto.user.UserEditRequestDto;
import app.model.dto.user.UserDto;
import app.model.dto.user.UserLoginRequestDto;
import app.model.dto.user.UserRegisterRequestDto;
import app.model.entity.user.User;
import app.model.entity.user.UserRole;
import app.model.mapper.user.UserMapper;
import app.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Account Creation, validate the user input and create a new user account
    public void register(UserRegisterRequestDto userRegisterRequest) {
        //check if user already exists
        userRepository.findByUsername(userRegisterRequest.getUsername())
                .ifPresent(user -> {
                    throw new DuplicateResourceException(
                            "(User with username " + userRegisterRequest.getUsername() + " already exists)");
                });

        //encoding the password
        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(encodedPassword);

        //create user, without skill progress. Skill progress will be created when the user logs in
        User userEntity = UserMapper.toUserEntity(userRegisterRequest);

        //save user
        userRepository.save(userEntity);
    }

    //log in registered user
    public UserDto login(UserLoginRequestDto userLoginRequestDto) {
        //1.Verify the username exists and confirm the password matches securely
        Optional<User> optionalUser = userRepository.findByUsername(userLoginRequestDto.getUsername());
        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(userLoginRequestDto.getPassword(), optionalUser.get().getPassword())) {
            throw new InvalidCredentialsException("Username or password is mismatch!");
        }
        User user = optionalUser.get();
        if (!user.isEnabled()) {
            throw new AccountDisabledException("This account has been disabled. Please contact support.");
        }
        //2.Return the logged-in optionalUser
        return UserMapper.toUserDto(user);
    }

    public UserDto getById(UUID id) {
        return UserMapper.toUserDto(getUserOrThrow(id));
    }

    public User getEntityById(UUID id) {
        return getUserOrThrow(id);
    }

    //check why build edit request is needed
    public UserEditRequestDto getEditRequestById(UUID id) {
        UserDto user = getById(id);
        return UserMapper.toEditRequestDto(user);
    }

    public UserDto updateProfile(UUID id, UserEditRequestDto userEditRequest) {
        User entity = getUserOrThrow(id);

        userRepository.findByEmail(userEditRequest.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(entity.getId())) {
                        throw new DuplicateResourceException("Email is already in use.");
                    }
                });

        entity.setFirstName(userEditRequest.getFirstName());
        entity.setLastName(userEditRequest.getLastName());
        entity.setEmail(userEditRequest.getEmail());
        entity.setProfilePicture(userEditRequest.getProfilePicture());
        User updatedUser = userRepository.save(entity);
        return UserMapper.toUserDto(updatedUser);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void switchStatus(UUID userId) {
        User user = getUserOrThrow(userId);

        if (user.getRole() == UserRole.ADMIN) {
            throw new UnauthorizedActionException("Cannot change status of an admin account");
        }

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    public void deleteUser(UUID userId, UUID requestingUserId) {
        if (userId.equals(requestingUserId)) {
            throw new UnauthorizedActionException("You cannot delete your own account");
        }

        User user = getUserOrThrow(userId);

        if (user.getRole() == UserRole.ADMIN) {
            throw new UnauthorizedActionException("Cannot delete an admin account");
        }

        userRepository.delete(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    // общ helper — премахва дублирането от 5 метода
    private User getUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with id [%s] does not exist.".formatted(id)));
    }
}