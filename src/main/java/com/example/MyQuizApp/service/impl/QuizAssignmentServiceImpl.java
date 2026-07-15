package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.dto.request.AssignQuizRequest;
import com.example.MyQuizApp.dto.response.QuizAssignmentResponse;
import com.example.MyQuizApp.entity.Quiz;
import com.example.MyQuizApp.entity.QuizAssignment;
import com.example.MyQuizApp.entity.User;
import com.example.MyQuizApp.enums.AssignmentStatus;
import com.example.MyQuizApp.repository.QuizAssignmentRepository;
import com.example.MyQuizApp.repository.QuizRepository;
import com.example.MyQuizApp.repository.UserRepository;
import com.example.MyQuizApp.service.NotificationService;
import com.example.MyQuizApp.service.QuizAssignmentService;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizAssignmentServiceImpl implements QuizAssignmentService {


    private final QuizAssignmentRepository quizAssignmentRepository;

    private final UserRepository userRepository;

    private final QuizRepository quizRepository;

    private final NotificationService notificationService;


    public QuizAssignmentServiceImpl(
            QuizAssignmentRepository quizAssignmentRepository,
            UserRepository userRepository,
            QuizRepository quizRepository, NotificationService notificationService
    ) {
        this.quizAssignmentRepository = quizAssignmentRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.notificationService = notificationService;
    }



    @Override
    public QuizAssignmentResponse assignQuiz(AssignQuizRequest request) {


        // 1. Find User

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // 2. Find Quiz

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() ->
                        new RuntimeException("Quiz not found")
                );


        // 3. Check duplicate assignment

        if(quizAssignmentRepository
                .existsByUserIdAndQuizId(
                        request.getUserId(),
                        request.getQuizId()
                )) {

            throw new RuntimeException(
                    "Quiz already assigned to this user"
            );
        }



        // 4. Create Assignment

        QuizAssignment assignment = new QuizAssignment();

        assignment.setUser(user);

        assignment.setQuiz(quiz);

        assignment.setDueDate(request.getDueDate());

        assignment.setStatus(
                AssignmentStatus.ASSIGNED
        );



        // 5. Save

        QuizAssignment savedAssignment =
                quizAssignmentRepository.save(assignment);


        notificationService.createNotification(

                user.getId(),

                "New Quiz Assigned",

                quiz.getTitle()
                        + " quiz has been assigned to you."

        );


        // 6. Convert Entity to Response DTO

        QuizAssignmentResponse response =
                new QuizAssignmentResponse();


        response.setId(savedAssignment.getId());

        response.setUserId(user.getId());

        response.setQuizId(quiz.getId());

        response.setQuizTitle(quiz.getTitle());

        response.setStatus(
                savedAssignment.getStatus().name()
        );

        response.setAssignedAt(
                savedAssignment.getAssignedAt()
        );

        response.setDueDate(
                savedAssignment.getDueDate()
        );


        return response;
    }


    @Override
    public List<QuizAssignmentResponse> getAssignmentsByUserId(Integer userId) {
        List<QuizAssignment> assignments = quizAssignmentRepository.findByUserId(userId);
        return assignments.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<QuizAssignmentResponse> getAllAssignments() {
        List<QuizAssignment> assignments = quizAssignmentRepository.findAll();
        return assignments.stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deleteAssignment(Integer id) {
        quizAssignmentRepository.deleteById(id);
    }

    private QuizAssignmentResponse mapToResponse(QuizAssignment assignment) {
        QuizAssignmentResponse response = new QuizAssignmentResponse();
        response.setId(assignment.getId());
        response.setUserId(assignment.getUser().getId());
        response.setQuizId(assignment.getQuiz().getId());
        response.setQuizTitle(assignment.getQuiz().getTitle());
        response.setStatus(assignment.getStatus().name());
        response.setAssignedAt(assignment.getAssignedAt());
        response.setDueDate(assignment.getDueDate());
        return response;
    }
}