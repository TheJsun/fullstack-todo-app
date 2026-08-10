package com.jason.todoapp.fullstack_todo_app.todos;

import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.TodoResponse;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodosController {

  private final TodoService todoService;

  public TodosController(TodoService todoService) {
    this.todoService = todoService;
  }

  @GetMapping()
  public List<Todo> findAllTodos() {
    return this.todoService.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Todo> getTodoById(@PathVariable Long id) {
    return this.todoService.findById(id);
  }

  @PostMapping()
  public Todo createTodo(@RequestBody CreateTodoRequest data) {
    Todo createdTodo = this.todoService.create(data);
    return createdTodo;
  }

  @PatchMapping("/{id}")
  public TodoResponse updateTodoById(
    @PathVariable Long id,
    @Valid @RequestBody UpdateTodoRequest data
  ) throws Exception {
    Todo result = this.todoService
      .updateById(id, data)
      .orElseThrow(() -> new Exception("Could not find todo with id + " + id));
    return TodoResponse.of(result);
  }

  @DeleteMapping("/{id}")
  public void deleteTodoById(@PathVariable Long id) throws Exception {
    boolean isDeleted = this.todoService.deleteById(id);
    if (!isDeleted) {
      throw new Exception("Could not find todo with id of " + id);
    }
  }
}
