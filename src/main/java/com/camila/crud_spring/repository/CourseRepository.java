package com.camila.crud_spring.repository;

import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByStatus(Status status, Pageable pageable);

    Optional<Course> findByIdAndStatus(Long id, Status status);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") Long id);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id AND c.status = :status")
    Optional<Course> findByIdAndStatusWithLessons(@Param("id") Long id, @Param("status") Status status);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.status = :status")
    Page<Course> findByStatusWithLessons(@Param("status") Status status, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons")
    Page<Course> findAllWithLessons(Pageable pageable);
}
