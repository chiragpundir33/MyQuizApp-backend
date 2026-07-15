package com.example.MyQuizApp.config;

import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.enums.Role;
import com.example.MyQuizApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_NAME}")
    private String adminName;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;


    @Override
    public void run(String... args) {

        User admin = userRepository.findByEmail(adminEmail);

        if (admin == null) {

            User defaultAdmin = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();

            userRepository.save(defaultAdmin);

            System.out.println("Default admin created successfully.");
        }
    }
}