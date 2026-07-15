package com.example.MyQuizApp.service.AuthService;

import com.example.MyQuizApp.dto.auth.LoginRequest;
import com.example.MyQuizApp.dto.auth.RegisterRequest;
import com.example.MyQuizApp.dto.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}
