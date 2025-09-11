package com.camila.crud_spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(exclude = "course")
@ToString(exclude = "course")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Column(length = 11, nullable = false)
    @NotBlank(message = "YouTube URL is mandatory")
    private String youtubeUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Lesson() {}

    public Lesson(String name, String youtubeUrl) {
        this.name = name;
        this.youtubeUrl = youtubeUrl;
    }

    public String getFullYouTubeUrl() {
        return "https://www.youtube.com/watch?v=" + this.youtubeUrl;
    }
}
