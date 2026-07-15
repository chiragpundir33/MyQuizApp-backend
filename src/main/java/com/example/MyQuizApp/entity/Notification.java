package com.example.MyQuizApp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-id", nullable = false)
    private User user;


    private String title;

    private String message;

    private boolean isRead = false;


    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist(){

        if(createdAt == null){
            createdAt = LocalDateTime.now();
        }

    }



}
