package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.entity.QuestionEntity;
import com.quiz.management.application.entity.QuizEntity;
import com.quiz.management.application.handler.QuestionException;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.repository.QuestionRepository;
import com.quiz.management.application.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    @Override
    public QuestionDTO addQuestion(QuestionDTO questionDTO) throws QuizException {
        QuestionEntity questionEntity = objectMapper.convertValue(questionDTO,QuestionEntity.class);
        QuizEntity quizEntity = quizRepository.findByCategory(questionEntity.getCategory()).orElseThrow(() -> new QuizException("Quiz not found with category: " + questionEntity.getCategory()));
        questionEntity.setQuiz(quizEntity);
        return objectMapper.convertValue(questionRepository.save(questionEntity),QuestionDTO.class);
    }

    @Override
    public QuestionDTO getQuestionById(Integer Id) throws QuestionException, JsonProcessingException {
        Optional<QuestionEntity> questionEntity = redisService.get("Question_No." + Id, QuestionEntity.class);
        if(questionEntity.isPresent()) return objectMapper.convertValue(questionEntity.get(),QuestionDTO.class);
        QuestionEntity question = questionRepository.findById(Id).orElseThrow(() -> new QuestionException("Quiz not found with Id: " + Id));
        redisService.set("Question_No." + Id,question,300L);
        return objectMapper.convertValue(question,QuestionDTO.class);
    }

    @Override
    public List<QuestionDTO> getQuestionsByCategory(String category) throws QuestionException {
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        questionRepository.findByCategory(category).ifPresent(questions -> questions.forEach(question -> questionDTOS.add(objectMapper.convertValue(question,QuestionDTO.class))));
        if(questionDTOS.isEmpty()) throw new QuestionException("Questions not found with category: " + category);
        return questionDTOS;
    }

    @Override
    public QuestionDTO updateQuestion(QuestionDTO questionDTO) {
        QuestionEntity questionEntity = objectMapper.convertValue(questionDTO, QuestionEntity.class);
        Optional<QuestionEntity> question = questionRepository.findById(questionEntity.getId());
        question.get().setDifficulty(questionEntity.getDifficulty());
        question.get().setOptions(questionEntity.getOptions());
        question.get().setCorrectOption(questionEntity.getCorrectOption());
        question.get().setDescription(questionEntity.getDescription());
        return objectMapper.convertValue(questionRepository.save(question.get()), QuestionDTO.class);
    }

    @Override
    public void deleteQuestionById(Integer Id) {
        Optional<QuestionEntity> questionEntity = questionRepository.findById(Id);
        QuizEntity quizEntity = questionEntity.get().getQuiz();
        quizEntity.getQuestions().remove(questionEntity.get());
        questionRepository.deleteById(Id);
    }

    @Override
    public void deleteQuestionByCategory(String category) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        questionRepository.findByCategory(category).ifPresent(questionEntities::addAll);
        Optional<QuizEntity> quizEntity = quizRepository.findByCategory(category);
        quizEntity.get().getQuestions().clear();
        questionRepository.deleteAll(questionEntities);
    }
}
