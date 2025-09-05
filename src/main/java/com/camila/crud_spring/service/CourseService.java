package com.camila.crud_spring.service;

import com.camila.crud_spring.exception.RecordNotFoundException;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private static final String STATUS_ACTIVE = "Active";
    private static final String STATUS_INACTIVE = "Inactive";

    private final CourseRepository courseRepository;

    public List<Course> listActiveCourses() {
        return courseRepository.findByStatus(STATUS_ACTIVE);
    }

    public List<Course> listAllCourses() {
        return courseRepository.findAll();
    }

    public Course findActiveById(Long id) {
        return courseRepository.findByIdAndStatus(id, STATUS_ACTIVE)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseData) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    existingCourse.setName(courseData.getName());
                    existingCourse.setCategory(courseData.getCategory());
                    if (courseData.getStatus() != null) {
                        existingCourse.setStatus(courseData.getStatus());
                    }
                    return courseRepository.save(existingCourse);
                })
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
    }

    public void hardDeleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
        courseRepository.delete(course);
    }

    public void softDeleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
        course.setStatus(STATUS_INACTIVE);
        courseRepository.save(course);
    }

    public boolean validateIdConsistency(Long pathId, Course course) {
        return course.getId() == null || course.getId().equals(pathId);
    }
}
