package com.camila.crud_spring.dto.mapper;

import com.camila.crud_spring.dto.CourseDTO;
import com.camila.crud_spring.dto.CourseWithLessonRequestDTO;
import com.camila.crud_spring.dto.CourseWithLessonsResponseDTO;
import com.camila.crud_spring.dto.LessonDTO;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO toCourseDTO(Course course) {
        if (course == null) return null;

        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getCategory()
        );
    }

    public Course toCourse(CourseDTO courseDTO) {
        if (courseDTO == null) return null;

        Course course = new Course();
        course.setId(courseDTO.id());
        course.setName(courseDTO.name());
        course.setCategory(courseDTO.category());

        return course;
    }

    public CourseWithLessonsResponseDTO toCourseWithLessonsResponseDTO(Course course) {
        if (course == null) return null;

        List<LessonDTO> lessonDTOs = null;
        if (course.getLessons() != null) {
            lessonDTOs = course.getLessons().stream()
                    .map(this::toLessonDTO)
                    .collect(Collectors.toList());
        }

        CourseDTO courseDTO = toCourseDTO(course);

        return new CourseWithLessonsResponseDTO(courseDTO, lessonDTOs);
    }

    public Course toCourse(CourseWithLessonRequestDTO courseDTO) {
        if (courseDTO == null) return null;

        Course course = new Course();
        course.setId(courseDTO.id());
        course.setName(courseDTO.name());
        course.setCategory(courseDTO.category());

        if (courseDTO.lessons() != null && !courseDTO.lessons().isEmpty()) {
            List<Lesson> lessons = courseDTO.lessons().stream()
                    .map(lessonDTO -> {
                        Lesson lesson = new Lesson();
                        lesson.setId(lessonDTO.id());
                        lesson.setName(lessonDTO.name());
                        lesson.setYoutubeUrl(lessonDTO.youtubeUrl());
                        lesson.setCourse(course);
                        return lesson;
                    })
                    .collect(Collectors.toList());

            course.setLessons(lessons);
        }

        return course;
    }

    private LessonDTO toLessonDTO(Lesson lesson) {
        if (lesson == null) return null;

        return new LessonDTO(
                lesson.getId(),
                lesson.getName(),
                lesson.getYoutubeUrl()
        );
    }
}
