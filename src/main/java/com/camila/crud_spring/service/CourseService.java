package com.camila.crud_spring.service;

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

    public Optional<Course> findActiveById(Long id) {
        return courseRepository.findByIdAndStatus(id, STATUS_ACTIVE);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> updateCourse(Long id, Course courseData) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    existingCourse.setName(courseData.getName());
                    existingCourse.setCategory(courseData.getCategory());

                    if (courseData.getStatus() != null) {
                        existingCourse.setStatus(courseData.getStatus());
                    }
                    return courseRepository.save(existingCourse);
                });
    }

    public boolean hardDeleteCourse(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            courseRepository.delete(course.get());
            return true;
        }
        return false;
    }

    public boolean softDeleteCourse(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course courseToUpdate = course.get();
            courseToUpdate.setStatus(STATUS_INACTIVE);
            courseRepository.save(courseToUpdate);
            return true;
        }
        return false;
    }

    public boolean validateIdConsistency(Long pathId, Course course) {
        return course.getId() == null || course.getId().equals(pathId);
    }
}
