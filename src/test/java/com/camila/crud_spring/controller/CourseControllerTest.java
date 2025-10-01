package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.CourseDTO;
import com.camila.crud_spring.dto.CourseWithLessonRequestDTO;
import com.camila.crud_spring.dto.CourseWithLessonsResponseDTO;
import com.camila.crud_spring.dto.mapper.CourseMapper;
import com.camila.crud_spring.dto.mapper.LessonMapper;
import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.repository.CourseRepository;
import com.camila.crud_spring.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@DisplayName("Course Controller Tests")
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private CourseMapper courseMapper;

    @MockitoBean
    private LessonMapper lessonMapper;

    @MockitoBean
    private CourseRepository courseRepository;

    private Course course;
    private CourseDTO courseDTO;
    private CourseWithLessonsResponseDTO courseWithLessonsDTO;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setName("Angular Basics");
        course.setCategory(Category.FRONTEND);
        course.setStatus(Status.ACTIVE);
        course.setLessons(new ArrayList<>());

        courseDTO = new CourseDTO(1L, "Angular Basics", Category.FRONTEND);
        courseWithLessonsDTO = new CourseWithLessonsResponseDTO(courseDTO, Collections.emptyList());
    }

    @Test
    @DisplayName("Should list active courses with pagination")
    void shouldListActiveCourses() throws Exception {
        // Given
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        when(courseService.listActiveCourses(any(Pageable.class))).thenReturn(coursePage);
        when(courseMapper.toCourseDTO(any(Course.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Angular Basics"));

        verify(courseService, times(1)).listActiveCourses(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 204 when no courses found")
    void shouldReturnNoContentWhenNoCourses() throws Exception {
        // Given
        Page<Course> emptyPage = Page.empty();
        when(courseService.listActiveCourses(any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).listActiveCourses(any(Pageable.class));
    }

    @Test
    @DisplayName("Should get course by ID")
    void shouldGetCourseById() throws Exception {
        // Given
        when(courseService.findActiveById(1L)).thenReturn(course);
        when(courseMapper.toCourseDTO(any(Course.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Angular Basics"));

        verify(courseService, times(1)).findActiveById(1L);
    }

    @Test
    @DisplayName("Should create course")
    void shouldCreateCourse() throws Exception {
        // Given
        String courseJson = """
                {
                    "name": "Angular Basics",
                    "category": "Front-end",
                    "lessons": []
                }
                """;

        when(courseMapper.toCourse(any(CourseWithLessonRequestDTO.class))).thenReturn(course);
        when(courseService.createCourse(any(Course.class))).thenReturn(course);
        when(courseMapper.toCourseWithLessonsResponseDTO(any(Course.class))).thenReturn(courseWithLessonsDTO);

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/courses/1"));

        verify(courseService, times(1)).createCourse(any(Course.class));
    }

    @Test
    @DisplayName("Should return 400 when creating course with invalid data")
    void shouldReturnBadRequestWhenCreatingInvalidCourse() throws Exception {
        // Given - name too short (less than 5 chars)
        String invalidCourseJson = """
                {
                    "name": "AB",
                    "category": "Front-end",
                    "lessons": []
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCourseJson))
                .andExpect(status().isBadRequest());

        verify(courseService, never()).createCourse(any(Course.class));
    }

    @Test
    @DisplayName("Should update course")
    void shouldUpdateCourse() throws Exception {
        // Given
        String courseJson = """
                {
                    "name": "Angular Advanced",
                    "category": "Front-end",
                    "lessons": []
                }
                """;

        when(courseMapper.toCourse(any(CourseWithLessonRequestDTO.class))).thenReturn(course);
        when(courseService.validateIdConsistency(anyLong(), any(Course.class))).thenReturn(true);
        when(courseService.updateCourse(anyLong(), any(Course.class))).thenReturn(course);
        when(courseMapper.toCourseWithLessonsResponseDTO(any(Course.class))).thenReturn(courseWithLessonsDTO);

        // When & Then
        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isOk());

        verify(courseService, times(1)).validateIdConsistency(anyLong(), any(Course.class));
        verify(courseService, times(1)).updateCourse(anyLong(), any(Course.class));
    }

    @Test
    @DisplayName("Should return 400 when updating with inconsistent ID")
    void shouldReturnBadRequestWhenUpdatingWithInconsistentId() throws Exception {
        // Given
        String courseJson = """
                {
                    "name": "Angular Advanced",
                    "category": "Front-end",
                    "lessons": []
                }
                """;

        when(courseMapper.toCourse(any(CourseWithLessonRequestDTO.class))).thenReturn(course);
        when(courseService.validateIdConsistency(anyLong(), any(Course.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isBadRequest());

        verify(courseService, times(1)).validateIdConsistency(anyLong(), any(Course.class));
        verify(courseService, never()).updateCourse(anyLong(), any(Course.class));
    }

    @Test
    @DisplayName("Should soft delete course")
    void shouldSoftDeleteCourse() throws Exception {
        // Given
        doNothing().when(courseService).softDeleteCourse(1L);

        // When & Then
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).softDeleteCourse(1L);
    }

    @Test
    @DisplayName("Should hard delete course")
    void shouldHardDeleteCourse() throws Exception {
        // Given
        doNothing().when(courseService).hardDeleteCourse(1L);

        // When & Then
        mockMvc.perform(delete("/api/courses/1/hard"))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).hardDeleteCourse(1L);
    }

    @Test
    @DisplayName("Should list courses with lessons")
    void shouldListCoursesWithLessons() throws Exception {
        // Given
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        when(courseService.listActiveCoursesWithLessons(any(Pageable.class))).thenReturn(coursePage);
        when(courseMapper.toCourseDTO(any(Course.class))).thenReturn(courseDTO);
        when(lessonMapper.toLessonDTO(any())).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/courses/with-lessons")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(courseService, times(1)).listActiveCoursesWithLessons(any(Pageable.class));
    }

    @Test
    @DisplayName("Should get course by ID with lessons")
    void shouldGetCourseByIdWithLessons() throws Exception {
        // Given
        when(courseService.findActiveByIdWithLessons(1L)).thenReturn(course);
        when(courseMapper.toCourseDTO(any(Course.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses/1/with-lessons"))
                .andExpect(status().isOk());

        verify(courseService, times(1)).findActiveByIdWithLessons(1L);
    }

    @Test
    @DisplayName("Should list all courses including inactive")
    void shouldListAllCourses() throws Exception {
        // Given
        List<Course> courses = List.of(course);
        Page<Course> coursePage = new PageImpl<>(courses);
        when(courseService.listAllCourses(any(Pageable.class))).thenReturn(coursePage);
        when(courseMapper.toCourseDTO(any(Course.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses/all")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(courseService, times(1)).listAllCourses(any(Pageable.class));
    }
}