package com.jason.todoapp.fullstack_todo_app.todos;

import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.TodoResponse;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import jakarta.validation.Valid;
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
@RequestMapping("/todos")
public class TodosController {

  private final TodoService todoService;

  public TodosController(TodoService todoService) {
    this.todoService = todoService;
  }

  @GetMapping()
  public ResponseEntity<List<TodoResponse>> findAllTodos() {
    List<Todo> allTodos = this.todoService.findAll();
    return ResponseEntity.ok(TodoResponse.of(allTodos));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id)
    throws Exception {
    Todo result = this.todoService.findById(id);

    return ResponseEntity.ok(TodoResponse.of(result));
  }

  @PostMapping()
  public ResponseEntity<TodoResponse> createTodo(
    @Valid @RequestBody CreateTodoRequest data
  ) {
    Todo createdTodo = this.todoService.create(data);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      TodoResponse.of(createdTodo)
    );
  }

  @PatchMapping("/{id}")
  public ResponseEntity<TodoResponse> updateTodoById(
    @Valid @PathVariable Long id,
    @Valid @RequestBody UpdateTodoRequest data
  ) {
    Todo result = this.todoService.updateById(id, data);
    return ResponseEntity.ok(TodoResponse.of(result));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
    this.todoService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
