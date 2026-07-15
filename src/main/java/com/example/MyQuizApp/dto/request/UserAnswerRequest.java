package com.example.MyQuizApp.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAnswerRequest {

    private Integer userQuizId;

    private Integer questionId;

    private String selectedAnswer;

}