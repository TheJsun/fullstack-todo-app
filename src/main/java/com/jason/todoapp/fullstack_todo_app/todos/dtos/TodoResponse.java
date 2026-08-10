package com.jason.todoapp.fullstack_todo_app.todos.dtos;

import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import java.time.LocalDate;
import java.util.List;

public record TodoResponse(
  Long id,
  String title,
  String description,
  LocalDate dueDate,
  LocalDate createdAt,
  Boolean isCompleted,
  String category
) {
  public static TodoResponse of(Todo todo) {
    return new TodoResponse(
      todo.getId(),
      todo.getTitle(),
      todo.getDescription(),
      todo.getDueDate(),
      todo.getCreatedAt(),
      todo.getIsCompleted(),
      todo.getCategory().getName()
    );
  }

  public static List<TodoResponse> of(List<Todo> todos) {
    return todos
      .stream()
      .map(t -> TodoResponse.of(t))
      .toList();
  }
}
