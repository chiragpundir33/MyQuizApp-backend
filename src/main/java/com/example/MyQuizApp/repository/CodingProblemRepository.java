package com.example.MyQuizApp.repository;


import com.example.MyQuizApp.entity.coding.CodingProblem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CodingProblemRepository
        extends JpaRepository<CodingProblem,Integer> {

}