# 🎓 Course Management API

A RESTful API built with Spring Boot for managing online courses and lessons, featuring comprehensive CRUD operations, pagination, and soft delete capabilities.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Project Structure](#-project-structure)
- [Configuration](#-configuration)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- ✅ Complete CRUD operations for courses and lessons
- ✅ RESTful API design with proper HTTP methods and status codes
- ✅ Server-side pagination with customizable page size and sorting
- ✅ Soft delete functionality (status-based)
- ✅ One-to-Many relationship management (Course ↔ Lessons)
- ✅ Bean Validation for input validation
- ✅ Global exception handling
- ✅ DTO pattern for data transfer
- ✅ H2 in-memory database with persistent storage
- ✅ 10 predefined course categories
- ✅ Category-based filtering with icons

## 🛠️ Tech Stack

- **Java** 21
- **Spring Boot** 3.5.4
- **Spring Data JPA** - Database access layer
- **Spring Web** - RESTful API
- **H2 Database** - In-memory database with file persistence
- **Maven** - Dependency management
- **Bean Validation** - Input validation
- **Lombok** - Boilerplate code reduction

## 🏗️ Architecture

The application follows a layered architecture pattern:

```
┌─────────────────┐
│   Controller    │  ← REST endpoints, DTO conversion
├─────────────────┤
│    Service      │  ← Business logic
├─────────────────┤
│   Repository    │  ← Data access (Spring Data JPA)
├─────────────────┤
│    Database     │  ← H2 Database
└─────────────────┘
```

### Design Patterns

- **DTO Pattern**: Separation between entities and API contracts
- **Repository Pattern**: Abstraction of data access
- **Service Layer**: Business logic encapsulation
- **Exception Handling**: Centralized error management

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/camilagksantos/course-crud-spring.git
   cd course-crud-spring
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API: `http://localhost:8080/api`
   - H2 Console: `http://localhost:8080/h2-console`

### H2 Database Configuration

**JDBC URL**: `jdbc:h2:file:./data/courses-crud-angular-spring`  
**Username**: `sa`  
**Password**: *(empty)*

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Course Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/courses` | List active courses (paginated) | No |
| `GET` | `/courses/all` | List all courses including inactive | No |
| `GET` | `/courses/{id}` | Get course by ID | No |
| `GET` | `/courses/{id}/with-lessons` | Get course with lessons | No |
| `GET` | `/courses/with-lessons` | List all courses with lessons (paginated) | No |
| `POST` | `/courses` | Create new course | No |
| `PUT` | `/courses/{id}` | Update course | No |
| `DELETE` | `/courses/{id}` | Soft delete course | No |
| `DELETE` | `/courses/{id}/hard` | Hard delete course | No |

### Lesson Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/lessons` | List all lessons | No |
| `GET` | `/lessons/course/{courseId}` | Get lessons by course | No |
| `GET` | `/lessons/{id}` | Get lesson by ID | No |
| `POST` | `/lessons/course/{courseId}` | Create lesson | No |
| `PUT` | `/lessons/{id}` | Update lesson | No |
| `DELETE` | `/lessons/{id}` | Delete lesson | No |

### Pagination Parameters

All list endpoints support pagination:

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | Integer | `0` | Page number (zero-based) |
| `size` | Integer | `10` | Items per page |
| `sortBy` | String | `name` | Sort field |
| `direction` | String | `ASC` | Sort direction (ASC/DESC) |

**Example Request:**
```bash
GET /api/courses?page=0&size=10&sortBy=name&direction=ASC
```

### Request/Response Examples

#### Create Course

**Request:**
```bash
POST /api/courses
Content-Type: application/json

{
  "name": "Advanced Angular",
  "category": "FRONTEND",
  "lessons": [
    {
      "name": "RxJS Operators",
      "youtubeUrl": "dQw4w9WgXcQ"
    }
  ]
}
```

**Response:** `201 Created`
```json
{
  "_id": 1,
  "name": "Advanced Angular",
  "category": "FRONTEND",
  "status": "ACTIVE",
  "lessons": [
    {
      "id": 1,
      "name": "RxJS Operators",
      "youtubeUrl": "dQw4w9WgXcQ"
    }
  ]
}
```

#### List Courses (Paginated)

**Request:**
```bash
GET /api/courses?page=0&size=5
```

**Response:** `200 OK`
```json
{
  "content": [
    {
      "_id": 1,
      "name": "Angular Basics",
      "category": "FRONTEND",
      "status": "ACTIVE"
    }
  ],
  "page": {
    "size": 5,
    "number": 0,
    "totalElements": 32,
    "totalPages": 7
  }
}
```

### Error Responses

The API returns consistent error responses:

**400 Bad Request** - Validation error
```json
{
  "timestamp": "2025-09-30T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/courses"
}
```

**404 Not Found**
```json
{
  "timestamp": "2025-09-30T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Course not found with id: 999",
  "path": "/api/courses/999"
}
```

## 🗄️ Database Schema

### Course Entity

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGINT | PK, Auto | Unique identifier |
| `name` | VARCHAR(100) | NOT NULL | Course name (5-100 chars) |
| `category` | INTEGER | NOT NULL | Category enum (ORDINAL) |
| `status` | INTEGER | NOT NULL | Status enum (ORDINAL) |

### Lesson Entity

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGINT | PK, Auto | Unique identifier |
| `name` | VARCHAR(100) | NOT NULL | Lesson name (3-100 chars) |
| `youtube_url` | VARCHAR(11) | NOT NULL | YouTube video ID (11 chars) |
| `course_id` | BIGINT | FK, NOT NULL | Reference to course |

### Enums

**Status:**
- `0` - ACTIVE
- `1` - INACTIVE

**Category:**
- `0` - FRONTEND
- `1` - BACKEND
- `2` - DATA_SCIENCE
- `3` - DEVOPS
- `4` - DATABASE
- `5` - MOBILE
- `6` - CLOUD
- `7` - SECURITY
- `8` - DESIGN
- `9` - TESTING

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/camila/crud_spring/
│   │       ├── controller/      # REST endpoints
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── model/           # JPA entities
│   │       ├── repository/      # Spring Data repositories
│   │       ├── service/         # Business logic
│   │       └── exception/       # Custom exceptions
│   └── resources/
│       ├── application.yml      # Application config
│       └── data.sql            # Initial data
└── test/
    └── java/                    # Test classes
```

## ⚙️ Configuration

### Application Properties

Key configurations in `application.yml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:file:./data/courses-crud-angular-spring
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    
  h2:
    console:
      enabled: true
      path: /h2-console
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Application port | `8080` |
| `DB_URL` | Database URL | `jdbc:h2:file:./data/courses-crud-angular-spring` |
| `DB_USERNAME` | Database username | `sa` |
| `DB_PASSWORD` | Database password | *(empty)* |

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=CourseServiceTest
```

### Test Coverage
```bash
mvn clean test jacoco:report
```

Coverage report: `target/site/jacoco/index.html`

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes (formatting)
- `refactor:` Code refactoring
- `test:` Adding tests
- `chore:` Maintenance tasks

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Camila Kfouri**
- GitHub: [@camilagksantos](https://github.com/camilagksantos)
- LinkedIn: [Camila Kfouri](https://www.linkedin.com/in/camila-kfouri/)

## 🙏 Acknowledgments

- Spring Boot Team for the amazing framework
- Angular Team for the frontend framework
- H2 Database for the lightweight database solution

---

⭐ **If you found this project helpful, please give it a star!** ⭐
