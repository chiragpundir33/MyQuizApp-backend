package com.example.MyQuizApp.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuizRequest {

    private Integer userSid;

    private Integer quizSid;

}