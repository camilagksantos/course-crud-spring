package com.camila.crud_spring.dto;

import com.camila.crud_spring.enums.Category;

import java.util.List;

public record CourseWithLessonsDTO(
        Long id,
        String name,
        Category category,
        List<LessonDTO> lessons
) {
}
