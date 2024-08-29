package com.quiz.management.application.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.service.QuestionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    QuestionService questionService;

    QuestionDTO questionDTO;

    @BeforeEach
    void setUp(){
        questionDTO = QuestionDTO.builder()
                .category("Science")
                .options(List.of("1", "2", "3", "4"))
                .description("Test question 2")
                .difficulty("Easy")
                .correctOption(2)
                .build();
    }

    @Test
    void test_addQuestion() throws Exception {
        when(questionService.addQuestion(any(QuestionDTO.class))).thenReturn(questionDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionDTO))
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        QuestionDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuestionDTO.class);
        assertEquals(questionDTO.getCategory(),result.getCategory());
        assertEquals(questionDTO.getDescription(),result.getDescription());

        verify(questionService, times(1)).addQuestion(any(QuestionDTO.class));
    }

    @Test
    void test_getQuestionById() throws Exception {
        when(questionService.getQuestionById(anyInt())).thenReturn(questionDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .get("/questions/Id/{questionId}",1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        QuestionDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuestionDTO.class);
        assertEquals(questionDTO.getDifficulty(),result.getDifficulty());

        verify(questionService,times(1)).getQuestionById(1);
    }

    @Test
    void test_getQuestionsByCategory() throws Exception {
        List<QuestionDTO> questionDTOS = List.of(questionDTO);
        when(questionService.getQuestionsByCategory(anyString())).thenReturn(questionDTOS);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .get("/questions/category/{category}","Science")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        List<QuizDTO> result = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(questionDTOS.size(),result.size());

        verify(questionService,times(1)).getQuestionsByCategory("Science");
    }

    @Test
    void test_updateQuestion() throws Exception {
        when(questionService.updateQuestion(any(QuestionDTO.class))).thenReturn(questionDTO);

        MvcResult res = mockMvc.perform(MockMvcRequestBuilders
                .put("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionDTO))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        QuestionDTO result = objectMapper.readValue(res.getResponse().getContentAsString(),QuestionDTO.class);
        assertEquals(questionDTO.getCorrectOption(),result.getCorrectOption());

        verify(questionService,times(1)).updateQuestion(any(QuestionDTO.class));
    }

    @Test
    void test_deleteQuestionById() throws Exception {
        doNothing().when(questionService).deleteQuestionById(anyInt());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/questions/Id/{questionId}",1)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

        verify(questionService,times(1)).deleteQuestionById(1);
    }

    @Test
    void test_deleteQuestionByCategory() throws Exception {
        doNothing().when(questionService).deleteQuestionByCategory(anyString());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/questions/category/{category}",questionDTO.getCategory())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andReturn();

        verify(questionService,times(1)).deleteQuestionByCategory(questionDTO.getCategory());
    }
}