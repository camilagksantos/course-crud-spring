package com.camila.crud_spring.repository;

import com.camila.crud_spring.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);

    List<Lesson> findByNameContainingIgnoreCase(String name);

    Optional<Lesson> findByYoutubeUrl(String youtubeUrl);
}
