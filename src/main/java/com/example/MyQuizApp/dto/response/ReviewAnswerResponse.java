package com.example.MyQuizApp.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAnswerResponse {

    private Integer questionId;

    private String questionTitle;

    private String selectedAnswer;

    private String correctAnswer;

    private Boolean isCorrect;

}
