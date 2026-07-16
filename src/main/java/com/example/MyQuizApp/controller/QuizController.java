package com.example.MyQuizApp.controller;


import com.example.MyQuizApp.dto.request.QuizRequest;
import com.example.MyQuizApp.dto.request.QuizSubmissionRequest;
import com.example.MyQuizApp.dto.request.VideoUrlRequest;
import com.example.MyQuizApp.dto.response.*;
import com.example.MyQuizApp.service.QuizService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {


    private final QuizService quizService;



    // ADMIN ONLY
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse saveQuiz(
            @RequestBody QuizRequest quizRequest
    ){

        return quizService.saveQuiz(quizRequest);
    }



    // USER + ADMIN
    @GetMapping("/getAll")
    public List<QuizResponse> getAllQuizzes(){

        return quizService.getAllQuizzes();
    }




    // USER + ADMIN
    @GetMapping("/getById/{id}")
    public Optional<QuizResponse> getById(
            @PathVariable Integer id
    ){

        return quizService.getQuizById(id);
    }




    // ADMIN ONLY
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse updateQuiz(
            @PathVariable Integer id,
            @RequestBody QuizRequest quizRequest
    ){

        return quizService.updateQuiz(id, quizRequest);
    }




    // ADMIN ONLY
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteQuiz(
            @PathVariable Integer id
    ){

        quizService.deleteQuiz(id);
    }





    // USER + ADMIN
    @GetMapping("/{quizId}/questions")
    public List<QuizQuestionResponse> getQuizQuestions(
            @PathVariable Integer quizId
    ){

        return quizService.getQuizQuestions(quizId);
    }





    // USER ONLY
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    public QuizResultResponse submitQuiz(
            @RequestBody QuizSubmissionRequest request
    ){

        return quizService.submitQuiz(request);
    }





    // USER ONLY
    @GetMapping("/review/{userQuizId}")
    @PreAuthorize("hasRole('USER')")
    public List<ReviewAnswerResponse> reviewQuiz(
            @PathVariable Integer userQuizId
    ){

        return quizService.reviewQuiz(userQuizId);
    }





    // USER ONLY
    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<QuizHistoryResponse>> getHistory(
            @PathVariable Integer userId
    ){

        return ResponseEntity.ok(
                quizService.getQuizHistory(userId)
        );
    }





    // USER ONLY
    @GetMapping("/highest-score/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HighestScoreResponse> getHighestScore(
            @PathVariable Integer userId
    ){

        return ResponseEntity.ok(
                quizService.getHighestScore(userId)
        );
    }





    // USER + ADMIN
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardResponse>> leaderboard() {

        return ResponseEntity.ok(
                quizService.getLeaderboard()
        );
    }
    //getAll Quiz details and there users and there correct answer nd


    // ADMIN
    @GetMapping("/getAllUserDetails")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuizDetailResponse> QuizDetails(){
        return quizService.QuizDetails();
    }


    // USER + ADMIN
    @PostMapping("/generate-from-video")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public QuizResponse generateQuizFromVideo(
            @RequestBody VideoUrlRequest videoUrlRequest
    ){
        return quizService.generateQuizFromVideo(videoUrlRequest.getVideoUrl());
    }

}