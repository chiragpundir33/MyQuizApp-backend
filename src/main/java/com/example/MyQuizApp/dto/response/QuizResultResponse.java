package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultResponse {

    private Integer quizId;

    private Integer totalQuestions;

    private Integer correctAnswers;

    private BigDecimal score;

    private Double percentage;

    private String result;

}
