package com.example.MyQuizApp.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequest {

    private String title;

    private List<Integer> questionIds;

}