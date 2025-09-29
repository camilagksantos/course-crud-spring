package com.camila.crud_spring.service;

import com.camila.crud_spring.exception.RecordNotFoundException;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.repository.CourseRepository;
import com.camila.crud_spring.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@SuppressWarnings("java:S1192")
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public Page<Lesson> listAllLessons(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    public Page<Lesson> listLessonsByCourse(Long courseId, Pageable pageable) {
        return lessonRepository.findByCourseId(courseId, pageable);
    }

    public Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Lição não encontrada com id: ", id));
    }

    public Lesson createLesson(Lesson lesson, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RecordNotFoundException("Curso não encontrado com id: ", courseId));

        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Long id, Lesson lessonData) {
        return lessonRepository.findById(id)
                .map(existingLesson -> {
                    existingLesson.setName(lessonData.getName());
                    existingLesson.setYoutubeUrl(lessonData.getYoutubeUrl());
                    return lessonRepository.save(existingLesson);
                })
                .orElseThrow(() -> new RecordNotFoundException("Lição não encontrada com id: ", id));
    }

    public void deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Lição não encontrada com id: ", id));
        lessonRepository.delete(lesson);
    }

    public boolean validateIdConsistency(Long pathId, Lesson lesson) {
        return lesson.getId() == null || lesson.getId().equals(pathId);
    }
}
