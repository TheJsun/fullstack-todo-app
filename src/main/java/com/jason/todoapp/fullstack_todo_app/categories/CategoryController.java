package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.dtos.CategoryResponse;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping()
  public ResponseEntity<List<CategoryResponse>> getAllCategories() {
    return ResponseEntity.ok(
      CategoryResponse.of(this.categoryService.findAll())
    );
  }

  @PostMapping
  public ResponseEntity<CategoryResponse> createCategory(
    @RequestBody CreateCategoryRequest data
  ) {
    Category createdCategory = this.categoryService.create(data);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      CategoryResponse.of(createdCategory)
    );
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CategoryResponse> updateCategory(
    @PathVariable Long id,
    @RequestBody UpdateCategoryRequest data
  ) {
    Category result = this.categoryService.updateById(id, data);
    return ResponseEntity.ok(CategoryResponse.of(result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id)
    throws Exception {
    this.categoryService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
