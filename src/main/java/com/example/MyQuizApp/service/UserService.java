package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.request.UserRequest;
import com.example.MyQuizApp.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponse saveUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    Optional<UserResponse> getUserById(Integer id);

    UserResponse updateUser(Integer id, UserRequest userRequest);

    void deleteUser(Integer id);



}
