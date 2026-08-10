package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.dtos.CategoryResponse;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.List;
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
  public List<Category> getAllCategories() {
    return this.categoryService.findAll();
  }

  @PostMapping
  public Category createCategory(@RequestBody CreateCategoryRequest data) {
    Category createdCategory = this.categoryService.create(data);
    return createdCategory;
  }

  @PatchMapping("/{id}")
  public CategoryResponse updateCategory(
    @PathVariable Long id,
    @RequestBody UpdateCategoryRequest data
  ) throws Exception {
    Category result = this.categoryService
      .updateById(id, data)
      .orElseThrow(() ->
        new Exception("Could not find category with id = " + id)
      );
    return CategoryResponse.of(result);
  }

  @DeleteMapping("/{id}")
  public void deleteCategoryById(@PathVariable Long id) throws Exception {
    boolean isDeleted = this.categoryService.deleteById(id);
    if (!isDeleted) {
      throw new Exception("Could not find category with id of " + id);
    }
  }
}
