package com.example.MyQuizApp.entity;

import com.example.MyQuizApp.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(nullable = false)
    private String name;

    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    private Boolean isActive = true;


}