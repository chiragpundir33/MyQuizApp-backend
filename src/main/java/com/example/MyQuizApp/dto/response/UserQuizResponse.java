package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuizResponse {

    private Integer id;

    private Integer userSid;

    private Integer quizSid;

    private BigDecimal score;

    private LocalDateTime attemptedAt;

}