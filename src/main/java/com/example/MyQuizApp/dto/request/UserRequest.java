package com.example.MyQuizApp.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private Integer id;

    private String name;

    private Boolean isActive;
}
