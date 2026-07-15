package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void createNotification(
            Integer userId,
            String title,
            String message
    );

    List<NotificationResponse> getUserNotifications(
            Integer userId
    );

    long getUnreadCount(
            Integer userId
    );

    void markAsRead(
            Integer notificationId
    );

}
