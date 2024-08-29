package com.quiz.management.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;
    @Column(name = "category")
    private String category;
    @Column(name = "description", unique = true)
    private String description;
    private List<String> options;
    @Column(name = "correct_option")
    private Integer correctOption;
    @Column(name = "difficulty")
    private String difficulty;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    @JsonIgnore
    private QuizEntity quiz;
}
