package com.quiz.management.application.repository;

import com.quiz.management.application.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<QuestionEntity, Integer> {
    Optional<List<QuestionEntity>> findByCategory(String category);
}
