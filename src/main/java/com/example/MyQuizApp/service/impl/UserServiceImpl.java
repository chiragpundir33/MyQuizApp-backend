package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.dto.request.UserRequest;
import com.example.MyQuizApp.dto.response.UserResponse;
import com.example.MyQuizApp.repository.UserRepository;
import com.example.MyQuizApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
           User user = User.builder()
                   .id(userRequest.getId())
                   .name(userRequest.getName())
                   .isActive(userRequest.getIsActive() != null ? userRequest.getIsActive() : true)
                   .build();

           User savedUser = userRepository.save(user);

           return UserResponse.builder()
                   .id(savedUser.getId())
                   .name(savedUser.getName())
                   .email(savedUser.getEmail())
                   .role(savedUser.getRole())
                   .isActive(savedUser.getIsActive())
                   .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
          List<User> users = userRepository.findAll();

          return users.stream()
                  .map(user -> UserResponse.builder()
                          .id(user.getId())
                          .name(user.getName())
                          .email(user.getEmail())
                          .role(user.getRole())
                          .isActive(user.getIsActive())
                          .build())
                  .toList();
    }

    @Override
    public Optional<UserResponse> getUserById(Integer id) {
          Optional<User> user = userRepository.findById(id);

          if (user.isEmpty()) {
              return Optional.empty();
          }

          return Optional.of(UserResponse.builder()
                  .id(user.get().getId())
                  .name(user.get().getName())
                  .email(user.get().getEmail())
                  .role(user.get().getRole())
                  .isActive(user.get().getIsActive())
                  .build());
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRequest.getName() != null) {
            existingUser.setName(userRequest.getName());
        }
        if (userRequest.getIsActive() != null) {
            existingUser.setIsActive(userRequest.getIsActive());
        }

        userRepository.save(existingUser);

        return UserResponse.builder()
                .id(existingUser.getId())
                .name(existingUser.getName())
                .email(existingUser.getEmail())
                .role(existingUser.getRole())
                .isActive(existingUser.getIsActive())
                .build();
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
