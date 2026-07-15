package com.example.MyQuizApp.coding.dto.response;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingProblemResponse {


    private Integer id;

    private String title;

    private String difficulty;

    private String category;

}