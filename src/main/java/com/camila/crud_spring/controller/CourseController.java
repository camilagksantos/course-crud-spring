package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.CourseDTO;
import com.camila.crud_spring.dto.mapper.CourseMapper;
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
    public ResponseEntity<CourseDTO> create(@RequestBody @Valid CourseDTO courseDTO) {
        var course = courseMapper.toCourse(courseDTO);
        var savedCourse = courseService.createCourse(course);
        var location = URI.create("/api/courses/" + savedCourse.getId());

        return ResponseEntity.created(location).body(courseMapper.toCourseDTO(savedCourse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid CourseDTO courseDTO) {

        var course = courseMapper.toCourse(courseDTO);
        if (!courseService.validateIdConsistency(id, course)) {

            return ResponseEntity.badRequest().build();
        }

        Course updatedCourse = courseService.updateCourse(id, course);

        return ResponseEntity.ok(courseMapper.toCourseDTO(updatedCourse));
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
}
