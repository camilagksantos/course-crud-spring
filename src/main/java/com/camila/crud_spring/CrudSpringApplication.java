package com.camila.crud_spring;

import com.camila.crud_spring.enums.Category;
import com.camila.crud_spring.enums.Status;
import com.camila.crud_spring.model.Course;
import com.camila.crud_spring.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
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
			c4.setStatus(Status.ACTIVE);
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
		};
	}
}

