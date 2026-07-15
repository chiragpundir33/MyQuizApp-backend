package com.example.MyQuizApp.repository;


import com.example.MyQuizApp.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz, Integer> {


    List<UserQuiz> findByUserIdOrderByAttemptedAtDesc(Integer userId);

    @Query("""
       SELECT MAX(uq.score)
       FROM UserQuiz uq
       WHERE uq.user.id = :userId
       """)
    BigDecimal findHighestScoreByUserId(Integer userId);

    @Query("""
       SELECT uq.user.id, 
              uq.user.name,
              MAX(uq.score)
       FROM UserQuiz uq
       GROUP BY uq.user.id, uq.user.name
       ORDER BY MAX(uq.score) DESC
       """)
    List<Object[]> findLeaderboard();


}
