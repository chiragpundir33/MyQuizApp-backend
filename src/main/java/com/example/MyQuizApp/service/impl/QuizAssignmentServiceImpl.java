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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuizAssignmentServiceImpl implements QuizAssignmentService {

    private final QuizAssignmentRepository quizAssignmentRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final NotificationService notificationService;

    public QuizAssignmentServiceImpl(
            QuizAssignmentRepository quizAssignmentRepository,
            UserRepository userRepository,
            QuizRepository quizRepository,
            NotificationService notificationService
    ) {
        this.quizAssignmentRepository = quizAssignmentRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.notificationService = notificationService;
    }

    @Override
    public QuizAssignmentResponse assignQuiz(AssignQuizRequest request) {

        try {

            log.info("Assign Quiz Request: {}", request);

            // Find User
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

            log.info("User Found: {}", user.getId());

            // Find Quiz
            Quiz quiz = quizRepository.findById(request.getQuizId())
                    .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + request.getQuizId()));

            log.info("Quiz Found: {}", quiz.getId());

            // Duplicate Check
            if (quizAssignmentRepository.existsByUserIdAndQuizId(
                    request.getUserId(),
                    request.getQuizId())) {

                throw new RuntimeException("Quiz is already assigned to this user.");
            }

            // Create Assignment
            QuizAssignment assignment = new QuizAssignment();
            assignment.setUser(user);
            assignment.setQuiz(quiz);
            assignment.setDueDate(request.getDueDate());
            assignment.setStatus(AssignmentStatus.ASSIGNED);

            QuizAssignment savedAssignment = quizAssignmentRepository.save(assignment);

            log.info("Assignment Saved Successfully. ID: {}", savedAssignment.getId());

            // Create Notification
            notificationService.createNotification(
                    user.getId(),
                    "New Quiz Assigned",
                    quiz.getTitle() + " quiz has been assigned to you."
            );

            log.info("Notification Created Successfully");

            QuizAssignmentResponse response = new QuizAssignmentResponse();
            response.setId(savedAssignment.getId());
            response.setUserId(user.getId());
            response.setQuizId(quiz.getId());
            response.setQuizTitle(quiz.getTitle());
            response.setStatus(savedAssignment.getStatus().name());
            response.setAssignedAt(savedAssignment.getAssignedAt());
            response.setDueDate(savedAssignment.getDueDate());

            return response;

        } catch (Exception e) {

            log.error("Error while assigning quiz", e);

            throw new RuntimeException("Failed to assign quiz: " + e.getMessage(), e);
        }
    }

    @Override
    public List<QuizAssignmentResponse> getAssignmentsByUserId(Integer userId) {
        return quizAssignmentRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<QuizAssignmentResponse> getAllAssignments() {
        return quizAssignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
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