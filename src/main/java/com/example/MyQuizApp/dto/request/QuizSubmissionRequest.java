package com.example.MyQuizApp.dto.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmissionRequest {


    private Integer userQuizId;

    private List<SubmittedAnswerRequest> answers;
}
