package com.example.MyQuizApp.config;

import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.enums.Role;
import com.example.MyQuizApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        User admin = userRepository.findByEmail("admin@myquizapp.com");

        if (admin == null) {

            User defaultAdmin = User.builder()
                    .name("Admin")
                    .email("admin@myquizapp.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();

            userRepository.save(defaultAdmin);

            System.out.println("Default admin created successfully.");
        }
    }
}
