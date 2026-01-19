# 📝 To-Do Application — Spring Boot (Kotlin)

A production-style **RESTful To-Do API** built with **Spring Boot and Kotlin**, featuring comprehensive CRUD operations, database persistence, pagination, filtering, and automated API documentation.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## 🚀 Features

### ✅ Core Functionality

- **Complete CRUD Operations** for task management
- **PostgreSQL Persistence** with Spring Data JPA
- **Pagination Support** for efficient data retrieval
- **Status Filtering** (PENDING/COMPLETED)
- **Type-Safe Enum** for task status
- **Input Validation** with detailed error messages
- **Global Exception Handling** for consistent error responses
- **Swagger/OpenAPI Documentation** for interactive API exploration
- **Comprehensive Testing** with JUnit 5, MockK, and MockMvc

---

## 📋 API Endpoints

### Task Management Operations

| Operation | HTTP Method | Endpoint | Description |
|-----------|------------|----------|-------------|
| Create task | `POST` | `/api/todos` | Create a new to-do task |
| Get all tasks | `GET` | `/api/todos` | Retrieve all tasks (with pagination & filtering) |
| Get task by ID | `GET` | `/api/todos/{id}` | Retrieve a specific task |
| Update task | `PUT` | `/api/todos/{id}` | Update an existing task |
| Delete task | `DELETE` | `/api/todos/{id}` | Delete a task |

---

## 🔍 Query Parameters

### Pagination

Retrieve paginated results:

```http
GET /api/todos?page=0&size=10
```

**Parameters:**
- `page` — Page number (0-indexed)
- `size` — Number of items per page

### Status Filtering

Filter tasks by status:

```http
GET /api/todos?status=PENDING
GET /api/todos?status=COMPLETED
```

### Combined Query

Use both pagination and filtering together:

```http
GET /api/todos?page=1&size=5&status=COMPLETED
```

---

## 🏗️ Project Architecture

Clean layered architecture following best practices:

```
com.example.todo
│
├── controller/       → REST API endpoints
├── service/          → Business logic layer
├── repository/       → Database access (Spring Data JPA)
├── entity/           → JPA entities + TaskStatus enum
├── dto/              → Request/Response DTOs
└── exception/        → Global exception handling
```

### Key Components

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic and validation
- **Repository Layer**: Database operations using Spring Data JPA
- **Entity Layer**: Domain models and database entities
- **DTO Layer**: Data transfer objects for API contracts
- **Exception Layer**: Centralized error handling

---

## 🗄️ Database Configuration

### PostgreSQL Setup

Create the database and user:

```sql
CREATE DATABASE todo_db;
CREATE USER todo_user WITH PASSWORD 'todo123';
GRANT ALL PRIVILEGES ON DATABASE todo_db TO todo_user;
```

### Application Configuration

Configure `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=todo_user
spring.datasource.password=todo123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 📘 API Documentation

Interactive API documentation is available via Swagger UI:

**Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

**OpenAPI Specification:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Using Swagger UI

From Swagger UI, you can:
- ✅ View all available endpoints
- ✅ Test APIs directly from your browser
- ✅ See request/response schemas
- ✅ View validation rules and constraints

---

## 📦 Request/Response Examples

### Create a To-Do Task

**Request:** `POST /api/todos`

```json
{
  "title": "Learn Spring Boot",
  "description": "Build a comprehensive ToDo application with Kotlin",
  "status": "PENDING"
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Build a comprehensive ToDo application with Kotlin",
  "status": "PENDING"
}
```

### Update a To-Do Task

**Request:** `PUT /api/todos/1`

```json
{
  "title": "Learn Spring Boot",
  "description": "Successfully completed the project",
  "status": "COMPLETED"
}
```

**Response:** `200 OK`

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Successfully completed the project",
  "status": "COMPLETED"
}
```

### Get All Tasks (Paginated)

**Request:** `GET /api/todos?page=0&size=5&status=PENDING`

**Response:** `200 OK`

```json
{
  "content": [
    {
      "id": 1,
      "title": "Learn Spring Boot",
      "description": "Build a ToDo project",
      "status": "PENDING"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5
  },
  "totalElements": 1,
  "totalPages": 1
}
```

---

## ⚠️ Validation Rules

The API enforces the following validation constraints:

| Field | Rule | Error Message |
|-------|------|---------------|
| `title` | Not blank | "Title cannot be empty" |
| `title` | 3-100 characters | "Title must be between 3 and 100 characters" |
| `description` | Max 255 characters | "Description cannot exceed 255 characters" |
| `status` | Valid enum value | "Invalid status value" |

### Validation Error Example

**Invalid Request:**

```json
{
  "title": "",
  "description": "Test task"
}
```

**Error Response:** `400 Bad Request`

```json
{
  "message": "title: Title cannot be empty",
  "status": 400,
  "timestamp": "2026-01-19T10:20:30Z"
}
```

---

## 🧠 Task Status Enum

Tasks use a type-safe enum for status management:

```kotlin
enum class TaskStatus {
    PENDING,
    COMPLETED
}
```

**Benefits:**
- Type safety at compile time
- Prevents invalid status values
- Auto-completion in IDEs
- Clear documentation

---

## 🧪 Testing

Comprehensive test coverage including unit and integration tests.

### Test Structure

- **Unit Tests**: Service layer testing with MockK
- **Controller Tests**: API endpoint testing with MockMvc
- **Integration Tests**: End-to-end testing with test database

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 1.9+ | Primary programming language |
| **Spring Boot** | 3.x | Backend framework |
| **Spring Data JPA** | 3.x | ORM and database access |
| **PostgreSQL** | 15+ | Relational database |
| **Springdoc OpenAPI** | 2.x | API documentation (Swagger) |
| **JUnit 5** | 5.x | Testing framework |
| **MockK** | 1.13+ | Mocking library for Kotlin |
| **MockMvc** | — | Spring MVC test framework |
| **Gradle** | 8.x | Build automation |

---

## 🚀 Getting Started

### Prerequisites

Before running the application, ensure you have:

- ✅ Java 17 or higher
- ✅ PostgreSQL 15 or higher
- ✅ Gradle 8.x (or use the wrapper)

### Installation Steps

#### 1️⃣ Clone the Repository

```bash
git clone <https://github.com/Princccee/Todo-Kotlin.git>
cd demo
```

#### 2️⃣ Configure Database

Start PostgreSQL and create the database:

```bash
psql -U postgres
```

```sql
CREATE DATABASE todo_db;
CREATE USER todo_user WITH PASSWORD 'todo123';
GRANT ALL PRIVILEGES ON DATABASE todo_db TO todo_user;
\q
```

#### 3️⃣ Update Application Properties

Edit `src/main/resources/application.properties` if needed to match your database configuration.

#### 4️⃣ Build the Project

```bash
./gradlew clean build
```

#### 5️⃣ Run the Application

```bash
./gradlew bootRun
```

The application will start at `http://localhost:8080`

### Verify Installation

Test the API:

```bash
curl http://localhost:8080/api/todos
```

Access Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

---


## 🎯 Future Enhancements

Potential improvements for this project:

- [ ] **Sorting**: Add `?sort=title,asc` parameter support
- [ ] **Search**: Implement full-text search by title
- [ ] **Timestamps**: Add `createdAt` and `updatedAt` fields
- [ ] **Authentication**: Implement JWT or OAuth2 security
- [ ] **User Management**: Multi-user support with task ownership
- [ ] **Categories/Tags**: Organize tasks with labels
- [ ] **Due Dates**: Add deadline tracking
- [ ] **Priority Levels**: HIGH, MEDIUM, LOW priority
- [ ] **Docker**: Containerize the entire application
- [ ] **CI/CD**: Set up automated testing and deployment
- [ ] **Caching**: Implement Redis for performance
- [ ] **Monitoring**: Add Actuator and metrics

---


## 👨‍💻 Author

**Prince Kumar**
``