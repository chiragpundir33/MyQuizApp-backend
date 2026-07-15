package com.example.MyQuizApp.entity;


import com.example.MyQuizApp.enums.AssignmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Locale;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_assignments")
public class QuizAssignment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id" , nullable = false)
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    @Column(nullable = false)
    private LocalDateTime assignedAt;


    private LocalDateTime dueDate;


    @PrePersist
    public void prePersist() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = AssignmentStatus.ASSIGNED;
        }
    }



}
