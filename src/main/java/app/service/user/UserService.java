package app.service.user;

import app.model.entity.dto.user.UserDto;
import app.model.entity.dto.user.UserLoginRequestDto;
import app.model.entity.dto.user.UserRegisterRequestDto;
import app.model.entity.user.User;
import app.model.mapper.user.UserMapper;
import app.repository.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Account Creation, validate the user input and create a new user account
    public UserDto register(UserRegisterRequestDto userRegisterRequest) {
        //check if user already exists
        userRepository.findByUsername(userRegisterRequest.getUsername()).ifPresent(user -> {
            throw new IllegalArgumentException("(User with username " + userRegisterRequest.getUsername() + " already exists)");
        });

        //encoding the password
        String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(encodedPassword);

        //create user, without skill progress. Skill progress will be created when the user logs in
        User userEntity = UserMapper.toUserEntity(userRegisterRequest);

        //save user
        userRepository.save(userEntity);
        return UserMapper.toUserDto(userEntity);
    }

    //log in registered user
    public UserDto login(UserLoginRequestDto userLoginRequestDto) {
        //1.Verify the username exists and confirm the password matches securely
        Optional<User> optionalUser = userRepository.findByUsername(userLoginRequestDto.getUsername());
        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(userLoginRequestDto.getPassword(), optionalUser.get().getPassword())) {
            throw new IllegalArgumentException("Username or password is mismatch!");
        }

        //3.Return the logged-in optionalUser
        return UserMapper.toUserDto(optionalUser.get());
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void save(User user) {
        userRepository.save(user);
    }

//    public UserProfileDto getCurrentUserProfile() {
//        User user = getLoggedUser();
//
//        boolean isNew = user.getEducationPoints() == 0 &&
//                user.getPhysicalPoints() == 0 &&
//                user.getHobbyPoints() == 0 &&
//                user.getProfessionalPoints() == 0;
//
//        UserProfileDto dto = mapper.toProfileDto(user);
//        if (isNew) {
//            dto.setMessage("Започваш от нула. Успех!");
//        }
//
//        return dto;
//    }
//
//    public UserStatsDto getUserStats(UUID userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Проверка дали е нов потребител
//        if (user.getEducationPoints() == 0 &&
//                user.getPhysicalPoints() == 0 &&
//                user.getHobbyPoints() == 0 &&
//                user.getProfessionalPoints() == 0) {
//
//            // Тук връщаш съобщение към контролера/фронтенда
//            return new UserStatsDto(user, "Започваш от нула. Успех!");
//        }
//
//        return new UserStatsDto(user, null);
//    }
}
