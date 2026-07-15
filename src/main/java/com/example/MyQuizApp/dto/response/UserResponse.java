package com.example.MyQuizApp.dto.response;


import lombok.*;

import com.example.MyQuizApp.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer id;

    private String name;

    private String email;

    private Role role;

    private Boolean isActive;
}
