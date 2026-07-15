package com.example.MyQuizApp.dto.response;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardResponse {

    private Integer rank;

    private String username;

    private BigDecimal highestScore;


}
