package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.LessonDTO;
import com.camila.crud_spring.dto.mapper.LessonMapper;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.service.LessonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @GetMapping
    public ResponseEntity<List<LessonDTO>> list() {
        var lessons = lessonService.listAllLessons();
        var lessonDTOs = lessons.stream().map(lessonMapper::toLessonDTO).toList();

        return lessons.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lessonDTOs);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonDTO>> listByCourse(@PathVariable @NotNull @Positive Long courseId) {
        var lessons = lessonService.listLessonsByCourse(courseId);
        var lessonDTOs = lessons.stream().map(lessonMapper::toLessonDTO).toList();

        return lessons.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lessonDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getById(@PathVariable @NotNull @Positive Long id) {
        Lesson lesson = lessonService.findById(id);
        return ResponseEntity.ok(lessonMapper.toLessonDTO(lesson));
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<LessonDTO> create(
            @PathVariable @NotNull @Positive Long courseId,
            @RequestBody @Valid LessonDTO lessonDTO) {

        var lesson = lessonMapper.toLesson(lessonDTO);
        var savedLesson = lessonService.createLesson(lesson, courseId);
        var location = URI.create("/api/lessons/" + savedLesson.getId());

        return ResponseEntity.created(location).body(lessonMapper.toLessonDTO(savedLesson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> update(
            @PathVariable @NotNull @Positive Long id,
            @RequestBody @Valid LessonDTO lessonDTO) {

        var lesson = lessonMapper.toLesson(lessonDTO);
        if (!lessonService.validateIdConsistency(id, lesson)) {
            return ResponseEntity.badRequest().build();
        }

        Lesson updatedLesson = lessonService.updateLesson(id, lesson);
        return ResponseEntity.ok(lessonMapper.toLessonDTO(updatedLesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
