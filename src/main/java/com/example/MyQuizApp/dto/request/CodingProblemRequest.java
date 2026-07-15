package com.example.MyQuizApp.coding.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingProblemRequest {


    private String title;

    private String description;

    private String difficulty;

    private String category;

    private String constraints;

}