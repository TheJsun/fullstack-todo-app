package com.jason.todoapp.fullstack_todo_app.categories.dtos;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryRequest {

  @NotBlank
  private String name;

  public CreateCategoryRequest() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
