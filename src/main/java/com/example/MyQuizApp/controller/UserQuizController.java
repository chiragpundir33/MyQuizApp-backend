package com.example.MyQuizApp.controller;


import com.example.MyQuizApp.dto.request.UserQuizRequest;
import com.example.MyQuizApp.dto.response.UserQuizResponse;
import com.example.MyQuizApp.service.UserQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userQuiz")
public class UserQuizController {


    @Autowired
    private UserQuizService userQuizService;

    @PostMapping("/save")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public UserQuizResponse saveUserQuiz(@RequestBody UserQuizRequest userQuizRequest){
        return userQuizService.saveUserQuiz(userQuizRequest);
    }


    @GetMapping("/getAll")
    public List<UserQuizResponse> getAllUserQuizzes(){
        return userQuizService.getAllUserQuizzes();
    }


   @GetMapping("/getById/{id}")
    public UserQuizResponse getUserQuizById(@PathVariable Integer id){
        return userQuizService.getUserQuizById(id);
   }


   @PutMapping("/update/{id}")
    public UserQuizResponse updateUserQuiz(@PathVariable Integer id, @RequestBody UserQuizRequest userQuizRequest){
        return userQuizService.updateUserQuiz(id,userQuizRequest);
   }

   @DeleteMapping("/delete/{id}")
    public void deleteUserQuizById(@PathVariable Integer id){
        userQuizService.deleteUserQuiz(id);
   }




}
