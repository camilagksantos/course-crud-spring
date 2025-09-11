package com.camila.crud_spring.model;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(exclude = "lessons")
@ToString(exclude = "lessons")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters")
    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "course")
    @Valid
    private List<Lesson> lessons = new ArrayList<>();
}
