package com.camila.crud_spring.dto.mapper;

import com.camila.crud_spring.dto.LessonDTO;
import com.camila.crud_spring.model.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    public LessonDTO toLessonDTO(Lesson lesson) {
        if (lesson == null) {
            return null;
        }
        return new LessonDTO(
                lesson.getId(),
                lesson.getName(),
                lesson.getYoutubeUrl()
        );
    }

    public Lesson toLesson(LessonDTO lessonDTO) {
        if (lessonDTO == null) {
            return null;
        }
        Lesson lesson = new Lesson();
        lesson.setId(lessonDTO.id());
        lesson.setName(lessonDTO.name());
        lesson.setYoutubeUrl(lessonDTO.youtubeUrl());
        return lesson;
    }
}
