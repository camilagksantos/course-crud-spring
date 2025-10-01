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
@DisplayName("Course Repository Tests")
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Course activeCourse;
    private Course inactiveCourse;
    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        entityManager.getEntityManager()
                .createQuery("DELETE FROM Lesson").executeUpdate();
        entityManager.getEntityManager()
                .createQuery("DELETE FROM Course").executeUpdate();
        entityManager.flush();
        entityManager.clear();

        // Create active course with lessons
        activeCourse = new Course();
        activeCourse.setName("Angular Basics");
        activeCourse.setCategory(Category.FRONTEND);
        activeCourse.setStatus(Status.ACTIVE);
        entityManager.persist(activeCourse);

        lesson1 = new Lesson();
        lesson1.setName("Introduction to Angular");
        lesson1.setYoutubeUrl("video12345");
        lesson1.setCourse(activeCourse);
        entityManager.persist(lesson1);

        lesson2 = new Lesson();
        lesson2.setName("Components Deep Dive");
        lesson2.setYoutubeUrl("video67890");
        lesson2.setCourse(activeCourse);
        entityManager.persist(lesson2);

        // Create inactive course
        inactiveCourse = new Course();
        inactiveCourse.setName("Java Basics");
        inactiveCourse.setCategory(Category.BACKEND);
        inactiveCourse.setStatus(Status.INACTIVE);
        entityManager.persist(inactiveCourse);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should find courses by status ACTIVE")
    void shouldFindCoursesByStatusActive() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Course> result = courseRepository.findByStatus(Status.ACTIVE, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Angular Basics", result.getContent().getFirst().getName());
        assertEquals(Status.ACTIVE, result.getContent().getFirst().getStatus());
    }

    @Test
    @DisplayName("Should find courses by status INACTIVE")
    void shouldFindCoursesByStatusInactive() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Course> result = courseRepository.findByStatus(Status.INACTIVE, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Java Basics", result.getContent().getFirst().getName());
        assertEquals(Status.INACTIVE, result.getContent().getFirst().getStatus());
    }

    @Test
    @DisplayName("Should find active course by ID and status")
    void shouldFindActiveCourseByIdAndStatus() {
        // When
        Optional<Course> result = courseRepository.findByIdAndStatus(activeCourse.getId(), Status.ACTIVE);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Angular Basics", result.get().getName());
        assertEquals(Status.ACTIVE, result.get().getStatus());
    }

    @Test
    @DisplayName("Should not find inactive course when searching for active")
    void shouldNotFindInactiveCourseWhenSearchingForActive() {
        // When
        Optional<Course> result = courseRepository.findByIdAndStatus(inactiveCourse.getId(), Status.ACTIVE);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find course with lessons using custom query")
    void shouldFindCourseWithLessons() {
        // When
        Optional<Course> result = courseRepository.findByIdWithLessons(activeCourse.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals("Angular Basics", result.get().getName());
        assertNotNull(result.get().getLessons());
        assertEquals(2, result.get().getLessons().size());
    }

    @Test
    @DisplayName("Should find active course with lessons")
    void shouldFindActiveCourseWithLessons() {
        // When
        Optional<Course> result = courseRepository.findByIdAndStatusWithLessons(
                activeCourse.getId(),
                Status.ACTIVE
        );

        // Then
        assertTrue(result.isPresent());
        assertEquals("Angular Basics", result.get().getName());
        assertEquals(Status.ACTIVE, result.get().getStatus());
        assertEquals(2, result.get().getLessons().size());
    }

    @Test
    @DisplayName("Should not find inactive course with lessons when searching for active")
    void shouldNotFindInactiveCourseWithLessonsWhenSearchingForActive() {
        // When
        Optional<Course> result = courseRepository.findByIdAndStatusWithLessons(
                inactiveCourse.getId(),
                Status.ACTIVE
        );

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find active courses with lessons paginated")
    void shouldFindActiveCoursesWithLessonsPaginated() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Course> result = courseRepository.findByStatusWithLessons(Status.ACTIVE, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        Course course = result.getContent().getFirst();
        assertEquals("Angular Basics", course.getName());
        assertEquals(2, course.getLessons().size());
    }

    @Test
    @DisplayName("Should find all courses with lessons")
    void shouldFindAllCoursesWithLessons() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Course> result = courseRepository.findAllWithLessons(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements()); // activeCourse + inactiveCourse

        Optional<Course> courseWithLessons = result.getContent().stream()
                .filter(c -> c.getName().equals("Angular Basics"))
                .findFirst();

        assertTrue(courseWithLessons.isPresent());
        assertEquals(2, courseWithLessons.get().getLessons().size());
    }

    @Test
    @DisplayName("Should handle course without lessons")
    void shouldHandleCourseWithoutLessons() {
        // When
        Optional<Course> result = courseRepository.findByIdWithLessons(inactiveCourse.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals("Java Basics", result.get().getName());
        assertTrue(result.get().getLessons().isEmpty());
    }

    @Test
    @DisplayName("Should return empty when course not found")
    void shouldReturnEmptyWhenCourseNotFound() {
        // When
        Optional<Course> result = courseRepository.findByIdWithLessons(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should persist course with lessons correctly")
    void shouldPersistCourseWithLessons() {
        // Given
        Course newCourse = new Course();
        newCourse.setName("React Basics");
        newCourse.setCategory(Category.FRONTEND);
        newCourse.setStatus(Status.ACTIVE);

        Lesson newLesson = new Lesson();
        newLesson.setName("Introduction to React");
        newLesson.setYoutubeUrl("react12345");
        newLesson.setCourse(newCourse);
        newCourse.getLessons().add(newLesson);

        // When
        Course savedCourse = courseRepository.save(newCourse);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Course> result = courseRepository.findByIdWithLessons(savedCourse.getId());
        assertTrue(result.isPresent());
        assertEquals("React Basics", result.get().getName());
        assertEquals(1, result.get().getLessons().size());
        assertEquals("Introduction to React", result.get().getLessons().getFirst().getName());
    }
}