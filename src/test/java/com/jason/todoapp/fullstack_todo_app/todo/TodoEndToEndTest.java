package com.jason.todoapp.fullstack_todo_app.todo;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.todos.TodoRepository;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(
  scripts = "/sql/cleanup.sql",
  executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class TodoEndToEndTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setup() {
    RestAssured.port = this.port;
  }

  //Tests for Getting Todos

  @Test
  public void getAllTodos_NoTodosInDB_ReturnOkAndEmptyArray() {
    given()
      .when()
      .get("/todos")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body("$", hasSize(0));
  }

  @Test
  public void getAllTodos_TodosInDB_ReturnsOkAndArrayOfTodos() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    Todo todo1 = new Todo();
    todo1.setTitle("Test todo1");
    todo1.setDescription("Test todo1 description");
    todo1.setDueDate(LocalDate.of(2026, 12, 12));
    todo1.setIsCompleted(false);
    todo1.setCategory(exampleCategory);
    todoRepository.saveAndFlush(todo1);

    Todo todo2 = new Todo();
    todo2.setTitle("Test todo2");
    todo2.setDescription("Test todo2 description");
    todo2.setDueDate(LocalDate.of(2026, 12, 12));
    todo2.setIsCompleted(false);
    todo2.setCategory(exampleCategory);
    todoRepository.saveAndFlush(todo2);

    given()
      .when()
      .get("/todos")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body("$", hasSize(2))
      .body("title", hasItems("Test todo1", "Test todo2"))
      .body(
        "description",
        hasItems("Test todo1 description", "Test todo2 description")
      )
      .body("isCompleted", hasItems(false, false))
      .body("category", hasItem("Example"))
      .body(matchesJsonSchemaInClasspath("schemas/todo-list-schema.json"));
  }

  @Test
  public void getById_ValidIdForExistingTodo_Success() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    Todo exampleTodo = new Todo();
    exampleTodo.setTitle("Test todo1");
    exampleTodo.setDescription("Test todo1 description");
    exampleTodo.setDueDate(LocalDate.of(2026, 12, 12));
    exampleTodo.setIsCompleted(false);
    exampleTodo.setCategory(exampleCategory);
    todoRepository.saveAndFlush(exampleTodo);

    given()
      .when()
      .get("/todos/" + exampleTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.OK.value())
      .body("title", equalTo(exampleTodo.getTitle()))
      .body("description", equalTo(exampleTodo.getDescription()))
      .body("dueDate", equalTo("2026-12-12"))
      .body("isCompleted", equalTo(false))
      .body("category", equalTo("Example"))
      .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
  }

  @Test
  public void getById_InvalidId_BadRequest() {
    given()
      .when()
      .get("/todos/hello")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void getById_IdNotInDB_NotFound() {
    long id = 1L;
    given()
      .when()
      .get("/todos/" + id)
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NOT_FOUND.value())
      .body("error", equalTo("Not Found"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  //Tests for Creating Todos

  @Test
  public void createTodo_validDTO_Created() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    CreateTodoRequest validDTO = new CreateTodoRequest();
    validDTO.setTitle("valid title");
    validDTO.setDescription("valid description");
    validDTO.setDueDate(LocalDate.of(2026, 12, 12));
    validDTO.setCategoryId(exampleCategory.getId());

    given()
      .contentType(ContentType.JSON)
      .body(validDTO)
      .when()
      .post("/todos")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.CREATED.value())
      .body("title", equalTo("valid title"))
      .body("description", equalTo("valid description"))
      .body("dueDate", equalTo("2026-12-12"))
      .body("category", equalTo("Example"))
      .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
  }

  @Test
  public void createTodo_invalidDTO_BadRequest() {
    HashMap<String, String> data = new HashMap<>();
    data.put("title", "");
    data.put("description", "");

    given()
      .contentType(ContentType.JSON)
      .body(data)
      .when()
      .post("/todos")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("details.dueDate", hasItem("must not be null"))
      .body("details.description", hasItem("must not be blank"))
      .body("details.title", hasItem("must not be blank"))
      .body("details.categoryId", hasItem("must not be null"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void createTodo_missingBody_BadRequest() {
    given()
      .contentType(ContentType.JSON)
      .when()
      .post("/todos")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body("message", not(emptyString()))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void createTodo_categoryNotInDB_UnprocessableContent() {
    CreateTodoRequest validDTO = new CreateTodoRequest();
    validDTO.setTitle("valid title");
    validDTO.setDescription("valid description");
    validDTO.setDueDate(LocalDate.of(2026, 12, 12));
    validDTO.setCategoryId(1L);

    given()
      .contentType(ContentType.JSON)
      .body(validDTO)
      .when()
      .post("/todos")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.UNPROCESSABLE_CONTENT.value())
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void createTodo_invalidCategory_BadRequest() {
    CreateTodoRequest validDTO = new CreateTodoRequest();
    validDTO.setTitle("valid title");
    validDTO.setDescription("valid description");
    validDTO.setDueDate(LocalDate.of(2026, 12, 12));
    validDTO.setCategoryId(null);

    given()
      .contentType(ContentType.JSON)
      .body(validDTO)
      .when()
      .post("/todos")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("details.categoryId", hasItem("must not be null"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  // Tests for Updating Todos
  @Test
  public void updateTodo_validDTO_Updated() {
    Category exampleCategory1 = new Category();
    exampleCategory1.setName("Example1");
    categoryRepository.saveAndFlush(exampleCategory1);

    Category exampleCategory2 = new Category();
    exampleCategory2.setName("Example2");
    categoryRepository.saveAndFlush(exampleCategory2);

    Todo existingTodo = new Todo();
    existingTodo.setTitle("Test todo");
    existingTodo.setDescription("Test todo description");
    existingTodo.setDueDate(LocalDate.of(2026, 12, 12));
    existingTodo.setIsCompleted(false);
    existingTodo.setCategory(exampleCategory1);
    todoRepository.saveAndFlush(existingTodo);

    UpdateTodoRequest validDTO = new UpdateTodoRequest();
    validDTO.setTitle("updated title");
    validDTO.setDescription("updated description");
    validDTO.setDueDate(LocalDate.of(2027, 11, 13));
    validDTO.setIsCompleted(true);
    validDTO.setCategoryId(exampleCategory2.getId());

    given()
      .contentType(ContentType.JSON)
      .body(validDTO)
      .when()
      .patch("/todos/" + existingTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.OK.value())
      .body("title", equalTo("updated title"))
      .body("description", equalTo("updated description"))
      .body("dueDate", equalTo("2027-11-13"))
      .body("isCompleted", equalTo(true))
      .body("category", equalTo("Example2"))
      .body(matchesJsonSchemaInClasspath("schemas/todo-schema.json"));
  }

  @Test
  public void updateTodo_unparseableDTO_BadRequest() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    Todo existingTodo = new Todo();
    existingTodo.setTitle("Test todo");
    existingTodo.setDescription("Test todo description");
    existingTodo.setDueDate(LocalDate.of(2026, 12, 12));
    existingTodo.setIsCompleted(false);
    existingTodo.setCategory(exampleCategory);
    todoRepository.saveAndFlush(existingTodo);

    HashMap<String, String> data = new HashMap<>();
    data.put("title", "");
    data.put("description", "");
    data.put("dueDate", "invalid dueDate");
    data.put("isCompleted", "invalid isCompleted");
    data.put("categoryId", "invalid categoryId");

    given()
      .contentType(ContentType.JSON)
      .body(data)
      .when()
      .patch("/todos/" + existingTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void updateTodo_invalidDTO_BadRequest() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    Todo existingTodo = new Todo();
    existingTodo.setTitle("Test todo");
    existingTodo.setDescription("Test todo description");
    existingTodo.setDueDate(LocalDate.of(2026, 12, 12));
    existingTodo.setIsCompleted(false);
    existingTodo.setCategory(exampleCategory);
    todoRepository.saveAndFlush(existingTodo);

    HashMap<String, String> data = new HashMap<>();
    data.put("title", "");
    data.put("description", "");

    given()
      .contentType(ContentType.JSON)
      .body(data)
      .when()
      .patch("/todos/" + existingTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("details.description", hasItem("Description cannot be empty"))
      .body("details.title", hasItem("Title cannot be empty"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  // Tests for Deleting Todos
  @Test
  public void deleteTodo_todoInDB_Deleted() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    Todo existingTodo = new Todo();
    existingTodo.setTitle("Test todo");
    existingTodo.setDescription("Test todo description");
    existingTodo.setDueDate(LocalDate.of(2026, 12, 12));
    existingTodo.setIsCompleted(false);
    existingTodo.setCategory(exampleCategory);
    todoRepository.saveAndFlush(existingTodo);

    given()
      .when()
      .delete("/todos/" + existingTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NO_CONTENT.value());

    // Now verifying that the todo is no longer in the repository
    given()
      .when()
      .get("/todos/" + existingTodo.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NOT_FOUND.value())
      .body(
        "message",
        equalTo("Could not find Todo with id = " + existingTodo.getId())
      )
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void deleteTodo_todoNotInDB_NotFound() {
    given()
      .when()
      .delete("/todos/1")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NOT_FOUND.value())
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }
}
