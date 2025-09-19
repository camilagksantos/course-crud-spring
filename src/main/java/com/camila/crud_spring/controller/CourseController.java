package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.CourseDTO;
import com.camila.crud_spring.dto.CourseWithLessonRequestDTO;
import com.camila.crud_spring.dto.CourseWithLessonsResponseDTO;
import com.camila.crud_spring.dto.mapper.CourseMapper;
import com.camila.crud_spring.dto.mapper.LessonMapper;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final LessonMapper lessonMapper;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> list() {
        var courses = courseService.listActiveCourses();
        var courseDTOs = courses.stream().map(courseMapper::toCourseDTO).toList();

        return courses.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(courseDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> listAll() {
        var courses = courseService.listAllCourses();
        var courseDTOs = courses.stream().map(courseMapper::toCourseDTO).toList();

        return courseDTOs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(courseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getById(@PathVariable @NotNull @Positive Long id) {
        Course course = courseService.findActiveById(id);
        return ResponseEntity.ok(courseMapper.toCourseDTO(course));
    }

    @PostMapping
    public ResponseEntity<CourseWithLessonsResponseDTO> create(@RequestBody @Valid CourseWithLessonRequestDTO courseDTO) {
        var course = courseMapper.toCourse(courseDTO);
        var savedCourse = courseService.createCourse(course);
        var location = URI.create("/api/courses/" + savedCourse.getId());

        return ResponseEntity.created(location)
                .body(courseMapper.toCourseWithLessonsResponseDTO(savedCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseWithLessonsResponseDTO> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid CourseWithLessonRequestDTO courseDTO) {

        var course = courseMapper.toCourse(courseDTO);
        if (!courseService.validateIdConsistency(id, course)) {
            return ResponseEntity.badRequest().build();
        }

        Course updatedCourse = courseService.updateCourse(id, course);

        return ResponseEntity.ok(courseMapper.toCourseWithLessonsResponseDTO(updatedCourse));
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id) {
        courseService.hardDeleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable @NotNull @Positive Long id) {
        courseService.softDeleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-lessons")
    public ResponseEntity<List<CourseWithLessonsResponseDTO>> listWithLessons() {
        var courses = courseService.listActiveCoursesWithLessons();
        var coursesWithLessons = courses.stream()
                .map(course -> {
                    var courseDTO = courseMapper.toCourseDTO(course);
                    var lessonDTOs = course.getLessons().stream()
                            .map(lessonMapper::toLessonDTO)
                            .toList();

                    return new CourseWithLessonsResponseDTO(courseDTO, lessonDTOs);
                })
                .toList();

        return coursesWithLessons.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(coursesWithLessons);
    }

    @GetMapping("/{id}/with-lessons")
    public ResponseEntity<CourseWithLessonsResponseDTO> getByIdWithLessons(@PathVariable @NotNull @Positive Long id) {
        Course course = courseService.findActiveByIdWithLessons(id);

        var lessonDTOs = course.getLessons().stream()
                .map(lessonMapper::toLessonDTO)
                .toList();

        var courseDTO = courseMapper.toCourseDTO(course);
        var courseWithLessons = new CourseWithLessonsResponseDTO(courseDTO, lessonDTOs);

        return ResponseEntity.ok(courseWithLessons);
    }
}
