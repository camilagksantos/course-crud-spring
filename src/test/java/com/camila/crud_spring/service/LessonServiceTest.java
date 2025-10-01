package com.camila.crud_spring.service;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.exception.RecordNotFoundException;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.repository.CourseRepository;
import com.camila.crud_spring.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Lesson Service Tests")
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    private Course course;
    private Lesson lesson;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        // Setup Course entity
        course = new Course();
        course.setId(1L);
        course.setName("Angular Basics");
        course.setCategory(Category.FRONTEND);
        course.setStatus(Status.ACTIVE);
        course.setLessons(new ArrayList<>());

        // Setup Lesson entity
        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setName("Introduction to Angular");
        lesson.setYoutubeUrl("dQw4w9WgXcQ");
        lesson.setCourse(course);
    }

    @Test
    @DisplayName("Should create lesson successfully")
    void shouldCreateLesson() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        // When
        Lesson result = lessonService.createLesson(lesson, 1L);

        // Then
        assertNotNull(result);
        assertEquals("Introduction to Angular", result.getName());
        assertEquals("dQw4w9WgXcQ", result.getYoutubeUrl());
        assertEquals(course, result.getCourse());
        verify(courseRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    @DisplayName("Should throw exception when creating lesson for non-existent course")
    void shouldThrowExceptionWhenCreatingLessonForNonExistentCourse() {
        // Given
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> {
            lessonService.createLesson(lesson, 999L);
        });
        verify(courseRepository, times(1)).findById(999L);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    @DisplayName("Should find lesson by ID successfully")
    void shouldFindLessonById() {
        // Given
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        // When
        Lesson result = lessonService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Introduction to Angular", result.getName());
        assertEquals("dQw4w9WgXcQ", result.getYoutubeUrl());
        verify(lessonRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when lesson not found")
    void shouldThrowExceptionWhenLessonNotFound() {
        // Given
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> {
            lessonService.findById(999L);
        });
        verify(lessonRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update lesson successfully")
    void shouldUpdateLesson() {
        // Given
        Lesson updatedData = new Lesson();
        updatedData.setName("Angular Advanced Topics");
        updatedData.setYoutubeUrl("newVideo123");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        // When
        Lesson result = lessonService.updateLesson(1L, updatedData);

        // Then
        assertNotNull(result);
        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent lesson")
    void shouldThrowExceptionWhenUpdatingNonExistentLesson() {
        // Given
        Lesson updateData = new Lesson();
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> {
            lessonService.updateLesson(999L, updateData);
        });
        verify(lessonRepository, times(1)).findById(999L);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    @DisplayName("Should delete lesson successfully")
    void shouldDeleteLesson() {
        // Given
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        // When
        lessonService.deleteLesson(1L);

        // Then
        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent lesson")
    void shouldThrowExceptionWhenDeletingNonExistentLesson() {
        // Given
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> {
            lessonService.deleteLesson(999L);
        });
        verify(lessonRepository, times(1)).findById(999L);
        verify(lessonRepository, never()).delete(any(Lesson.class));
    }

    @Test
    @DisplayName("Should list all lessons with pagination")
    void shouldListAllLessonsWithPagination() {
        // Given
        List<Lesson> lessons = List.of(lesson);
        Page<Lesson> lessonPage = new PageImpl<>(lessons, pageable, 1);

        when(lessonRepository.findAll(pageable)).thenReturn(lessonPage);

        // When
        Page<Lesson> result = lessonService.listAllLessons(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Introduction to Angular", result.getContent().get(0).getName());
        verify(lessonRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should list lessons by course ID")
    void shouldListLessonsByCourseId() {
        // Given
        List<Lesson> lessons = List.of(lesson);
        Page<Lesson> lessonPage = new PageImpl<>(lessons, pageable, 1);

        when(lessonRepository.findByCourseId(1L, pageable)).thenReturn(lessonPage);

        // When
        Page<Lesson> result = lessonService.listLessonsByCourse(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Introduction to Angular", result.getContent().get(0).getName());
        verify(lessonRepository, times(1)).findByCourseId(1L, pageable);
    }

    @Test
    @DisplayName("Should validate ID consistency - valid case")
    void shouldValidateIdConsistencyValid() {
        // Given
        lesson.setId(1L);

        // When
        boolean result = lessonService.validateIdConsistency(1L, lesson);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should validate ID consistency - null ID case")
    void shouldValidateIdConsistencyNullId() {
        // Given
        lesson.setId(null);

        // When
        boolean result = lessonService.validateIdConsistency(1L, lesson);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should validate ID consistency - invalid case")
    void shouldValidateIdConsistencyInvalid() {
        // Given
        lesson.setId(2L);

        // When
        boolean result = lessonService.validateIdConsistency(1L, lesson);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should create lesson and associate with course correctly")
    void shouldCreateLessonAndAssociateWithCourse() {
        // Given
        Lesson newLesson = new Lesson();
        newLesson.setName("RxJS Operators");
        newLesson.setYoutubeUrl("rxjs123456");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(invocation -> {
            Lesson saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        // When
        Lesson result = lessonService.createLesson(newLesson, 1L);

        // Then
        assertNotNull(result);
        assertEquals(course, result.getCourse());
        assertEquals("RxJS Operators", result.getName());
        verify(courseRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    @DisplayName("Should update only lesson name and URL without changing course")
    void shouldUpdateOnlyLessonDataWithoutChangingCourse() {
        // Given
        Lesson updateData = new Lesson();
        updateData.setName("Updated Lesson Name");
        updateData.setYoutubeUrl("updated1234");

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        // When
        Lesson result = lessonService.updateLesson(1L, updateData);

        // Then
        assertNotNull(result);
        assertEquals(course, lesson.getCourse()); // Course should remain unchanged
        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }
}