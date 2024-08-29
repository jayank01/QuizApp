package com.quiz.management.application.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

    private Integer id;
    @NotEmpty(message = "Quiz category is mandatory")
    private String category;

    private List<QuestionDTO> questions;
}
