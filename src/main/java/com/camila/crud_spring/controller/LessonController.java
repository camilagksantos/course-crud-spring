package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.LessonDTO;
import com.camila.crud_spring.dto.mapper.LessonMapper;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.service.LessonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/lessons")
@AllArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @GetMapping
    public ResponseEntity<Page<LessonDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<LessonDTO> lessonDTOs = lessonService.listAllLessons(pageable)
                .map(lessonMapper::toLessonDTO);

        return lessonDTOs.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(lessonDTOs);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<LessonDTO>> listByCourse(
            @PathVariable @NotNull @Positive Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<LessonDTO> lessonDTOs = lessonService.listLessonsByCourse(courseId, pageable)
                .map(lessonMapper::toLessonDTO);

        return lessonDTOs.isEmpty()
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
