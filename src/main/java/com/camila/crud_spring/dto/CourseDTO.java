package com.camila.crud_spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseDTO(
        @JsonProperty("_id")
        Long id,

        @NotBlank(message = "Name is required")
        @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
        String name,

        @NotBlank(message = "Category is required")
        String category
) { }
