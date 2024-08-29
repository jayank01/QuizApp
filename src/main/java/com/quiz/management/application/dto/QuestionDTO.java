package com.quiz.management.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Integer id;

    @NotEmpty(message = "Question Category Cannot be Empty")
    private String category;

    @NotEmpty(message = "Question description Cannot be Empty")
    private String description;

    @Size(min = 2, max = 4, message = "List must contain exactly 4 elements")
    private List<String> options;

    @Positive(message = "Option should be a positive value")
    private Integer correctOption;

    @NotEmpty(message = "Question difficulty Cannot be Empty")
    private String difficulty;

    @JsonIgnore
    private QuizDTO quiz;
}
