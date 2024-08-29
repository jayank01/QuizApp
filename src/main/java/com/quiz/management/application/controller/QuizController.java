package com.quiz.management.application.controller;

import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quizzes")
@Slf4j
@RequiredArgsConstructor
public class QuizController  {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO quizDTO) throws QuizException {
        log.info("Creating quiz with category: {}", quizDTO.getCategory());
        return new ResponseEntity<>(quizService.createQuiz(quizDTO),HttpStatus.CREATED);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Integer quizId) throws QuizException {
        return new ResponseEntity<>(quizService.getQuizById(quizId),HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        return new ResponseEntity<>(quizService.getAllQuizzes(),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<QuizDTO> updateQuiz(@Valid @RequestBody QuizDTO quizDTO) {
        return new ResponseEntity<>(quizService.updateQuiz(quizDTO),HttpStatus.OK);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuizById(@PathVariable Integer quizId) {
        quizService.deleteQuizById(quizId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllQuizzes() {
        quizService.deleteAllQuizzes();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
