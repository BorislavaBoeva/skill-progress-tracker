package app.init;

import app.model.entity.user.ProgressLevel;
import app.model.entity.user.User;
import app.model.entity.user.UserRole;
import app.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seedAdmin() {
        boolean adminExists = userRepository.existsByRole(UserRole.ADMIN);
        if (adminExists) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Admin")
                .lastName("Admin")
                .email("admin@skilltracker.com")
                .role(UserRole.ADMIN)
                .education(ProgressLevel.BEGINNER)
                .physical(ProgressLevel.BEGINNER)
                .hobby(ProgressLevel.BEGINNER)
                .professional(ProgressLevel.BEGINNER)
                .build();

        userRepository.save(admin);
    }
}