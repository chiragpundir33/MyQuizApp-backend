package com.example.MyQuizApp.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerResponse {

    private Integer id;

    private Integer userQuizId;

    private Integer questionId;

    private String selectedAnswer;

    private Boolean isCorrect;

}