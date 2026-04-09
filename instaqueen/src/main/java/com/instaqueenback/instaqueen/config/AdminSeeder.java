package com.instaqueenback.instaqueen.config;

import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.enums.Role;
import com.instaqueenback.instaqueen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@queenpharmacy.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@queenpharmacy.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Admin")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created: admin@queenpharmacy.com / admin123");
        } else {
            log.info("Admin user already exists, skipping seed");
        }
    }
}
