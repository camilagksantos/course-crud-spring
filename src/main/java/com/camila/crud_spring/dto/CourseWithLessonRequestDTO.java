package com.camila.crud_spring.dto;

import com.camila.crud_spring.enums.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CourseWithLessonRequestDTO(
        @JsonProperty("_id")
        Long id,

        @NotBlank(message = "Name is required")
        @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
        String name,

        Category category,

        @Valid
        List<LessonDTO> lessons
) {
}
