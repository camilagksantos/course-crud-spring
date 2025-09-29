package com.camila.crud_spring.repository;

import com.camila.crud_spring.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Page<Lesson> findByCourseId(Long courseId, Pageable pageable);

    Page<Lesson> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Lesson> findByYoutubeUrl(String youtubeUrl);
}
