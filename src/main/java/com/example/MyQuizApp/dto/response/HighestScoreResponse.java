package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighestScoreResponse {

    private Integer userId;

    private BigDecimal highestScore;
}
