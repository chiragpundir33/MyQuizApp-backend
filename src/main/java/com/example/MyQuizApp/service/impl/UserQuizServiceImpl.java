package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.entity.Quiz;
import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.entity.UserQuiz;
import com.example.MyQuizApp.dto.request.UserQuizRequest;
import com.example.MyQuizApp.dto.response.UserQuizResponse;
import com.example.MyQuizApp.repository.QuizRepository;
import com.example.MyQuizApp.repository.UserQuizRepository;
import com.example.MyQuizApp.repository.UserRepository;
import com.example.MyQuizApp.service.UserQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserQuizServiceImpl implements UserQuizService {

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;



    @Override
    public UserQuizResponse saveUserQuiz(UserQuizRequest userQuizRequest) {
        User user = userRepository.findById(userQuizRequest.getUserSid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizRepository.findById(userQuizRequest.getQuizSid())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));


        UserQuiz userQuiz = UserQuiz.builder()
                .user(user)
                .quiz(quiz)
                .score(BigDecimal.ZERO)
                .attemptedAt(LocalDateTime.now())
                .build();

       UserQuiz savedUserQuiz = userQuizRepository.save(userQuiz);

        return UserQuizResponse.builder()
                .id(savedUserQuiz.getId())
                .userSid(savedUserQuiz.getUser().getId())
                .quizSid(savedUserQuiz.getQuiz().getId())
                .score(savedUserQuiz.getScore())
                .attemptedAt(savedUserQuiz.getAttemptedAt())
                .build();
    }

    @Override
    public List<UserQuizResponse> getAllUserQuizzes() {
         List<UserQuiz> userQuizs = userQuizRepository.findAll();

         return userQuizs.stream()
                 .map(userQuiz -> UserQuizResponse.builder()
                         .id(userQuiz.getId())
                         .userSid(userQuiz.getUser().getId())
                         .quizSid(userQuiz.getQuiz().getId())
                         .score(userQuiz.getScore())
                         .attemptedAt(userQuiz.getAttemptedAt())
                         .build())
                 .toList();

    }

    @Override
    public UserQuizResponse getUserQuizById(Integer id) {
         Optional<UserQuiz> userQuiz = userQuizRepository.findById(id);

         return UserQuizResponse.builder()
                 .id(userQuiz.get().getId())
                 .userSid(userQuiz.get().getUser().getId())
                 .quizSid(userQuiz.get().getQuiz().getId())
                 .score(userQuiz.get().getScore())
                 .attemptedAt(userQuiz.get().getAttemptedAt())
                 .build();

    }

    @Override
    public UserQuizResponse updateUserQuiz(Integer id, UserQuizRequest userQuizRequest) {

         UserQuiz existingUserQuiz = userQuizRepository.findById(id)
                 .orElseThrow(()->new RuntimeException("UserQuiz not found"));


        User user = userRepository.findById(userQuizRequest.getUserSid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizRepository.findById(userQuizRequest.getQuizSid())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

         existingUserQuiz.setUser(user);
         existingUserQuiz.setQuiz(quiz);

         UserQuiz updatedUserQuiz = userQuizRepository.save(existingUserQuiz);

        return UserQuizResponse.builder()
                .id(updatedUserQuiz.getId())
                .userSid(updatedUserQuiz.getUser().getId())
                .quizSid(updatedUserQuiz.getQuiz().getId())
                .score(updatedUserQuiz.getScore())
                .attemptedAt(updatedUserQuiz.getAttemptedAt())
                .build();

    }


    @Override
    public void deleteUserQuiz(Integer id) {
        userQuizRepository.deleteById(id);
    }
}
