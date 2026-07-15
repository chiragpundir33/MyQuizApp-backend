package com.example.MyQuizApp.repository;



import com.example.MyQuizApp.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {

    List<UserAnswer> findByUserQuizId(Integer userQuizId);

}
