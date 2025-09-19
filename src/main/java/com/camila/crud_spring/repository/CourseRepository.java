package com.camila.crud_spring.repository;

import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatus(Status status);

    Optional<Course> findByIdAndStatus(Long id, Status status);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") Long id);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id AND c.status = :status")
    Optional<Course> findByIdAndStatusWithLessons(@Param("id") Long id, @Param("status") Status status);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.status = :status")
    List<Course> findByStatusWithLessons(@Param("status") Status status);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons")
    List<Course> findAllWithLessons();
}
