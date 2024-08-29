package com.quiz.management.application.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    QuizService quizService;

    QuizDTO quizDTO;

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
    }

    @Test
    void test_createQuiz() throws Exception {
        when(quizService.createQuiz(any(QuizDTO.class))).thenReturn(quizDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .post("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quizDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        QuizDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuizDTO.class);
        assertEquals(quizDTO.getCategory(),result.getCategory());

        verify(quizService, times(1)).createQuiz(any(QuizDTO.class));
    }

    @Test
    void test_getQuizById() throws Exception {
        when(quizService.getQuizById(anyInt())).thenReturn(quizDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .get("/quizzes/{quizId}",1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        QuizDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuizDTO.class);
        assertEquals(quizDTO.getCategory(),result.getCategory());

        verify(quizService,times(1)).getQuizById(1);
    }

    @Test
    void test_getAllQuizzes() throws Exception {
        List<QuizDTO> quizDTOS = List.of(quizDTO);
        when(quizService.getAllQuizzes()).thenReturn(quizDTOS);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .get("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<QuizDTO> result = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<List<QuizDTO>>() {});
        assertEquals(quizDTOS.size(),result.size());

        verify(quizService,times(1)).getAllQuizzes();
    }

    @Test
    void test_updateQuiz() throws Exception {
        when(quizService.updateQuiz(any(QuizDTO.class))).thenReturn(quizDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .put("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quizDTO))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        QuizDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuizDTO.class);
        assertEquals(quizDTO.getCategory(),result.getCategory());

        verify(quizService,times(1)).updateQuiz(any(QuizDTO.class));
    }

    @Test
    void test_deleteQuizById() throws Exception {
        doNothing().when(quizService).deleteQuizById(anyInt());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/quizzes/{quizId}",1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

        verify(quizService,times(1)).deleteQuizById(1);
    }

    @Test
    void deleteAllQuizzes() throws Exception {
        doNothing().when(quizService).deleteAllQuizzes();

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

        verify(quizService,times(1)).deleteAllQuizzes();
    }

}