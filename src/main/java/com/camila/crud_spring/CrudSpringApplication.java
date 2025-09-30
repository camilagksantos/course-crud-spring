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

            Course c9 = new Course();
            c9.setName("Vue.js Completo");
            c9.setCategory(Category.FRONTEND);
            c9.setStatus(Status.ACTIVE);
            courses.add(c9);

            Course c10 = new Course();
            c10.setName("Node.js e Express");
            c10.setCategory(Category.BACKEND);
            c10.setStatus(Status.ACTIVE);
            courses.add(c10);

            Course c11 = new Course();
            c11.setName("Flutter Mobile");
            c11.setCategory(Category.MOBILE);
            c11.setStatus(Status.ACTIVE);
            courses.add(c11);

            Course c12 = new Course();
            c12.setName("React Native");
            c12.setCategory(Category.MOBILE);
            c12.setStatus(Status.ACTIVE);
            courses.add(c12);

            Course c13 = new Course();
            c13.setName("AWS Fundamentos");
            c13.setCategory(Category.CLOUD);
            c13.setStatus(Status.ACTIVE);
            courses.add(c13);

            Course c14 = new Course();
            c14.setName("Azure DevOps");
            c14.setCategory(Category.CLOUD);
            c14.setStatus(Status.ACTIVE);
            courses.add(c14);

            Course c15 = new Course();
            c15.setName("Segurança Web");
            c15.setCategory(Category.SECURITY);
            c15.setStatus(Status.ACTIVE);
            courses.add(c15);

            Course c16 = new Course();
            c16.setName("Ethical Hacking");
            c16.setCategory(Category.SECURITY);
            c16.setStatus(Status.ACTIVE);
            courses.add(c16);

            Course c17 = new Course();
            c17.setName("UI/UX Design");
            c17.setCategory(Category.DESIGN);
            c17.setStatus(Status.ACTIVE);
            courses.add(c17);

            Course c18 = new Course();
            c18.setName("Figma Avançado");
            c18.setCategory(Category.DESIGN);
            c18.setStatus(Status.ACTIVE);
            courses.add(c18);

            Course c19 = new Course();
            c19.setName("Testes Automatizados");
            c19.setCategory(Category.TESTING);
            c19.setStatus(Status.ACTIVE);
            courses.add(c19);

            Course c20 = new Course();
            c20.setName("Jest e Testing Library");
            c20.setCategory(Category.TESTING);
            c20.setStatus(Status.ACTIVE);
            courses.add(c20);

            Course c21 = new Course();
            c21.setName("MongoDB Essencial");
            c21.setCategory(Category.DATABASE);
            c21.setStatus(Status.ACTIVE);
            courses.add(c21);

            Course c22 = new Course();
            c22.setName("PostgreSQL Avançado");
            c22.setCategory(Category.DATABASE);
            c22.setStatus(Status.ACTIVE);
            courses.add(c22);

            Course c23 = new Course();
            c23.setName("Python Django");
            c23.setCategory(Category.BACKEND);
            c23.setStatus(Status.ACTIVE);
            courses.add(c23);

            Course c24 = new Course();
            c24.setName("Ruby on Rails");
            c24.setCategory(Category.BACKEND);
            c24.setStatus(Status.ACTIVE);
            courses.add(c24);

            Course c25 = new Course();
            c25.setName("Deep Learning");
            c25.setCategory(Category.DATA_SCIENCE);
            c25.setStatus(Status.ACTIVE);
            courses.add(c25);

            Course c26 = new Course();
            c26.setName("Terraform e IaC");
            c26.setCategory(Category.DEVOPS);
            c26.setStatus(Status.ACTIVE);
            courses.add(c26);

            Course c27 = new Course();
            c27.setName("CI/CD com Jenkins");
            c27.setCategory(Category.DEVOPS);
            c27.setStatus(Status.ACTIVE);
            courses.add(c27);

            Course c28 = new Course();
            c28.setName("Svelte Framework");
            c28.setCategory(Category.FRONTEND);
            c28.setStatus(Status.ACTIVE);
            courses.add(c28);

            Course c29 = new Course();
            c29.setName("Go para Backend");
            c29.setCategory(Category.BACKEND);
            c29.setStatus(Status.ACTIVE);
            courses.add(c29);

            Course c30 = new Course();
            c30.setName("Kotlin Android");
            c30.setCategory(Category.MOBILE);
            c30.setStatus(Status.ACTIVE);
            courses.add(c30);

            Course c31 = new Course();
            c31.setName("Google Cloud Platform");
            c31.setCategory(Category.CLOUD);
            c31.setStatus(Status.ACTIVE);
            courses.add(c31);

            Course c32 = new Course();
            c32.setName("Cypress E2E");
            c32.setCategory(Category.TESTING);
            c32.setStatus(Status.ACTIVE);
            courses.add(c32);

            Course c33 = new Course();
            c33.setName("GraphQL APIs");
            c33.setCategory(Category.BACKEND);
            c33.setStatus(Status.INACTIVE);
            courses.add(c33);

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

            Lesson lesson12 = new Lesson("Introdução ao Vue", "OrxmtDw4pVI");
            lesson12.setCourse(c9);
            c9.getLessons().add(lesson12);

            Lesson lesson13 = new Lesson("Setup Node.js", "TlB_eWDSMt4");
            lesson13.setCourse(c10);
            c10.getLessons().add(lesson13);

            Lesson lesson14 = new Lesson("Criando APIs REST", "vjf774RKrLc");
            lesson14.setCourse(c10);
            c10.getLessons().add(lesson14);

            Lesson lesson15 = new Lesson("Componentes React Native", "0-S5a0eXPoc");
            lesson15.setCourse(c12);
            c12.getLessons().add(lesson15);

            Lesson lesson16 = new Lesson("EC2 Básico", "iHX-jtKghC0");
            lesson16.setCourse(c13);
            c13.getLessons().add(lesson16);

            Lesson lesson17 = new Lesson("S3 Storage", "tfU0JEZjcsg");
            lesson17.setCourse(c13);
            c13.getLessons().add(lesson17);

            Lesson lesson18 = new Lesson("OWASP Top 10", "GchojK3dWFA");
            lesson18.setCourse(c15);
            c15.getLessons().add(lesson18);

            Lesson lesson19 = new Lesson("Penetration Testing", "3Kq1MIfTWCE");
            lesson19.setCourse(c16);
            c16.getLessons().add(lesson19);

            Lesson lesson20 = new Lesson("Network Security", "qiQR5rTSshw");
            lesson20.setCourse(c16);
            c16.getLessons().add(lesson20);

            Lesson lesson21 = new Lesson("Princípios de UX", "Ovj4hFxko7c");
            lesson21.setCourse(c17);
            c17.getLessons().add(lesson21);

            Lesson lesson22 = new Lesson("Unit Testing", "r9HdJ8P6GQI");
            lesson22.setCourse(c19);
            c19.getLessons().add(lesson22);

            Lesson lesson23 = new Lesson("Integration Tests", "QM1iUe6IofM");
            lesson23.setCourse(c19);
            c19.getLessons().add(lesson23);

            Lesson lesson24 = new Lesson("Testing React", "8Xwq35cPwYg");
            lesson24.setCourse(c20);
            c20.getLessons().add(lesson24);

            Lesson lesson25 = new Lesson("Advanced Queries", "qw--VYLpxG4");
            lesson25.setCourse(c22);
            c22.getLessons().add(lesson25);

            Lesson lesson26 = new Lesson("Django Setup", "F5mRW0jo-U4");
            lesson26.setCourse(c23);
            c23.getLessons().add(lesson26);

            Lesson lesson27 = new Lesson("Django ORM", "rHux0gMZ3Eg");
            lesson27.setCourse(c23);
            c23.getLessons().add(lesson27);

            Lesson lesson28 = new Lesson("Neural Networks", "aircAruvnKk");
            lesson28.setCourse(c25);
            c25.getLessons().add(lesson28);

            Lesson lesson29 = new Lesson("Terraform Basics", "l5k1ai_GBDE");
            lesson29.setCourse(c26);
            c26.getLessons().add(lesson29);

            Lesson lesson30 = new Lesson("Infrastructure as Code", "tomUWcQ0P3k");
            lesson30.setCourse(c26);
            c26.getLessons().add(lesson30);

            courseRepository.saveAll(courses);
        };
	}
}

