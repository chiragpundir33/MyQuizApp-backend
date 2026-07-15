package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.dto.request.QuizSubmissionRequest;
import com.example.MyQuizApp.dto.request.SubmittedAnswerRequest;
import com.example.MyQuizApp.dto.response.*;
import com.example.MyQuizApp.entity.Question;
import com.example.MyQuizApp.entity.Quiz;
import com.example.MyQuizApp.dto.request.QuizRequest;
import com.example.MyQuizApp.entity.UserAnswer;
import com.example.MyQuizApp.entity.UserQuiz;
import com.example.MyQuizApp.entity.QuizAssignment;
import com.example.MyQuizApp.enums.AssignmentStatus;
import com.example.MyQuizApp.repository.*;
import com.example.MyQuizApp.service.QuizService;
import com.example.MyQuizApp.service.UserQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.MyQuizApp.dto.response.QuizResultResponse.*;


@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuizAssignmentRepository quizAssignmentRepository;



    @Override
    public QuizResponse saveQuiz(QuizRequest quizRequest) {

        List<Question> questions = quizRequest.getQuestionIds() != null
                ? questionRepository.findAllById(quizRequest.getQuestionIds())
                : List.of();

        Quiz quiz = Quiz.builder()
                .title(quizRequest.getTitle())
                .questions(questions)
                .build();

        quizRepository.save(quiz);

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .build();

    }

    @Override
    public List<QuizResponse> getAllQuizzes() {
        List<Quiz> quizs = quizRepository.findAll();

        return quizs.stream()
                .map(quiz -> QuizResponse.builder()
                        .id(quiz.getId())
                        .title(quiz.getTitle()).build())
                .toList();
    }


    @Override
    public Optional<QuizResponse> getQuizById(Integer id) {

         Optional<Quiz> quiz = quizRepository.findById(id);

         return Optional.ofNullable(QuizResponse.builder()
                 .id(quiz.get().getId())
                 .title(quiz.get().getTitle())
                 .build());
    }

    @Override
    public QuizResponse updateQuiz(Integer id, QuizRequest quizRequest) {

        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Quiz not found"));

        existingQuiz.setTitle(quizRequest.getTitle());
        if (quizRequest.getQuestionIds() != null) {
            List<Question> questions = questionRepository.findAllById(quizRequest.getQuestionIds());
            existingQuiz.setQuestions(questions);
        }

        quizRepository.save(existingQuiz);

        return QuizResponse.builder()
                .id(existingQuiz.getId())
                .title(existingQuiz.getTitle())
                .build();

    }

    @Override
    public void deleteQuiz(Integer id) {
       quizRepository.deleteById(id);
    }




    @Override
    public List<QuizQuestionResponse> getQuizQuestions(Integer quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(()-> new RuntimeException("Quiz not found"));

        return quiz.getQuestions()
                .stream()
                .map(question -> QuizQuestionResponse.builder()
                        .id(question.getId())
                        .questionTitle(question.getQuestionTitle())
                        .option1(question.getOption1())
                        .option2(question.getOption2())
                        .option3(question.getOption3())
                        .option4(question.getOption4())
                        .build())
                .toList();

    }


    @Override
    public QuizResultResponse submitQuiz(QuizSubmissionRequest request) {

        UserQuiz userQuiz = userQuizRepository.findById(request.getUserQuizId())
                .orElseThrow(() -> new RuntimeException("User Quiz not found"));

        int correctAnswers = 0;

        for (SubmittedAnswerRequest answerRequest : request.getAnswers()) {

            Question question = questionRepository.findById(answerRequest.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            boolean isCorrect = question.getRightAnswer()
                    .equalsIgnoreCase(answerRequest.getSelectedAnswer());

            if (isCorrect) {
                correctAnswers++;
            }

            UserAnswer userAnswer = UserAnswer.builder()
                    .userQuiz(userQuiz)
                    .question(question)
                    .selectedAnswer(answerRequest.getSelectedAnswer())
                    .isCorrect(isCorrect)
                    .build();

            userAnswerRepository.save(userAnswer);

        }

        userQuiz.setScore(BigDecimal.valueOf(correctAnswers));
        userQuiz.setAttemptedAt(LocalDateTime.now());

        userQuizRepository.save(userQuiz);

        // Update QuizAssignment status to COMPLETED if it exists
        Optional<QuizAssignment> assignmentOpt = quizAssignmentRepository
                .findByUserIdAndQuizId(userQuiz.getUser().getId(), userQuiz.getQuiz().getId());
        if (assignmentOpt.isPresent()) {
            QuizAssignment assignment = assignmentOpt.get();
            assignment.setStatus(AssignmentStatus.COMPLETED);
            quizAssignmentRepository.save(assignment);
        }

        int totalQuestions = request.getAnswers().size();

        double percentage = ((double) correctAnswers / totalQuestions) * 100;

        return QuizResultResponse.builder()
                .quizId(userQuiz.getQuiz().getId())
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .score(userQuiz.getScore())
                .percentage(percentage)
                .result(percentage >= 40 ? "PASS" : "FAIL")
                .build();
    }



    @Override
    public List<ReviewAnswerResponse> reviewQuiz(Integer userQuizId){

        List<UserAnswer> answers = userAnswerRepository.findByUserQuizId(userQuizId);

        return answers.stream()
                .map(answer -> ReviewAnswerResponse.builder()
                        .questionId(answer.getQuestion().getId())
                        .questionTitle(answer.getQuestion().getQuestionTitle())
                        .selectedAnswer(answer.getSelectedAnswer())
                        .correctAnswer(answer.getQuestion().getRightAnswer())
                        .isCorrect(answer.getIsCorrect())
                        .build())
                .toList();
    }





    @Override
    public List<QuizHistoryResponse> getQuizHistory(Integer userId) {


        List<UserQuiz> attempts =
                userQuizRepository.findByUserIdOrderByAttemptedAtDesc(userId);



        return attempts.stream()
                .map(userQuiz -> QuizHistoryResponse.builder()
                        .quizName(
                                userQuiz.getQuiz().getTitle()
                        )
                        .score(
                                userQuiz.getScore()
                        )
                        .totalQuestions(
                                userQuiz.getQuiz()
                                        .getQuestions()
                                        .size()
                        )
                        .attemptedAt(
                                userQuiz.getAttemptedAt()
                        )
                        .build()
                )
                .toList();

    }



    @Override
    public HighestScoreResponse getHighestScore(Integer userId) {


        BigDecimal highestScore =
                userQuizRepository.findHighestScoreByUserId(userId);


        return HighestScoreResponse.builder()
                .userId(userId)
                .highestScore(
                        highestScore == null
                                ? BigDecimal.ZERO
                                : highestScore
                )
                .build();

    }


    @Override
    public List<LeaderboardResponse> getLeaderboard() {


        List<Object[]> result =
                userQuizRepository.findLeaderboard();


        AtomicInteger rank = new AtomicInteger(1);


        return result.stream()
                .map(row -> LeaderboardResponse.builder()

                        .rank(rank.getAndIncrement())

                        .username(
                                (String) row[1]
                        )

                        .highestScore(
                                (BigDecimal) row[2]
                        )

                        .build()
                )
                .toList();
    }


    @Override
    public List<QuizDetailResponse> QuizDetails() {

        List<UserQuiz> userQuizzes = userQuizRepository.findAll();

        List<QuizDetailResponse> responseList = new ArrayList<>();

        for (UserQuiz userQuiz : userQuizzes) {

            List<QuizDetailResponse.QuestionResponse> questionResponses = new ArrayList<>();
            List<UserAnswer> userAnswers = userAnswerRepository.findByUserQuizId(userQuiz.getId());

            for (Question question : userQuiz.getQuiz().getQuestions()) {

                String selectedAns = userAnswers.stream()
                        .filter(ans -> ans.getQuestion().getId().equals(question.getId()))
                        .map(UserAnswer::getSelectedAnswer)
                        .findFirst()
                        .orElse("Not Answered");

                QuizDetailResponse.QuestionResponse questionResponse =
                        QuizDetailResponse.QuestionResponse.builder()
                                .id(question.getId())
                                .questionTitle(question.getQuestionTitle())
                                .option1(question.getOption1())
                                .option2(question.getOption2())
                                .option3(question.getOption3())
                                .option4(question.getOption4())
                                .difficultyLevel(question.getDifficultyLevel())
                                .category(question.getCategory())
                                .correctAnswer(question.getRightAnswer())
                                .selectedAnswer(selectedAns)
                                .build();

                questionResponses.add(questionResponse);
            }


            QuizDetailResponse.UserQuestionResponse userResponse =
                    QuizDetailResponse.UserQuestionResponse.builder()
                            .fullName(userQuiz.getUser().getName())
                            .userName(userQuiz.getUser().getEmail())
                            .userSid(userQuiz.getUser().getId())
                            .questionResponseList(questionResponses)
                            .totalMarks(userQuiz.getScore().toString())
                            .build();


            QuizDetailResponse quizResponse =
                    QuizDetailResponse.builder()
                            .quizSid(userQuiz.getQuiz().getId())
                            .quizName(userQuiz.getQuiz().getTitle())
                            .userQuestionResponseList(List.of(userResponse))
                            .build();

            responseList.add(quizResponse);

        }

        return responseList;
    }





}
