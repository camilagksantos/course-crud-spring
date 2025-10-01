package com.camila.crud_spring.controller;

import com.camila.crud_spring.dto.LessonDTO;
import com.camila.crud_spring.dto.mapper.LessonMapper;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.repository.CourseRepository;
import com.camila.crud_spring.service.LessonService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@DisplayName("Lesson Controller Tests")
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private LessonMapper lessonMapper;

    @MockitoBean
    private CourseRepository courseRepository;

    private Lesson lesson;
    private LessonDTO lessonDTO;

    @BeforeEach
    void setUp() {
        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setName("Introduction to Angular");
        lesson.setYoutubeUrl("dQw4w9WgXcQ");

        lessonDTO = new LessonDTO(1L, "Introduction to Angular", "dQw4w9WgXcQ");
    }

    @Test
    @DisplayName("Should list all lessons with pagination")
    void shouldListAllLessons() throws Exception {
        // Given
        List<Lesson> lessons = List.of(lesson);
        Page<Lesson> lessonPage = new PageImpl<>(lessons);
        when(lessonService.listAllLessons(any(Pageable.class))).thenReturn(lessonPage);
        when(lessonMapper.toLessonDTO(any(Lesson.class))).thenReturn(lessonDTO);

        // When & Then
        mockMvc.perform(get("/api/lessons")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Introduction to Angular"));

        verify(lessonService, times(1)).listAllLessons(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 204 when no lessons found")
    void shouldReturnNoContentWhenNoLessons() throws Exception {
        // Given
        Page<Lesson> emptyPage = Page.empty();
        when(lessonService.listAllLessons(any(Pageable.class))).thenReturn(emptyPage);

        // When & Then
        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).listAllLessons(any(Pageable.class));
    }

    @Test
    @DisplayName("Should list lessons by course ID")
    void shouldListLessonsByCourse() throws Exception {
        // Given
        List<Lesson> lessons = List.of(lesson);
        Page<Lesson> lessonPage = new PageImpl<>(lessons);
        when(lessonService.listLessonsByCourse(anyLong(), any(Pageable.class))).thenReturn(lessonPage);
        when(lessonMapper.toLessonDTO(any(Lesson.class))).thenReturn(lessonDTO);

        // When & Then
        mockMvc.perform(get("/api/lessons/course/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(lessonService, times(1)).listLessonsByCourse(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should get lesson by ID")
    void shouldGetLessonById() throws Exception {
        // Given
        when(lessonService.findById(1L)).thenReturn(lesson);
        when(lessonMapper.toLessonDTO(any(Lesson.class))).thenReturn(lessonDTO);

        // When & Then
        mockMvc.perform(get("/api/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Introduction to Angular"));

        verify(lessonService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create lesson")
    void shouldCreateLesson() throws Exception {
        // Given
        String lessonJson = """
                {
                    "name": "Introduction to Angular",
                    "youtubeUrl": "dQw4w9WgXcQ"
                }
                """;

        when(lessonMapper.toLesson(any(LessonDTO.class))).thenReturn(lesson);
        when(lessonService.createLesson(any(Lesson.class), anyLong())).thenReturn(lesson);
        when(lessonMapper.toLessonDTO(any(Lesson.class))).thenReturn(lessonDTO);

        // When & Then
        mockMvc.perform(post("/api/lessons/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/lessons/1"));

        verify(lessonService, times(1)).createLesson(any(Lesson.class), anyLong());
    }

    @Test
    @DisplayName("Should return 400 when creating lesson with invalid data")
    void shouldReturnBadRequestWhenCreatingInvalidLesson() throws Exception {
        // Given - name too short (less than 3 chars)
        String invalidLessonJson = """
                {
                    "name": "AB",
                    "youtubeUrl": "dQw4w9WgXcQ"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/lessons/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLessonJson))
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).createLesson(any(Lesson.class), anyLong());
    }

    @Test
    @DisplayName("Should return 400 when creating lesson with blank YouTube URL")
    void shouldReturnBadRequestWhenYoutubeUrlBlank() throws Exception {
        // Given - YouTube URL blank
        String invalidLessonJson = """
                {
                    "name": "Introduction to Angular",
                    "youtubeUrl": ""
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/lessons/course/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidLessonJson))
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).createLesson(any(Lesson.class), anyLong());
    }

    @Test
    @DisplayName("Should update lesson")
    void shouldUpdateLesson() throws Exception {
        // Given
        String lessonJson = """
                {
                    "name": "Advanced Angular Topics",
                    "youtubeUrl": "newVideo123"
                }
                """;

        when(lessonMapper.toLesson(any(LessonDTO.class))).thenReturn(lesson);
        when(lessonService.validateIdConsistency(anyLong(), any(Lesson.class))).thenReturn(true);
        when(lessonService.updateLesson(anyLong(), any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.toLessonDTO(any(Lesson.class))).thenReturn(lessonDTO);

        // When & Then
        mockMvc.perform(put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk());

        verify(lessonService, times(1)).validateIdConsistency(anyLong(), any(Lesson.class));
        verify(lessonService, times(1)).updateLesson(anyLong(), any(Lesson.class));
    }

    @Test
    @DisplayName("Should return 400 when updating with inconsistent ID")
    void shouldReturnBadRequestWhenUpdatingWithInconsistentId() throws Exception {
        // Given
        String lessonJson = """
                {
                    "name": "Advanced Angular Topics",
                    "youtubeUrl": "newVideo123"
                }
                """;

        when(lessonMapper.toLesson(any(LessonDTO.class))).thenReturn(lesson);
        when(lessonService.validateIdConsistency(anyLong(), any(Lesson.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isBadRequest());

        verify(lessonService, times(1)).validateIdConsistency(anyLong(), any(Lesson.class));
        verify(lessonService, never()).updateLesson(anyLong(), any(Lesson.class));
    }

    @Test
    @DisplayName("Should delete lesson")
    void shouldDeleteLesson() throws Exception {
        // Given
        doNothing().when(lessonService).deleteLesson(1L);

        // When & Then
        mockMvc.perform(delete("/api/lessons/1"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).deleteLesson(1L);
    }

    @Test
    @DisplayName("Should return 400 when ID is negative")
    void shouldReturnBadRequestWhenIdIsNegative() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/lessons/-1"))
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Should return 400 when course ID is negative")
    void shouldReturnBadRequestWhenCourseIdIsNegative() throws Exception {
        // Given
        String lessonJson = """
                {
                    "name": "Introduction to Angular",
                    "youtubeUrl": "dQw4w9WgXcQ"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/lessons/course/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).createLesson(any(Lesson.class), anyLong());
    }

    @Test
    @DisplayName("Should apply pagination parameters correctly")
    void shouldApplyPaginationParameters() throws Exception {
        // Given
        Page<Lesson> lessonPage = Page.empty();
        when(lessonService.listAllLessons(any(Pageable.class))).thenReturn(lessonPage);

        // When & Then
        mockMvc.perform(get("/api/lessons")
                        .param("page", "2")
                        .param("size", "20")
                        .param("sortBy", "name")
                        .param("direction", "DESC"))
                .andExpect(status().isNoContent());

        verify(lessonService, times(1)).listAllLessons(any(Pageable.class));
    }
}