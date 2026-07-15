package com.example.MyQuizApp.controller;

import com.example.MyQuizApp.dto.request.AssignQuizRequest;
import com.example.MyQuizApp.dto.response.QuizAssignmentResponse;
import com.example.MyQuizApp.service.QuizAssignmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminAssignmentController {


    private final QuizAssignmentService quizAssignmentService;


    public AdminAssignmentController(
            QuizAssignmentService quizAssignmentService
    ) {
        this.quizAssignmentService = quizAssignmentService;
    }



    @PostMapping("/assign-quiz")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuizAssignmentResponse> assignQuiz(
            @RequestBody AssignQuizRequest request
    ) {

        QuizAssignmentResponse response =
                quizAssignmentService.assignQuiz(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }
}