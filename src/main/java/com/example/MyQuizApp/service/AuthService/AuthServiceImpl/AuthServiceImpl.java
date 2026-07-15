package com.example.MyQuizApp.service.AuthService.AuthServiceImpl;

import com.example.MyQuizApp.dto.auth.AuthResponse;
import com.example.MyQuizApp.dto.auth.LoginRequest;
import com.example.MyQuizApp.dto.auth.RegisterRequest;
import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.enums.Role;
import com.example.MyQuizApp.repository.UserRepository;
import com.example.MyQuizApp.service.AuthService.AuthService;
import com.example.MyQuizApp.service.JwtService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                null
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail());

        String token = jwtService.generateToken(user.getEmail(),user.getRole().name());

        return new AuthResponse(
                "Login successful",
                token
        );
    }
}