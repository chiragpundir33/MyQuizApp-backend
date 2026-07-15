package com.example.MyQuizApp.service;


import com.example.MyQuizApp.dto.request.QuestionRequest;
import com.example.MyQuizApp.dto.response.QuestionResponse;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    QuestionResponse saveQuestion(QuestionRequest questionRequest);

    List<QuestionResponse> getAllQuestions();

    Optional<QuestionResponse> getQuestionById(Integer id);

    QuestionResponse updateQuestion(Integer id, QuestionRequest questionRequest);

    void deleteQuestion(Integer id);


}
