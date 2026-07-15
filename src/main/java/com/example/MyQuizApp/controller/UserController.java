package com.example.MyQuizApp.controller;

import com.example.MyQuizApp.dto.request.UserRequest;
import com.example.MyQuizApp.dto.response.UserResponse;
import com.example.MyQuizApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public String userProfile(){

        return "Welcome User";

    }


    @PostMapping("/save")
    public UserResponse save(@RequestBody UserRequest userRequest){
        return userService.saveUser(userRequest);
    }

    @GetMapping("/getAllUser")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public Optional<UserResponse> getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable Integer id, @RequestBody UserRequest userRequest){
        return userService.updateUser(id,userRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }

}
