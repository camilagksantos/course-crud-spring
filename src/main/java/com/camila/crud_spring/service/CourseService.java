package com.camila.crud_spring.service;

import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.exception.RecordNotFoundException;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("java:S1192")
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> listActiveCourses() {
        return courseRepository.findByStatus(Status.ACTIVE);
    }

    public List<Course> listActiveCoursesWithLessons() {
        return courseRepository.findByStatusWithLessons(Status.ACTIVE);
    }

    public List<Course> listAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> listAllCoursesWithLessons() {
        return courseRepository.findAllWithLessons();
    }


    public Course findActiveById(Long id) {
        return courseRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
    }

    public Course findActiveByIdWithLessons(Long id) {
        return courseRepository.findByIdAndStatusWithLessons(id, Status.ACTIVE)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
    }

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(Long id, Course courseData) {
        return courseRepository.findByIdWithLessons(id)
                .map(existingCourse -> {
                    existingCourse.setName(courseData.getName());
                    existingCourse.setCategory(courseData.getCategory());
                    if (courseData.getStatus() != null) {
                        existingCourse.setStatus(courseData.getStatus());
                    }

                    existingCourse.getLessons().clear();

                    if (courseData.getLessons() != null && !courseData.getLessons().isEmpty()) {
                        courseData.getLessons().forEach(lesson -> {
                            lesson.setCourse(existingCourse);
                            existingCourse.getLessons().add(lesson);
                        });
                    }

                    return courseRepository.save(existingCourse);
                })
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
    }

    @Transactional
    public void hardDeleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
        courseRepository.delete(course);
    }

    @Transactional
    public void softDeleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", id));
        course.setStatus(Status.INACTIVE);
        courseRepository.save(course);
    }

    public boolean validateIdConsistency(Long pathId, Course course) {
        return course.getId() == null || course.getId().equals(pathId);
    }
}
