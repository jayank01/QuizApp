package com.quiz.management.application.repository;

import com.quiz.management.application.entity.QuizEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuizRepository extends CrudRepository<QuizEntity, Integer>{
    Optional<QuizEntity> findByCategory(String Category);
}
