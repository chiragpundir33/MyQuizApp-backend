package com.example.MyQuizApp.controller;


import com.example.MyQuizApp.dto.response.NotificationResponse;
import com.example.MyQuizApp.service.NotificationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;


    public NotificationController(
            NotificationService notificationService
    ){

        this.notificationService =
                notificationService;

    }



    @GetMapping("/{userId}")
    public List<NotificationResponse> getNotifications(
            @PathVariable Integer userId
    ){

        return notificationService
                .getUserNotifications(userId);

    }




    @GetMapping("/unread/{userId}")
    public long unreadCount(
            @PathVariable Integer userId
    ){

        return notificationService
                .getUnreadCount(userId);

    }





    @PutMapping("/read/{notificationId}")
    public String markRead(
            @PathVariable Integer notificationId
    ){

        notificationService
                .markAsRead(notificationId);


        return "Notification marked as read";

    }

}