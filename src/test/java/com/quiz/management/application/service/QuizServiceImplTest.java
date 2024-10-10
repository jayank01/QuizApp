package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.entity.QuestionEntity;
import com.quiz.management.application.entity.QuizEntity;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceImplTest {
    @Mock
    QuizRepository quizRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    QuizServiceImpl quizService;

    QuizDTO quizDTO;
    QuizEntity quizEntity;

    @BeforeEach
    void setUp(){
        quizDTO = QuizDTO.builder()
                .category("Science")
                .build();

        quizDTO.setQuestions(List.of(QuestionDTO.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question")
                .difficulty("Easy")
                .correctOption(3)
                .build()));

        quizEntity = QuizEntity.builder()
                .category("Science")
                .build();

        quizEntity.setQuestions(List.of(QuestionEntity.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question")
                .difficulty("Easy")
                .correctOption(2)
                .build()));
    }

    @Test
    void test_createQuiz() throws QuizException {
        when(quizRepository.findByCategory(anyString())).thenReturn(Optional.of(quizEntity));
        when(objectMapper.convertValue(quizDTO, QuizEntity.class)).thenReturn(quizEntity);
        when(quizRepository.save(any(QuizEntity.class))).thenReturn(quizEntity);
        when(objectMapper.convertValue(quizEntity, QuizDTO.class)).thenReturn(quizDTO);

        QuizDTO result = quizService.createQuiz(quizDTO);
        assertNotNull(result);
        assertEquals(result.getCategory(),quizDTO.getCategory());
        assertEquals(1, result.getQuestions().size());

        verify(quizRepository,atLeastOnce()).findByCategory(quizDTO.getCategory());
        verify(objectMapper,times(2)).convertValue(quizDTO, QuizEntity.class);
        verify(quizRepository, times(1)).save(any(QuizEntity.class));
        verify(objectMapper,times(1)).convertValue(quizEntity, QuizDTO.class);
    }

    @Test
    void test_getQuizById() throws QuizException, JsonProcessingException {
        when(quizRepository.findById(anyInt())).thenReturn(Optional.of(quizEntity));
        when(objectMapper.convertValue(quizEntity, QuizDTO.class)).thenReturn(quizDTO);

        QuizDTO result = quizService.getQuizById(1);
        assertNotNull(result);
        assertEquals(result.getCategory(),quizDTO.getCategory());
        assertEquals(1, result.getQuestions().size());

        verify(quizRepository,atLeastOnce()).findById(anyInt());
        verify(objectMapper,times(1)).convertValue(quizEntity, QuizDTO.class);
    }

    @Test
    void test_getAllQuizzes(){
        List<QuizEntity> quizEntities = List.of(quizEntity);
        when(quizRepository.findAll()).thenReturn(quizEntities);
        when(objectMapper.convertValue(quizEntity, QuizDTO.class)).thenReturn(quizDTO);

        List<QuizDTO> expected = List.of(quizDTO);
        List<QuizDTO> result = quizService.getAllQuizzes();
        assertEquals(expected,result);

        verify(quizRepository,atLeastOnce()).findAll();
        verify(objectMapper,times(1)).convertValue(quizEntity, QuizDTO.class);
    }

    @Test
    void test_updateQuiz(){
        quizDTO.setId(1);
        quizEntity.setId(1);
        when(objectMapper.convertValue(quizDTO, QuizEntity.class)).thenReturn(quizEntity);
        when(quizRepository.findById(anyInt())).thenReturn(Optional.of(quizEntity));
        when(quizRepository.save(any(QuizEntity.class))).thenReturn(quizEntity);
        when(objectMapper.convertValue(quizEntity, QuizDTO.class)).thenReturn(quizDTO);

        QuizDTO result = quizService.updateQuiz(quizDTO);
        assertEquals(quizDTO,result);

        verify(objectMapper,times(1)).convertValue(quizEntity, QuizDTO.class);
        verify(quizRepository,atLeastOnce()).findById(quizEntity.getId());
        verify(quizRepository, times(1)).save(any(QuizEntity.class));
        verify(objectMapper,times(1)).convertValue(quizDTO, QuizEntity.class);
    }

    @Test
    void test_deleteQuizById(){
        doNothing().when(quizRepository).deleteById(anyInt());

        quizService.deleteQuizById(1);

        verify(quizRepository, times(1)).deleteById(1);
    }

    @Test
    void test_deleteAllQuizzes(){
        doNothing().when(quizRepository).deleteAll();

        quizService.deleteAllQuizzes();

        verify(quizRepository, times(1)).deleteAll();

    }

}
