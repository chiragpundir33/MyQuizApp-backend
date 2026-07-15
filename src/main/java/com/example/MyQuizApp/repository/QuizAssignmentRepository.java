package com.example.MyQuizApp.repository;

import com.example.MyQuizApp.entity.QuizAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuizAssignmentRepository extends JpaRepository<QuizAssignment, Integer> {

    List<QuizAssignment> findByUserId(Integer userId);

    List<QuizAssignment> findByQuizId(Integer quizId);

    Optional<QuizAssignment> findByUserIdAndQuizId(Integer userId, Integer quizId);

    boolean existsByUserIdAndQuizId(Integer userId, Integer quizId);
}
