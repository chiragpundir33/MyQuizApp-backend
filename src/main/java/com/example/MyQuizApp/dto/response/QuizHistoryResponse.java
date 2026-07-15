package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizHistoryResponse {

    private String quizName;

    private BigDecimal score;

    private Integer totalQuestions;

    private LocalDateTime attemptedAt;

}