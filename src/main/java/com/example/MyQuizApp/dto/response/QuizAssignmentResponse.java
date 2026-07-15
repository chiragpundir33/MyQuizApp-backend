package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAssignmentResponse {

    private Integer id;

    private Integer userId;

    private Integer quizId;

    private String QuizTitle;

    private String status;

    private LocalDateTime assignedAt;

    private LocalDateTime dueDate;
}
