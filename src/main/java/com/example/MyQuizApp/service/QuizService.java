package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.request.QuizRequest;
import com.example.MyQuizApp.dto.request.QuizSubmissionRequest;
import com.example.MyQuizApp.dto.response.*;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    QuizResponse saveQuiz(QuizRequest quizRequest);

    List<QuizResponse> getAllQuizzes();

    Optional<QuizResponse> getQuizById(Integer id);

    QuizResponse updateQuiz(Integer id, QuizRequest quizRequest);

    void deleteQuiz(Integer id);


    List<QuizQuestionResponse> getQuizQuestions(Integer quizId);


    QuizResultResponse submitQuiz(QuizSubmissionRequest request);

    List<ReviewAnswerResponse> reviewQuiz(Integer userQuizId);

    List<QuizHistoryResponse> getQuizHistory(Integer userId);

    HighestScoreResponse getHighestScore(Integer userId);

    List<LeaderboardResponse> getLeaderboard();

    List<QuizDetailResponse> QuizDetails();

}
