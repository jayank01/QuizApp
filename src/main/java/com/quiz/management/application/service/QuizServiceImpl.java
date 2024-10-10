package com.quiz.management.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.management.application.dto.QuestionDTO;
import com.quiz.management.application.dto.QuizDTO;
import com.quiz.management.application.entity.QuizEntity;
import com.quiz.management.application.handler.QuizException;
import com.quiz.management.application.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    @Override
    public QuizDTO createQuiz(QuizDTO quizDTO) throws QuizException {
        List<QuestionDTO> questionDTOS = quizDTO.getQuestions();
        if(!questionDTOS.stream().allMatch(question -> question.getCategory().equals(quizDTO.getCategory()))) throw new QuizException("Category of the questions doesn't match with quiz category: " + quizDTO.getCategory());
        QuizEntity quiz = quizRepository.findByCategory(quizDTO.getCategory()).orElse(objectMapper.convertValue(quizDTO,QuizEntity.class));
        QuizEntity quizEntity = objectMapper.convertValue(quizDTO,QuizEntity.class);
        quiz.setQuestions(quizEntity.getQuestions());
        quiz.getQuestions().forEach(question -> question.setQuiz(quiz));
        return objectMapper.convertValue(quizRepository.save(quiz),QuizDTO.class);
    }

    @Override
    public QuizDTO getQuizById(Integer Id) throws QuizException, JsonProcessingException {
        QuizEntity quizEntity = redisService.get("Quiz_No." + Id, QuizEntity.class);
        if(quizEntity != null) return objectMapper.convertValue(quizEntity,QuizDTO.class);
        QuizEntity quiz = quizRepository.findById(Id).orElseThrow(() -> new QuizException("Quiz not found with id: " + Id));
        redisService.set("Quiz_No." + Id,quiz,300L);
        return objectMapper.convertValue(quiz, QuizDTO.class);
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        List<QuizDTO> quizDTOList = new ArrayList<>();
        quizRepository.findAll().forEach(quizEntity -> quizDTOList.add(objectMapper.convertValue(quizEntity, QuizDTO.class)));
        return quizDTOList;
    }

    @Override
    public QuizDTO updateQuiz(QuizDTO quizDTO) {
        QuizEntity quizEntity = objectMapper.convertValue(quizDTO,QuizEntity.class);
        quizEntity.getQuestions().forEach(question -> question.setQuiz(quizEntity));
        Optional<QuizEntity> quiz = quizRepository.findById(quizEntity.getId());
        quiz.get().setCategory(quizEntity.getCategory());
        quiz.get().setQuestions(quizEntity.getQuestions());
        return objectMapper.convertValue(quizRepository.save(quiz.get()),QuizDTO.class);
    }

    @Override
    public void deleteQuizById(Integer Id) {
        quizRepository.deleteById(Id);
    }

    @Override
    public void deleteAllQuizzes() {
        quizRepository.deleteAll();
    }
}
