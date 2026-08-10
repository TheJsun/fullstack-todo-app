package com.jason.todoapp.fullstack_todo_app.categories.dtos;

import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.List;

public record CategoryResponse(Long id, String name) {
  public static CategoryResponse of(Category category) {
    return new CategoryResponse(category.getId(), category.getName());
  }

  public static List<CategoryResponse> of(List<Category> categories) {
    return categories
      .stream()
      .map(c -> CategoryResponse.of(c))
      .toList();
  }
}
