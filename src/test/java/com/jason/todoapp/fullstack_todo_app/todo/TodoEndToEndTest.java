package com.jason.todoapp.fullstack_todo_app.todo;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.todos.TodoRepository;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
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

  //Get Tests

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

  //Create Tests

  @Test
  public void createTodo_validDTO_Created() {
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
      .statusCode(HttpStatus.BAD_REQUEST.value());
  }
}
