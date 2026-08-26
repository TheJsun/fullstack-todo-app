# Tododle

A simple, fullstack to-do application with task and category management.

## Tech Stack

**Backend**

- Java / Spring Boot
- Hibernate (JPA)
- MySQL

**Frontend**

- Vite + React + TypeScript
- Vitest + React Testing Library (testing)

## Features

- Add categories
- Add new tasks tagged with a task category
- Update tasks (change task name and/or category)
- Delete tasks

## API Endpoints

### Categories

| Method | Endpoint          | Description         |
| ------ | ----------------- | ------------------- |
| GET    | `/categories`     | List all categories |
| POST   | `/categories`     | Create a category   |
| PATCH  | `/categories/:id` | Update a category   |
| DELETE | `/categories/:id` | Delete a category   |

### Todos

| Method | Endpoint     | Description       |
| ------ | ------------ | ----------------- |
| GET    | `/todos`     | List all todos    |
| GET    | `/todos/:id` | Get a single todo |
| POST   | `/todos`     | Create a todo     |
| PATCH  | `/todos/:id` | Update a todo     |
| DELETE | `/todos/:id` | Delete a todo     |

## Project Structure

```
fullstack-todo-app/
├── .mvn/
├── .vscode/
├── src/
│   ├── main/
│   │   ├── java/com/jason/todoapp/fullstack_todo_app/
│   │   │   ├── categories/
│   │   │   │   ├── dtos/            # CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest
│   │   │   │   ├── entities/        # Category
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── CategoryService.java
│   │   │   ├── common/
│   │   │   │   ├── dtos/            # ApiErrorResponse
│   │   │   │   ├── exceptions/      # NotFoundException, UnprocessableContentException
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── config/
│   │   │   │   ├── seeders/         # DataSeeder
│   │   │   │   ├── ModelMapperConfiguration.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── todos/
│   │   │   │   ├── dtos/            # CreateTodoRequest, TodoResponse, UpdateTodoRequest
│   │   │   │   ├── entities/        # Todo
│   │   │   │   ├── TodoRepository.java
│   │   │   │   ├── TodosController.java
│   │   │   │   └── TodoService.java
│   │   │   └── FullstackTodoAppApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
│       ├── java/com/jason/todoapp/fullstack_todo_app/
│       │   ├── category/            # CategoryEndToEndTest, CategoryServiceTest
│       │   ├── todo/                 # TodoEndToEndTest, TodoServiceTest
│       │   └── FullstackTodoAppApplicationTests.java
│       └── resources/
│           ├── schemas/              # api-error, category, category-list, todo, todo-list JSON schemas
│           ├── sql/                  # cleanup.sql
│           └── application.properties
├── todo-app-frontend/
│   ├── config/
│   │   └── test-setup.js
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Button/
│   │   │   ├── Header/
│   │   │   ├── categories/           # CategoryForm, CategoryList
│   │   │   └── todos/                # TodoCard, TodoCreation, TodoForm, TodoList
│   │   ├── schemas/
│   │   ├── scss/
│   │   │   ├── mixins/
│   │   │   └── variables/            # _variables.scss, _normalize.scss
│   │   ├── services/                 # category-services.ts, todo-services.ts
│   │   ├── App.module.scss
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── vite-env.d.ts
│   ├── .env
│   ├── .env.production
│   └── package.json
├── pom.xml
└── README.md
```

## Prerequisites

- Java 17+ (JDK)
- Node.js 18+ and npm
- MySQL 8+
- Maven (or Gradle, depending on your build tool)

## Setup

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd tododle
```

### 2. Database setup

Create a MySQL database:

```sql
CREATE DATABASE tododle;
```

Configure your database connection in `src/main/resources/application.properties` (or `application.yml`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tododle
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

> Replace `your_username` / `your_password` with your actual MySQL credentials, and adjust the port if your MySQL instance doesn't use the default `3306`.

### 3. Run the backend

```bash
./mvnw spring-boot:run
```

The API should be running at `http://localhost:8080` (default Spring Boot port — update if you've configured a different one).

### 4. Run the frontend

```bash
cd todo-app-frontend
npm install
npm run dev
```

The frontend should be running at `http://localhost:5173` (default Vite port).

## Testing

**Backend** — end-to-end and service-level tests per domain (category, todo), validated against JSON schemas:

```bash
./mvnw test
```

**Frontend** — Vitest and React Testing Library:

```bash
cd todo-app-frontend
npm run test
```

## Learnings and Reflections

Notes and design decisions from building this project:

**Controllers**

- `@RestController` serializes return values directly to JSON; `@Controller` resolves a view name to a template instead. This project uses `@RestController` throughout since it's a pure API.
- Controller return types evolved in stages: plain `String` → entity/list → `ResponseEntity<TodoResponse>`, trading convenience for explicit control over status codes, headers, and body.
- `@RequestBody` deserializes the incoming JSON into a Java object for `POST`/`PATCH` parameters; `@Valid` alongside it is what actually triggers Bean Validation (`@NotBlank`, `@NotNull`, etc.) — without `@Valid`, those annotations are inert.

**DTOs**

- `CreateTodoRequest` (client → controller) intentionally excludes server-generated fields (`id`, `createdAt`) and defaults (`isCompleted`), keeping the input contract minimal and validated.
- `TodoResponse` (controller → client) flattens relationships (e.g. `categoryName` instead of a nested `Category`) to avoid lazy-loading serialization issues and to control exactly what's exposed.
- Response DTOs are implemented as Java `record`s — immutable by design (final fields, no setters), which matches what a DTO should be: a snapshot handed off for serialization and never mutated afterward.

**HTTP semantics**

- `ResponseEntity` gives explicit control over status code, headers, and body rather than defaulting to `200 OK` for everything. Key mappings used: `201 Created` on successful `POST`, `204 No Content` on successful `DELETE`, `404 Not Found` on missing lookups, `409 Conflict` for duplicates.

**Global exception handling**

- A `@ControllerAdvice` class with `@ExceptionHandler` methods centralizes error handling — services throw exceptions (e.g. `NotFoundException`), and the handler maps them to the correct status code and error body in one place, instead of repeating `if (missing) return ResponseEntity.notFound().build()` in every endpoint.

**Testing**

- End-to-end tests boot the real app (`@SpringBootTest(webEnvironment = RANDOM_PORT)`) against an in-memory H2 database, reset via SQL scripts between test contexts.
- Tests follow Arrange–Act–Assert, mirrored directly by RestAssured's `given()` (arrange the request) → `when()` (fire the HTTP call) → `then()` (assert status code and body shape).
- Entity IDs are stored as `long` rather than `int` — negligible storage cost for meaningfully more headroom.

Some features I would add next for this project would be to:

- Filter tasks by category
- Update and delete categories
- Create a limit on number of categories created.
