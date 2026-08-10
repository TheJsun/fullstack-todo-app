package com.jason.todoapp.fullstack_todo_app.categories.dtos;

import jakarta.validation.constraints.Pattern;

public class UpdateCategoryRequest {

  @Pattern(regexp = ".*\\S.*", message = "name cannot be empty")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
