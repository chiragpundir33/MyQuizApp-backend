package com.example.MyQuizApp.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmittedAnswerRequest {

    private Integer questionId;

    private String selectedAnswer;
}
