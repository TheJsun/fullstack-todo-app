package com.jason.todoapp.fullstack_todo_app.category;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
public class CategoryEndToEndTest {

  @LocalServerPort
  private int port;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  public void setup() {
    RestAssured.port = this.port;
  }

  // Tests for Getting Categories
  @Test
  public void getAllCategories_categoriesInDb_ReturnsOkAndArrayOfTodos() {
    Category exampleCategory = new Category();
    exampleCategory.setName("Example");
    categoryRepository.saveAndFlush(exampleCategory);

    given()
      .when()
      .get("/categories")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.OK.value())
      .body("$", hasSize(1))
      .body("name", hasItem("Example"))
      .body(matchesJsonSchemaInClasspath("schemas/category-list-schema.json"));
  }

  @Test
  public void getAllCategories_noCategoriesInDb_ReturnOkAndEmptyArray() {
    given()
      .when()
      .get("/categories")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.OK.value())
      .body("$", hasSize(0));
  }

  // Tests for Creating Categories
  @Test
  public void createCategory_validDTO_Created() {
    CreateCategoryRequest dto = new CreateCategoryRequest();
    dto.setName("example");

    given()
      .contentType(ContentType.JSON)
      .body(dto)
      .when()
      .post("/categories")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.CREATED.value())
      .body("name", equalTo("example"))
      .body(matchesJsonSchemaInClasspath("schemas/category-schema.json"));
  }

  @Test
  public void createCategory_invalidDTO_BadRequest() {
    HashMap<String, String> data = new HashMap<>();
    data.put("name", "");

    given()
      .contentType(ContentType.JSON)
      .body(data)
      .when()
      .post("/categories")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body("details.name", hasItem("must not be blank"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void createCategory_missingBody_BadRequest() {
    given()
      .contentType(ContentType.JSON)
      .when()
      .post("/categories")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body("message", not(emptyString()))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  // Tests for Updating Categories
  @Test
  public void updateCategory_validDTO_Updated() {
    Category existingCategory = new Category();
    existingCategory.setName("old name");
    categoryRepository.saveAndFlush(existingCategory);

    UpdateCategoryRequest dto = new UpdateCategoryRequest();
    dto.setName("new name");

    given()
      .contentType(ContentType.JSON)
      .body(dto)
      .when()
      .patch("/categories/" + existingCategory.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.OK.value())
      .body("name", equalTo("new name"))
      .body(matchesJsonSchemaInClasspath("schemas/category-schema.json"));
  }

  @Test
  public void updateCategory_invalidDTO_BadRequest() {
    HashMap<String, String> data = new HashMap<>();
    data.put("name", "");

    Category exampleCategory = new Category();
    exampleCategory.setName("example");
    categoryRepository.saveAndFlush(exampleCategory);

    given()
      .contentType(ContentType.JSON)
      .body(data)
      .when()
      .patch("/categories/" + exampleCategory.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body("details.name", hasItem("name cannot be empty"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void updateCategory_missingBody_BadRequest() {
    Category exampleCategory = new Category();
    exampleCategory.setName("example");
    categoryRepository.saveAndFlush(exampleCategory);

    given()
      .contentType(ContentType.JSON)
      .when()
      .patch("/categories/" + exampleCategory.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.BAD_REQUEST.value())
      .body("error", equalTo("Bad Request"))
      .body("message", not(emptyString()))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  @Test
  public void updateCategory_categoryNotInDB_NotFoundException() {
    UpdateCategoryRequest dto = new UpdateCategoryRequest();
    dto.setName("new name");

    given()
      .contentType(ContentType.JSON)
      .body(dto)
      .when()
      .patch("/categories/1")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NOT_FOUND.value())
      .body("error", equalTo("Not Found"))
      .body("message", equalTo("Could not find category with id = 1"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }

  // Tests for Deleting Categories
  @Test
  public void deleteCategory_categoryInDB_Deleted() {
    Category exampleCategory = new Category();
    exampleCategory.setName("example");
    categoryRepository.saveAndFlush(exampleCategory);

    given()
      .when()
      .delete("/categories/" + exampleCategory.getId())
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  public void deleteCategory_categoryNotInDB_NotFound() {
    given()
      .when()
      .delete("/categories/1")
      .then()
      .log()
      .body()
      .statusCode(HttpStatus.NOT_FOUND.value())
      .body("error", equalTo("Not Found"))
      .body("message", equalTo("Could not find category with id = 1"))
      .body(matchesJsonSchemaInClasspath("schemas/api-error-schema.json"));
  }
}
