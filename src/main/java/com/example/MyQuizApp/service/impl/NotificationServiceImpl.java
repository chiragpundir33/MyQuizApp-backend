package com.example.MyQuizApp.service.impl;


import com.example.MyQuizApp.dto.response.NotificationResponse;
import com.example.MyQuizApp.entity.Notification;
import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.repository.NotificationRepository;
import com.example.MyQuizApp.repository.UserRepository;
import com.example.MyQuizApp.service.NotificationService;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NotificationServiceImpl
        implements NotificationService {


    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;



    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ){

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;

    }



    @Override
    public void createNotification(
            Integer userId,
            String title,
            String message
    ){


        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );


        Notification notification = new Notification();


        notification.setUser(user);

        notification.setTitle(title);

        notification.setMessage(message);


        notificationRepository.save(notification);

    }

    @Override
    public List<NotificationResponse> getUserNotifications(Integer userId) {


        List<Notification> notifications =
                notificationRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);


        return notifications.stream()
                .map(notification -> {


                    NotificationResponse response =
                            new NotificationResponse();


                    response.setId(notification.getId());

                    response.setTitle(
                            notification.getTitle()
                    );

                    response.setMessage(
                            notification.getMessage()
                    );

                    response.setRead(
                            notification.isRead()
                    );

                    response.setCreatedAt(
                            notification.getCreatedAt()
                    );


                    return response;


                })
                .toList();
    }




    @Override
    public long getUnreadCount(Integer userId) {


        return notificationRepository
                .countByUserIdAndIsReadFalse(userId);

    }

    @Override
    public void markAsRead(Integer notificationId) {


        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Notification not found"
                                )
                        );


        notification.setRead(true);


        notificationRepository.save(notification);

    }


}