package com.camila.crud_spring.repository;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Lesson Repository Tests")
class LessonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LessonRepository lessonRepository;

    private Course course1;
    private Course course2;
    private Lesson lesson1;
    private Lesson lesson2;
    private Lesson lesson3;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        entityManager.getEntityManager()
                .createQuery("DELETE FROM Lesson").executeUpdate();
        entityManager.getEntityManager()
                .createQuery("DELETE FROM Course").executeUpdate();
        entityManager.flush();
        entityManager.clear();

        // Create first course with lessons
        course1 = new Course();
        course1.setName("Angular Basics");
        course1.setCategory(Category.FRONTEND);
        course1.setStatus(Status.ACTIVE);
        entityManager.persist(course1);

        lesson1 = new Lesson();
        lesson1.setName("Introduction to Angular");
        lesson1.setYoutubeUrl("angular1234"); // 11 chars
        lesson1.setCourse(course1);
        entityManager.persist(lesson1);

        lesson2 = new Lesson();
        lesson2.setName("Angular Components");
        lesson2.setYoutubeUrl("angular6789"); // 11 chars
        lesson2.setCourse(course1);
        entityManager.persist(lesson2);

        // Create second course with lesson
        course2 = new Course();
        course2.setName("React Basics");
        course2.setCategory(Category.FRONTEND);
        course2.setStatus(Status.ACTIVE);
        entityManager.persist(course2);

        lesson3 = new Lesson();
        lesson3.setName("Introduction to React");
        lesson3.setYoutubeUrl("react12345");
        lesson3.setCourse(course2);
        entityManager.persist(lesson3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should find lessons by course ID")
    void shouldFindLessonsByCourseId() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByCourseId(course1.getId(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(lesson -> lesson.getCourse().getId().equals(course1.getId())));
    }

    @Test
    @DisplayName("Should return empty page when course has no lessons")
    void shouldReturnEmptyPageWhenCourseHasNoLessons() {
        // Given
        Course emptyCourse = new Course();
        emptyCourse.setName("Empty Course");
        emptyCourse.setCategory(Category.BACKEND);
        emptyCourse.setStatus(Status.ACTIVE);
        entityManager.persist(emptyCourse);
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByCourseId(emptyCourse.getId(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Should find lessons by name containing ignore case")
    void shouldFindLessonsByNameContainingIgnoreCase() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByNameContainingIgnoreCase("angular", pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(lesson -> lesson.getName().toLowerCase().contains("angular")));
    }

    @Test
    @DisplayName("Should find lessons case insensitively")
    void shouldFindLessonsCaseInsensitively() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByNameContainingIgnoreCase("ANGULAR", pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should find lessons with partial name match")
    void shouldFindLessonsWithPartialNameMatch() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByNameContainingIgnoreCase("Introduction", pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(lesson -> lesson.getName().contains("Introduction")));
    }

    @Test
    @DisplayName("Should return empty page when no lessons match name")
    void shouldReturnEmptyPageWhenNoLessonsMatchName() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Lesson> result = lessonRepository.findByNameContainingIgnoreCase("NonExistent", pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    @DisplayName("Should find lesson by YouTube URL")
    void shouldFindLessonByYoutubeUrl() {
        // When
        Optional<Lesson> result = lessonRepository.findByYoutubeUrl("angular1234");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Introduction to Angular", result.get().getName());
        assertEquals("angular1234", result.get().getYoutubeUrl());
    }

    @Test
    @DisplayName("Should return empty when YouTube URL not found")
    void shouldReturnEmptyWhenYoutubeUrlNotFound() {
        // When
        Optional<Lesson> result = lessonRepository.findByYoutubeUrl("nonexistent123");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should persist lesson correctly")
    void shouldPersistLessonCorrectly() {
        // Given
        Lesson newLesson = new Lesson();
        newLesson.setName("Advanced Angular Concepts");
        newLesson.setYoutubeUrl("advanced123");
        newLesson.setCourse(course1);

        // When
        Lesson savedLesson = lessonRepository.save(newLesson);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Lesson> result = lessonRepository.findById(savedLesson.getId());
        assertTrue(result.isPresent());
        assertEquals("Advanced Angular Concepts", result.get().getName());
        assertEquals("advanced123", result.get().getYoutubeUrl());
        assertEquals(course1.getId(), result.get().getCourse().getId());
    }

    @Test
    @DisplayName("Should delete lesson correctly")
    void shouldDeleteLessonCorrectly() {
        // Given
        Long lessonId = lesson1.getId();

        // When
        lessonRepository.deleteById(lessonId);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Lesson> result = lessonRepository.findById(lessonId);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should update lesson correctly")
    void shouldUpdateLessonCorrectly() {
        // Given
        lesson1.setName("Updated Lesson Name");
        lesson1.setYoutubeUrl("updated123");

        // When
        lessonRepository.save(lesson1);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Lesson> result = lessonRepository.findById(lesson1.getId());
        assertTrue(result.isPresent());
        assertEquals("Updated Lesson Name", result.get().getName());
        assertEquals("updated123", result.get().getYoutubeUrl());
    }

    @Test
    @DisplayName("Should maintain relationship with course after update")
    void shouldMaintainRelationshipWithCourseAfterUpdate() {
        // Given
        Long originalCourseId = lesson1.getCourse().getId();
        lesson1.setName("Updated Name");

        // When
        lessonRepository.save(lesson1);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Lesson> result = lessonRepository.findById(lesson1.getId());
        assertTrue(result.isPresent());
        assertEquals(originalCourseId, result.get().getCourse().getId());
    }
}