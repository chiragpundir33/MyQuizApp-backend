package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.dto.request.QuizSubmissionRequest;
import com.example.MyQuizApp.dto.request.SubmittedAnswerRequest;
import com.example.MyQuizApp.dto.request.VideoUrlRequest;
import com.example.MyQuizApp.dto.request.GeneratedQuestionDto;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${transcript.api.key}")
    private String transcriptApiKey;

    @Value("${transcript.api.url}")
    private String transcriptApiUrl;



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


    // ================= NEW: VIDEO-TO-QUIZ GENERATION =================

    @Override
    public QuizResponse generateQuizFromVideo(String videoUrl) {
        String videoId = extractVideoId(videoUrl);
        String transcript = fetchTranscript(videoId);
        
        QuizDetailWrapper generatedData = generateQuizDetailsFromTranscript(transcript);

        List<Question> savedQuestions = new ArrayList<>();
        for (GeneratedQuestionDto dto : generatedData.getQuestions()) {
            Question question = Question.builder()
                    .questionTitle(dto.getQuestionTitle())
                    .option1(dto.getOption1())
                    .option2(dto.getOption2())
                    .option3(dto.getOption3())
                    .option4(dto.getOption4())
                    .rightAnswer(dto.getRightAnswer())
                    .difficultyLevel(dto.getDifficultyLevel() != null ? dto.getDifficultyLevel() : "Easy")
                    .category(dto.getCategory() != null ? dto.getCategory() : "General")
                    .build();
            savedQuestions.add(questionRepository.save(question));
        }

        Quiz quiz = Quiz.builder()
                .title(generatedData.getTitle())
                .summary(generatedData.getSummary())
                .questions(savedQuestions)
                .build();

        quizRepository.save(quiz);

        return QuizResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .summary(quiz.getSummary())
                .build();
    }

    private String extractVideoId(String url) {
        String pattern = "(?:v=|youtu\\.be/|embed/|shorts/)([a-zA-Z0-9_-]{11})";
        Matcher matcher = Pattern.compile(pattern).matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Invalid YouTube URL");
    }

    private String fetchTranscript(String videoId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", transcriptApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = transcriptApiUrl + "?videoId=" + videoId;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            StringBuilder fullText = new StringBuilder();
            
            JsonNode content = root.get("content");
            if (content != null && content.isArray()) {
                for (JsonNode segment : content) {
                    JsonNode textNode = segment.get("text");
                    if (textNode != null) {
                        fullText.append(textNode.asText()).append(" ");
                    }
                }
            } else {
                throw new RuntimeException("Transcript content field is empty or missing");
            }
            return fullText.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse transcript response: " + e.getMessage(), e);
        }
    }

    private QuizDetailWrapper generateQuizDetailsFromTranscript(String transcript) {
        String prompt = "Based on the following video transcript, generate:\n" +
                "1. A suitable, engaging title for the quiz.\n" +
                "2. A concise and informative summary of the video transcript.\n" +
                "3. Exactly 5 multiple choice quiz questions only related to the video content to check understanding.\n" +
                "Return ONLY a JSON object with keys 'title', 'summary', and 'questions'.\n" +
                "The 'questions' key should be an array of exactly 5 objects, each containing: " +
                "'questionTitle', 'option1', 'option2', 'option3', 'option4', 'rightAnswer' (which MUST exactly match one of the four options), " +
                "'difficultyLevel' ('Easy', 'Medium', or 'Hard'), and 'category'.\n" +
                "Do not include any explanation or markdown formatting outside the JSON.\n\n" +
                "Transcript:\n" + transcript;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ObjectMapper mapper = new ObjectMapper();
            
            java.util.Map<String, Object> textPart = java.util.Map.of("text", prompt);
            java.util.Map<String, Object> contentObject = java.util.Map.of("parts", List.of(textPart));
            java.util.Map<String, Object> generationConfig = java.util.Map.of("responseMimeType", "application/json");
            
            java.util.Map<String, Object> requestBodyMap = java.util.Map.of(
                "contents", List.of(contentObject),
                "generationConfig", generationConfig
            );
            
            String requestBody = mapper.writeValueAsString(requestBodyMap);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiApiUrl + "?key=" + geminiApiKey;

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            JsonNode root = mapper.readTree(response.getBody());
            
            String generatedText = root.get("candidates").get(0)
                    .get("content").get("parts").get(0)
                    .get("text").asText();

            JsonNode quizNode = mapper.readTree(generatedText);
            String title = quizNode.get("title").asText("Quiz from YouTube Video");
            String summary = quizNode.get("summary").asText("No summary generated.");
            
            List<GeneratedQuestionDto> questions = new ArrayList<>();
            JsonNode questionsNode = quizNode.get("questions");
            if (questionsNode != null && questionsNode.isArray()) {
                for (JsonNode qNode : questionsNode) {
                    GeneratedQuestionDto q = mapper.treeToValue(qNode, GeneratedQuestionDto.class);
                    questions.add(q);
                }
            }
            
            return new QuizDetailWrapper(title, summary, questions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate quiz from Gemini: " + e.getMessage(), e);
        }
    }

    @lombok.Getter
    @lombok.Setter
    @lombok.AllArgsConstructor
    private static class QuizDetailWrapper {
        private String title;
        private String summary;
        private List<GeneratedQuestionDto> questions;
    }

}