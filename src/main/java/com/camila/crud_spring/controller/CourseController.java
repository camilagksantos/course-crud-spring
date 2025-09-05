package com.camila.crud_spring.controller;

import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> list() {
        var courses = courseService.listActiveCourses();
        return courses.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(courses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> listAll() {
        var courses = courseService.listAllCourses();
        return courses.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable @NotNull @Positive Long id) {
        return courseService.findActiveById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody @Valid Course course) {
        var savedCourse = courseService.createCourse(course);
        var location = URI.create("/api/courses/" + savedCourse.getId());
        return ResponseEntity.created(location).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid Course course) {
        if (!courseService.validateIdConsistency(id, course)) {
            return ResponseEntity.badRequest().build();
        }

        return courseService.updateCourse(id, course)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id) {
        return courseService.hardDeleteCourse(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable @NotNull @Positive Long id) {
        return courseService.softDeleteCourse(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
