package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.entity.QuestionEntity;
import com.quiz.management.application.entity.QuizEntity;
import com.quiz.management.application.handler.QuestionException;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.repository.QuestionRepository;
import com.quiz.management.application.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    QuizRepository quizRepository;

    @Mock
    QuestionRepository questionRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    QuestionServiceImpl questionService;

    QuestionDTO questionDTO;
    QuestionEntity questionEntity;
    QuizEntity quizEntity;

    @BeforeEach
    void setUp(){
        quizEntity = QuizEntity.builder()
                .category("Science")
                .questions(new ArrayList<>())
                .build();

        quizEntity.getQuestions().add(QuestionEntity.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question 1")
                .difficulty("Easy")
                .correctOption(1)
                .build());

        questionEntity = QuestionEntity.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question 2")
                .difficulty("Easy")
                .correctOption(2)
                .build();

        questionDTO = QuestionDTO.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question 2")
                .difficulty("Easy")
                .correctOption(2)
                .build();
    }

    @Test
    void test_addQuestion() throws QuizException {
        when(objectMapper.convertValue(questionDTO, QuestionEntity.class)).thenReturn(questionEntity);
        when(quizRepository.findByCategory(anyString())).thenReturn(Optional.of(quizEntity));
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionEntity);
        when(objectMapper.convertValue(questionEntity, QuestionDTO.class)).thenReturn(questionDTO);

        QuestionDTO result = questionService.addQuestion(questionDTO);
        assertEquals(questionDTO,result);

        verify(objectMapper,times(1)).convertValue(questionDTO, QuestionEntity.class);
        verify(quizRepository,times(1)).findByCategory(questionEntity.getCategory());
        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
        verify(objectMapper,times(1)).convertValue(questionEntity, QuestionDTO.class);
    }

    @Test
    void test_getQuestionById() throws QuestionException, JsonProcessingException {
        when(questionRepository.findById(anyInt())).thenReturn(Optional.of(questionEntity));
        when(objectMapper.convertValue(questionEntity, QuestionDTO.class)).thenReturn(questionDTO);

        QuestionDTO result = questionService.getQuestionById(1);
        assertNotNull(result);
        assertEquals(questionDTO,result);

        verify(questionRepository,atLeastOnce()).findById(1);
        verify(objectMapper,times(1)).convertValue(questionEntity, QuestionDTO.class);
    }

    @Test
    void test1_getQuestionsByCategory() throws QuestionException {
        List<QuestionEntity> questionEntities = List.of(questionEntity);
        when(questionRepository.findByCategory(anyString())).thenReturn(Optional.of(questionEntities));
        when(objectMapper.convertValue(questionEntity, QuestionDTO.class)).thenReturn(questionDTO);

        List<QuestionDTO> result =  questionService.getQuestionsByCategory(questionDTO.getCategory());
        List<QuestionDTO> expected = List.of(questionDTO);
        assertEquals(expected,result);

        verify(questionRepository,atLeastOnce()).findByCategory(questionDTO.getCategory());
        verify(objectMapper,times(1)).convertValue(questionEntity, QuestionDTO.class);
    }

    @Test
    void test2_getQuestionsByCategory() {
        when(questionRepository.findByCategory(anyString())).thenReturn(Optional.of(new ArrayList<>()));

        assertThrows(QuestionException.class,() -> questionService.getQuestionsByCategory(questionDTO.getCategory()));

        verify(questionRepository,atLeastOnce()).findByCategory(questionDTO.getCategory());
    }

    @Test
    void test_updateQuestion() {
        questionDTO.setId(1);
        questionEntity.setId(1);
        when(objectMapper.convertValue(questionDTO, QuestionEntity.class)).thenReturn(questionEntity);
        when(questionRepository.findById(anyInt())).thenReturn(Optional.of(questionEntity));
        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(questionEntity);
        when(objectMapper.convertValue(questionEntity, QuestionDTO.class)).thenReturn(questionDTO);

        QuestionDTO result = questionService.updateQuestion(questionDTO);
        assertEquals(questionDTO,result);

        verify(objectMapper,times(1)).convertValue(questionDTO, QuestionEntity.class);
        verify(questionRepository,atLeastOnce()).findById(1);
        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
        verify(objectMapper,times(1)).convertValue(questionEntity, QuestionDTO.class);

    }

    @Test
    void test_deleteQuestionById(){
        questionEntity.setQuiz(quizEntity);
        when(questionRepository.findById(anyInt())).thenReturn(Optional.of(questionEntity));
        doNothing().when(questionRepository).deleteById(anyInt());

        questionService.deleteQuestionById(1);

        verify(questionRepository,times(1)).findById(1);
        verify(questionRepository, times(1)).deleteById(1);
    }

    @Test
    void test_deleteQuestionByCategory(){
        List<QuestionEntity> questionEntities = List.of(questionEntity);
        when(questionRepository.findByCategory(anyString())).thenReturn(Optional.of(questionEntities));
        when(quizRepository.findByCategory(anyString())).thenReturn(Optional.of(quizEntity));
        doNothing().when(questionRepository).deleteAll(questionEntities);

        questionService.deleteQuestionByCategory(questionEntity.getCategory());

        verify(questionRepository,times(1)).findByCategory(questionEntity.getCategory());
        verify(quizRepository,times(1)).findByCategory(questionEntity.getCategory());
        verify(questionRepository, times(1)).deleteAll(questionEntities);
    }
}