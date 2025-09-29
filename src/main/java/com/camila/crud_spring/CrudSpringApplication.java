package com.camila.crud_spring;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.model.Lesson;
import com.camila.crud_spring.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CrudSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(CourseRepository courseRepository) {
		return args -> {
			courseRepository.deleteAll();

            List<Course> courses = new ArrayList<>();

            Course c1 = new Course();
            c1.setName("Angular Básico");
            c1.setCategory(Category.FRONTEND);
            c1.setStatus(Status.ACTIVE);
            courses.add(c1);

            Course c2 = new Course();
            c2.setName("Java Spring Boot");
            c2.setCategory(Category.BACKEND);
            c2.setStatus(Status.ACTIVE);
            courses.add(c2);

            Course c3 = new Course();
            c3.setName("React Avançado");
            c3.setCategory(Category.FRONTEND);
            c3.setStatus(Status.ACTIVE);
            courses.add(c3);

            Course c4 = new Course();
            c4.setName("Python para Data Science");
            c4.setCategory(Category.DATA_SCIENCE);
            c4.setStatus(Status.INACTIVE);
            courses.add(c4);

            Course c5 = new Course();
            c5.setName("Docker e Kubernetes");
            c5.setCategory(Category.DEVOPS);
            c5.setStatus(Status.ACTIVE);
            courses.add(c5);

            Course c6 = new Course();
            c6.setName("Machine Learning");
            c6.setCategory(Category.DATA_SCIENCE);
            c6.setStatus(Status.ACTIVE);
            courses.add(c6);

            Course c7 = new Course();
            c7.setName("TypeScript Completo");
            c7.setCategory(Category.FRONTEND);
            c7.setStatus(Status.ACTIVE);
            courses.add(c7);

            Course c8 = new Course();
            c8.setName("Banco de Dados SQL");
            c8.setCategory(Category.DATABASE);
            c8.setStatus(Status.ACTIVE);
            courses.add(c8);

            courseRepository.saveAll(courses);

            Lesson lesson1 = new Lesson("Introdução ao Angular", "dQw4w9WgXcQ");
            lesson1.setCourse(c1);
            c1.getLessons().add(lesson1);

            Lesson lesson2 = new Lesson("Componentes e Templates", "oHg5SJYRHA0");
            lesson2.setCourse(c1);
            c1.getLessons().add(lesson2);

            Lesson lesson3 = new Lesson("Configuração do Spring Boot", "rRKAktbMNt4");
            lesson3.setCourse(c2);
            c2.getLessons().add(lesson3);

            Lesson lesson4 = new Lesson("REST APIs com Spring", "6NaBfBMnp7Q");
            lesson4.setCourse(c2);
            c2.getLessons().add(lesson4);

            Lesson lesson5 = new Lesson("Hooks Avançados", "3OGEprdqwFo");
            lesson5.setCourse(c3);
            c3.getLessons().add(lesson5);

            Lesson lesson6 = new Lesson("Pandas Básico", "HW29067qVWk");
            lesson6.setCourse(c4);
            c4.getLessons().add(lesson6);

            Lesson lesson7 = new Lesson("Algoritmos Supervisionados", "aircAruvnKk");
            lesson7.setCourse(c6);
            c6.getLessons().add(lesson7);

            Lesson lesson8 = new Lesson("Redes Neurais", "IHZwWFHWa-w");
            lesson8.setCourse(c6);
            c6.getLessons().add(lesson8);

            Lesson lesson9 = new Lesson("Tipos Avançados", "ahCwqrYpIuM");
            lesson9.setCourse(c7);
            c7.getLessons().add(lesson9);

            Lesson lesson10 = new Lesson("SELECT e JOINs", "XFu3VIj5o2c");
            lesson10.setCourse(c8);
            c8.getLessons().add(lesson10);

            Lesson lesson11 = new Lesson("Índices e Performance", "YufocuHbYZo");
            lesson11.setCourse(c8);
            c8.getLessons().add(lesson11);

            courseRepository.saveAll(courses);
        };
	}
}

