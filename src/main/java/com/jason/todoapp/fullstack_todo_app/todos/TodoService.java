package com.jason.todoapp.fullstack_todo_app.todos;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryService;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private final TodoRepository repo;
  private final ModelMapper mapper;
  private final CategoryService categoryService;

  public TodoService(
    TodoRepository repo,
    ModelMapper mapper,
    CategoryService categoryService
  ) {
    this.repo = repo;
    this.mapper = mapper;
    this.categoryService = categoryService;
  }

  public List<Todo> findAll() {
    return this.repo.findAll();
  }

  public Todo findById(Long id) {
    return this.repo
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("Could not find Todo with id = " + id)
      );
  }

  public Todo create(CreateTodoRequest data) {
    Todo createdTodo = this.mapper.map(data, Todo.class);
    Category foundCategory = resolveCategory(data.getCategoryId());
    createdTodo.setCategory(foundCategory);
    createdTodo.setIsCompleted(false);
    this.repo.saveAndFlush(createdTodo);
    return createdTodo;
  }

  private Category resolveCategory(Long id) {
    Category categoryResult = this.categoryService
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("no category was found with this id")
      );
    return categoryResult;
  }

  public void deleteById(Long id) {
    Todo result = this.findById(id);
    this.repo.delete(result);
  }

  public Todo updateById(Long id, UpdateTodoRequest data) {
    Todo result = this.findById(id);

    this.mapper.map(data, result);

    if (data.getCategoryId() != null) {
      Category foundCategory = resolveCategory(data.getCategoryId());
      result.setCategory(foundCategory);
    }

    this.repo.saveAndFlush(result);
    return result;
  }
}
