package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDetailResponse {

    private Integer quizSid;
    private String quizName;
    private List<UserQuestionResponse> userQuestionResponseList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserQuestionResponse {

        private String fullName;
        private String userName;
        private Integer userSid;
        private List<QuestionResponse> questionResponseList;
        private String totalMarks;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionResponse {

        private Integer id;
        private String questionTitle;
        private String option1;
        private String option2;
        private String option3;
        private String option4;
        private String difficultyLevel;
        private String category;
        private String correctAnswer;
        private String selectedAnswer;
    }
}