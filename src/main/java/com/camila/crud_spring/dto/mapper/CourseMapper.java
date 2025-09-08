package com.camila.crud_spring.dto.mapper;

import com.camila.crud_spring.dto.CourseDTO;
import com.camila.crud_spring.model.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDTO toCourseDTO(Course course) {
        if (course == null) {
            return null;
        }
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getCategory()
        );
    }

    public Course toCourse(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }
        Course course = new Course();
        course.setId(courseDTO.id());
        course.setName(courseDTO.name());
        course.setCategory(courseDTO.category());
        return course;
    }
}
