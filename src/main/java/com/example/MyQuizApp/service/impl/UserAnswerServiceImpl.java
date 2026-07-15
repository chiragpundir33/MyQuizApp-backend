package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.dto.request.UserAnswerRequest;
import com.example.MyQuizApp.dto.response.UserAnswerResponse;
import com.example.MyQuizApp.entity.Question;
import com.example.MyQuizApp.entity.UserAnswer;
import com.example.MyQuizApp.entity.UserQuiz;
import com.example.MyQuizApp.repository.QuestionRepository;
import com.example.MyQuizApp.repository.UserAnswerRepository;
import com.example.MyQuizApp.repository.UserQuizRepository;
import com.example.MyQuizApp.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public UserAnswerResponse saveUserAnswer(UserAnswerRequest userAnswerRequest) {

        UserQuiz userQuiz = userQuizRepository.findById(userAnswerRequest.getUserQuizId())
                .orElseThrow(() -> new RuntimeException("UserQuiz not found"));

        Question question = questionRepository.findById(userAnswerRequest.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean isCorrect = question.getRightAnswer()
                .equalsIgnoreCase(userAnswerRequest.getSelectedAnswer());

        UserAnswer userAnswer = UserAnswer.builder()
                .userQuiz(userQuiz)
                .question(question)
                .selectedAnswer(userAnswerRequest.getSelectedAnswer())
                .isCorrect(isCorrect)
                .build();

        UserAnswer savedUserAnswer = userAnswerRepository.save(userAnswer);

        return UserAnswerResponse.builder()
                .id(savedUserAnswer.getId())
                .userQuizId(savedUserAnswer.getUserQuiz().getId())
                .questionId(savedUserAnswer.getQuestion().getId())
                .selectedAnswer(savedUserAnswer.getSelectedAnswer())
                .isCorrect(savedUserAnswer.getIsCorrect())
                .build();

    }

    @Override
    public List<UserAnswerResponse> getAllUserAnswers() {
        List<UserAnswer> userAnswers = userAnswerRepository.findAll();

        return  userAnswers.stream()
                .map(userAnswer -> UserAnswerResponse.builder()
                        .id(userAnswer.getId())
                        .userQuizId(userAnswer.getUserQuiz().getId())
                        .questionId(userAnswer.getQuestion().getId())
                        .selectedAnswer(userAnswer.getSelectedAnswer())
                        .isCorrect(userAnswer.getIsCorrect())
                        .build())
                .toList();

    }

    @Override
    public UserAnswerResponse getUserAnswerById(Integer id) {
         Optional<UserAnswer> userAnswer = userAnswerRepository.findById(id);

         return UserAnswerResponse.builder()
                 .id(userAnswer.get().getId())
                 .userQuizId(userAnswer.get().getUserQuiz().getId())
                 .questionId(userAnswer.get().getQuestion().getId())
                 .selectedAnswer(userAnswer.get().getSelectedAnswer())
                 .isCorrect(userAnswer.get().getIsCorrect())
                 .build();
    }

    @Override
    public UserAnswerResponse updateUserAnswer(Integer id, UserAnswerRequest userAnswerRequest) {

        UserAnswer existingUserAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAnswer not found"));

        UserQuiz userQuiz = userQuizRepository.findById(userAnswerRequest.getUserQuizId())
                .orElseThrow(() -> new RuntimeException("UserQuiz not found"));

        Question question = questionRepository.findById(userAnswerRequest.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean isCorrect = question.getRightAnswer()
                .equalsIgnoreCase(userAnswerRequest.getSelectedAnswer());

        existingUserAnswer.setUserQuiz(userQuiz);
        existingUserAnswer.setQuestion(question);
        existingUserAnswer.setSelectedAnswer(userAnswerRequest.getSelectedAnswer());
        existingUserAnswer.setIsCorrect(isCorrect);

        UserAnswer updatedUserAnswer = userAnswerRepository.save(existingUserAnswer);

        return UserAnswerResponse.builder()
                .id(updatedUserAnswer.getId())
                .userQuizId(updatedUserAnswer.getUserQuiz().getId())
                .questionId(updatedUserAnswer.getQuestion().getId())
                .selectedAnswer(updatedUserAnswer.getSelectedAnswer())
                .isCorrect(updatedUserAnswer.getIsCorrect())
                .build();
    }

    @Override
    public void deleteUserAnswer(Integer id) {

        UserAnswer userAnswer = userAnswerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAnswer not found"));

        userAnswerRepository.delete(userAnswer);
    }
}
