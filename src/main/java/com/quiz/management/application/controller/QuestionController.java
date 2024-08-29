package com.quiz.management.application.controller;

import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.handler.QuestionException;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionDTO> addQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws QuizException {
        return new ResponseEntity<>(questionService.addQuestion(questionDTO),HttpStatus.CREATED);
    }

    @GetMapping("/Id/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Integer questionId) throws QuestionException {
        return new ResponseEntity<>(questionService.getQuestionById(questionId),HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCategory(@PathVariable String category) throws QuestionException {
        return new ResponseEntity<>(questionService.getQuestionsByCategory(category),HttpStatus.OK);
    }

    @PutMapping
    public QuestionDTO updateQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        return questionService.updateQuestion(questionDTO);
    }

    @DeleteMapping("/Id/{questionId}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Integer questionId) {
        questionService.deleteQuestionById(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/category/{category}")
    public ResponseEntity<Void> deleteQuestionByCategory(@PathVariable String category) {
        questionService.deleteQuestionByCategory(category);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
