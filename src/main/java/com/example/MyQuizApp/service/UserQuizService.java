package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.request.UserQuizRequest;
import com.example.MyQuizApp.dto.response.UserQuizResponse;

import java.util.List;

public interface UserQuizService {

    UserQuizResponse saveUserQuiz(UserQuizRequest userQuizRequest);

    List<UserQuizResponse> getAllUserQuizzes();

    UserQuizResponse getUserQuizById(Integer id);

    UserQuizResponse updateUserQuiz(Integer id, UserQuizRequest userQuizRequest);

    void deleteUserQuiz(Integer id);

}