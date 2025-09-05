package com.camila.crud_spring.repository;

import com.camila.crud_spring.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStatus(String status);

    Optional<Course> findByIdAndStatus(Long id, String status);
}
