package com.camila.crud_spring.service;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.exception.RecordNotFoundException;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.repository.CourseRepository;
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
@DisplayName("Course Service Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

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
    @DisplayName("Should create course successfully")
    void shouldCreateCourse() {
        // Given
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        Course result = courseService.createCourse(course);

        // Then
        assertNotNull(result);
        assertEquals("Angular Basics", result.getName());
        assertEquals(Category.FRONTEND, result.getCategory());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should create course with lessons")
    void shouldCreateCourseWithLessons() {
        // Given
        course.getLessons().add(lesson);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        Course result = courseService.createCourse(course);

        // Then
        assertNotNull(result);
        assertEquals("Angular Basics", result.getName());
        assertEquals(1, result.getLessons().size());
        assertEquals("Introduction to Angular", result.getLessons().getFirst().getName());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should find active course by ID successfully")
    void shouldFindActiveCourseById() {
        // Given
        when(courseRepository.findByIdAndStatus(1L, Status.ACTIVE)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.findActiveById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Angular Basics", result.getName());
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(courseRepository, times(1)).findByIdAndStatus(1L, Status.ACTIVE);
    }

    @Test
    @DisplayName("Should throw exception when active course not found")
    void shouldThrowExceptionWhenActiveCourseNotFound() {
        // Given
        when(courseRepository.findByIdAndStatus(anyLong(), any(Status.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> courseService.findActiveById(999L));
        verify(courseRepository, times(1)).findByIdAndStatus(999L, Status.ACTIVE);
    }

    @Test
    @DisplayName("Should find active course with lessons by ID")
    void shouldFindActiveCourseWithLessonsById() {
        // Given
        course.getLessons().add(lesson);
        when(courseRepository.findByIdAndStatusWithLessons(1L, Status.ACTIVE)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.findActiveByIdWithLessons(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getLessons().size());
        verify(courseRepository, times(1)).findByIdAndStatusWithLessons(1L, Status.ACTIVE);
    }

    @Test
    @DisplayName("Should update course successfully")
    void shouldUpdateCourse() {
        // Given
        Course updatedData = new Course();
        updatedData.setName("Angular Advanced");
        updatedData.setCategory(Category.FRONTEND);
        updatedData.setStatus(Status.ACTIVE);
        updatedData.setLessons(new ArrayList<>());

        when(courseRepository.findByIdWithLessons(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        Course result = courseService.updateCourse(1L, updatedData);

        // Then
        assertNotNull(result);
        verify(courseRepository, times(1)).findByIdWithLessons(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent course")
    void shouldThrowExceptionWhenUpdatingNonExistentCourse() {
        // Given
        Course updateData = new Course();
        when(courseRepository.findByIdWithLessons(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> courseService.updateCourse(999L, updateData));
        verify(courseRepository, times(1)).findByIdWithLessons(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Should soft delete course")
    void shouldSoftDeleteCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        courseService.softDeleteCourse(1L);

        // Then
        assertEquals(Status.INACTIVE, course.getStatus());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Should hard delete course")
    void shouldHardDeleteCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        // When
        courseService.hardDeleteCourse(1L);

        // Then
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    @DisplayName("Should throw exception when hard deleting non-existent course")
    void shouldThrowExceptionWhenHardDeletingNonExistentCourse() {
        // Given
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecordNotFoundException.class, () -> courseService.hardDeleteCourse(999L));
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).delete(any(Course.class));
    }

    @Test
    @DisplayName("Should list active courses with pagination")
    void shouldListActiveCoursesWithPagination() {
        // Given
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, 1);

        when(courseRepository.findByStatus(Status.ACTIVE, pageable)).thenReturn(coursePage);

        // When
        Page<Course> result = courseService.listActiveCourses(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Angular Basics", result.getContent().getFirst().getName());
        verify(courseRepository, times(1)).findByStatus(Status.ACTIVE, pageable);
    }

    @Test
    @DisplayName("Should list active courses with lessons")
    void shouldListActiveCoursesWithLessons() {
        // Given
        course.getLessons().add(lesson);
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, 1);

        when(courseRepository.findByStatusWithLessons(Status.ACTIVE, pageable)).thenReturn(coursePage);

        // When
        Page<Course> result = courseService.listActiveCoursesWithLessons(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().getFirst().getLessons().size());
        verify(courseRepository, times(1)).findByStatusWithLessons(Status.ACTIVE, pageable);
    }

    @Test
    @DisplayName("Should list all courses including inactive")
    void shouldListAllCourses() {
        // Given
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, 1);

        when(courseRepository.findAll(pageable)).thenReturn(coursePage);

        // When
        Page<Course> result = courseService.listAllCourses(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(courseRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should list all courses with lessons")
    void shouldListAllCoursesWithLessons() {
        // Given
        course.getLessons().add(lesson);
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, 1);

        when(courseRepository.findAllWithLessons(pageable)).thenReturn(coursePage);

        // When
        Page<Course> result = courseService.listAllCoursesWithLessons(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(courseRepository, times(1)).findAllWithLessons(pageable);
    }

    @Test
    @DisplayName("Should validate ID consistency - valid case")
    void shouldValidateIdConsistencyValid() {
        // Given
        course.setId(1L);

        // When
        boolean result = courseService.validateIdConsistency(1L, course);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should validate ID consistency - null ID case")
    void shouldValidateIdConsistencyNullId() {
        // Given
        course.setId(null);

        // When
        boolean result = courseService.validateIdConsistency(1L, course);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should validate ID consistency - invalid case")
    void shouldValidateIdConsistencyInvalid() {
        // Given
        course.setId(2L);

        // When
        boolean result = courseService.validateIdConsistency(1L, course);

        // Then
        assertFalse(result);
    }
}