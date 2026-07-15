package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.request.UserAnswerRequest;
import com.example.MyQuizApp.dto.response.UserAnswerResponse;

import java.util.List;

public interface UserAnswerService {

    UserAnswerResponse saveUserAnswer(UserAnswerRequest userAnswerRequest);

    List<UserAnswerResponse> getAllUserAnswers();

    UserAnswerResponse getUserAnswerById(Integer id);

    UserAnswerResponse updateUserAnswer(Integer id, UserAnswerRequest userAnswerRequest);

    void deleteUserAnswer(Integer id);

}