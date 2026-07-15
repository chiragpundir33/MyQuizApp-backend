package com.example.MyQuizApp.entity.coding;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingProblem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String title;


    @Column(columnDefinition = "TEXT")
    private String description;


    private String difficulty;


    private String category;


    @Column(columnDefinition = "TEXT")
    private String constraints;


    private LocalDateTime createdAt;

}