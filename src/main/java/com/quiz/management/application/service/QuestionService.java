package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.entity.QuestionEntity;
import com.quiz.management.application.handler.QuestionException;
import com.quiz.management.application.handler.QuizException;

import java.util.List;

public interface QuestionService {
    QuestionDTO addQuestion(QuestionDTO questionDTO) throws QuizException;
    QuestionDTO getQuestionById(Integer Id) throws QuestionException, JsonProcessingException;
    List<QuestionDTO> getQuestionsByCategory(String category) throws QuestionException;
    QuestionDTO updateQuestion(QuestionDTO questionDTO);
    void deleteQuestionById(Integer Id);
    void deleteQuestionByCategory(String category);
}
