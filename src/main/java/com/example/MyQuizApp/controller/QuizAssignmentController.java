package com.example.MyQuizApp.controller;

import com.example.MyQuizApp.dto.response.QuizAssignmentResponse;
import com.example.MyQuizApp.service.QuizAssignmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class QuizAssignmentController {

    private final QuizAssignmentService quizAssignmentService;

    public QuizAssignmentController(QuizAssignmentService quizAssignmentService) {
        this.quizAssignmentService = quizAssignmentService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<QuizAssignmentResponse> getAssignmentsByUserId(@PathVariable Integer userId) {
        return quizAssignmentService.getAssignmentsByUserId(userId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuizAssignmentResponse> getAllAssignments() {
        return quizAssignmentService.getAllAssignments();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAssignment(@PathVariable Integer id) {
        quizAssignmentService.deleteAssignment(id);
        return "Quiz assignment revoked successfully";
    }
}
