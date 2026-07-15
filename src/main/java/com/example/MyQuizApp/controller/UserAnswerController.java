package com.example.MyQuizApp.controller;

import com.example.MyQuizApp.dto.request.UserAnswerRequest;
import com.example.MyQuizApp.dto.response.UserAnswerResponse;
import com.example.MyQuizApp.repository.UserAnswerRepository;
import com.example.MyQuizApp.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userAnswer")
public class UserAnswerController {

    @Autowired
    private UserAnswerService userAnswerService;

    @PostMapping("/save")
    public UserAnswerResponse saveUserAnswer(@RequestBody UserAnswerRequest userAnswerRequest) {
        return userAnswerService.saveUserAnswer(userAnswerRequest);
    }

    @GetMapping("/getAll")
    public List<UserAnswerResponse> getAllUserAnswer(){
        return userAnswerService.getAllUserAnswers();
    }


    @GetMapping("/getById/{id}")
    public UserAnswerResponse getUserAnswerById(@PathVariable Integer id){
        return userAnswerService.getUserAnswerById(id);
    }

    @PutMapping("/update/{id}")
    public UserAnswerResponse updateUserAnswer(@PathVariable Integer id , @RequestBody UserAnswerRequest userAnswerRequest){
        return userAnswerService.updateUserAnswer(id,userAnswerRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserAnswerById(@PathVariable Integer id){
        userAnswerService.deleteUserAnswer(id);
    }


}
