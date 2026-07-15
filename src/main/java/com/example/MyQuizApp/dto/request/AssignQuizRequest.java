package com.example.MyQuizApp.dto.request;

import lombok.*;

import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignQuizRequest {

    private Integer userId;

    private Integer quizId;

    private LocalDateTime dueDate;


}
