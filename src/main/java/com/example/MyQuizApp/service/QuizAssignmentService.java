package com.example.MyQuizApp.service;

import com.example.MyQuizApp.dto.request.AssignQuizRequest;
import com.example.MyQuizApp.dto.response.QuizAssignmentResponse;
import java.util.List;

public interface QuizAssignmentService {

    QuizAssignmentResponse assignQuiz(AssignQuizRequest request);

    List<QuizAssignmentResponse> getAssignmentsByUserId(Integer userId);

    List<QuizAssignmentResponse> getAllAssignments();

    void deleteAssignment(Integer id);

}
