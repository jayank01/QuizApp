package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.handler.QuizException;

import java.util.List;

public interface QuizService {
    QuizDTO createQuiz(QuizDTO quizDTO) throws QuizException;
    QuizDTO getQuizById(Integer Id) throws QuizException, JsonProcessingException;
    List<QuizDTO> getAllQuizzes();
    QuizDTO updateQuiz(QuizDTO quizDTO);
    void deleteQuizById(Integer Id);
    void deleteAllQuizzes();
}
