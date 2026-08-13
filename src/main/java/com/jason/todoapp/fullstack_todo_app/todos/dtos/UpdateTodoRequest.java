package com.jason.todoapp.fullstack_todo_app.todos.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class UpdateTodoRequest {

  @Pattern(regexp = ".*\\S.*", message = "Title cannot be empty")
  private String title;

  @Pattern(regexp = ".*\\S.*", message = "Description cannot be empty")
  private String description;

  private LocalDate dueDate;

  private boolean isCompleted;

  @Min(1)
  private Long categoryId;

  public UpdateTodoRequest() {}

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public boolean getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
