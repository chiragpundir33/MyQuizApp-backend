package com.example.MyQuizApp.controller;


import com.example.MyQuizApp.dto.request.QuestionRequest;
import com.example.MyQuizApp.dto.response.QuestionResponse;
import com.example.MyQuizApp.service.QuestionService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {


    private final QuestionService questionService;


    // ADMIN ONLY
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse saveQuestion(
            @RequestBody QuestionRequest questionRequest
    ) {

        return questionService.saveQuestion(questionRequest);
    }


    // USER + ADMIN
    @GetMapping("/getAll")
    public List<QuestionResponse> getAllQuestions() {

        return questionService.getAllQuestions();
    }


    // USER + ADMIN
    @GetMapping("/getById/{id}")
    public Optional<QuestionResponse> getQuestionById(
            @PathVariable Integer id
    ) {

        return questionService.getQuestionById(id);
    }


    // ADMIN ONLY
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse updateQuestion(
            @PathVariable Integer id,
            @RequestBody QuestionRequest questionRequest
    ) {

        return questionService.updateQuestion(id, questionRequest);
    }


    // ADMIN ONLY
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteQuestion(
            @PathVariable Integer id
    ) {

        questionService.deleteQuestion(id);
    }

}