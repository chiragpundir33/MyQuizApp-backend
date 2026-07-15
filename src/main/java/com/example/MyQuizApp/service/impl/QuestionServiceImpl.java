package com.example.MyQuizApp.service.impl;

import com.example.MyQuizApp.entity.Question;
import com.example.MyQuizApp.dto.request.QuestionRequest;
import com.example.MyQuizApp.dto.response.QuestionResponse;
import com.example.MyQuizApp.repository.QuestionRepository;
import com.example.MyQuizApp.service.QuestionService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Builder
public class QuestionServiceImpl implements QuestionService {


    private final QuestionRepository questionRepository;


    @Override
    public QuestionResponse saveQuestion(QuestionRequest questionRequest) {

        Question question = Question.builder()
                .questionTitle(questionRequest.getQuestionTitle())
                .option1(questionRequest.getOption1())
                .option2(questionRequest.getOption2())
                .option3(questionRequest.getOption3())
                .option4(questionRequest.getOption4())
                .rightAnswer(questionRequest.getRightAnswer())
                .difficultyLevel(questionRequest.getDifficultyLevel())
                .category(questionRequest.getCategory())
                .build();

        Question savedQuestion = questionRepository.save(question);

        return QuestionResponse.builder()
                .id(savedQuestion.getId())
                .questionTitle(savedQuestion.getQuestionTitle())
                .option1(savedQuestion.getOption1())
                .option2(savedQuestion.getOption2())
                .option3(savedQuestion.getOption3())
                .option4(savedQuestion.getOption4())
                .difficultyLevel(savedQuestion.getDifficultyLevel())
                .category(savedQuestion.getCategory())
                .build();


    }

    @Override
    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions= questionRepository.findAll();

      return questions.stream()
              .map(question -> QuestionResponse.builder()
                      .id(question.getId())
                      .questionTitle(question.getQuestionTitle())
                      .option1(question.getOption1())
                      .option2(question.getOption2())
                      .option3(question.getOption3())
                      .option4(question.getOption4())
                      .difficultyLevel(question.getDifficultyLevel())
                      .category(question.getCategory())
                      .build()
              ).toList();

    }

    @Override
    public Optional<QuestionResponse> getQuestionById(Integer id) {
        Optional<Question> question = questionRepository.findById(id);


       return Optional.ofNullable(QuestionResponse.builder()
               .id(question.get().getId())
               .questionTitle(question.get().getQuestionTitle())
               .option1(question.get().getOption1())
               .option2(question.get().getOption2())
               .option3(question.get().getOption3())
               .option4(question.get().getOption4())
               .difficultyLevel(question.get().getDifficultyLevel())
               .category(question.get().getCategory())
               .build());

    }



    @Override
    public QuestionResponse updateQuestion(Integer id, QuestionRequest questionRequest) {

        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Question not found"));

        existingQuestion.setQuestionTitle(questionRequest.getQuestionTitle());
        existingQuestion.setOption1(questionRequest.getOption1());
        existingQuestion.setOption2(questionRequest.getOption2());
        existingQuestion.setOption3(questionRequest.getOption3());
        existingQuestion.setOption4(questionRequest.getOption4());
        existingQuestion.setRightAnswer(questionRequest.getRightAnswer());
        existingQuestion.setDifficultyLevel(questionRequest.getDifficultyLevel());
        existingQuestion.setCategory(questionRequest.getCategory());

        questionRepository.save(existingQuestion);

        return  QuestionResponse.builder()
                .id(existingQuestion.getId())
                .questionTitle(existingQuestion.getQuestionTitle())
                .option1(existingQuestion.getOption1())
                .option2(existingQuestion.getOption2())
                .option3(existingQuestion.getOption3())
                .option4(existingQuestion.getOption4())
                .difficultyLevel(existingQuestion.getDifficultyLevel())
                .category(existingQuestion.getCategory())
                .build();


    }



    @Override
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }






}
