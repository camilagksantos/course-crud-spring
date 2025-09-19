package com.camila.crud_spring.dto;

import java.util.List;

public record CourseWithLessonsResponseDTO(
        CourseDTO course,
        List<LessonDTO> lessons
) {
}
