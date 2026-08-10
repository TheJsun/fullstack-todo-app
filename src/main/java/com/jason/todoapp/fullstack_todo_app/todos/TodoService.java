package com.jason.todoapp.fullstack_todo_app.todos;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryService;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
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

  public Optional<Todo> findById(Long id) {
    return this.repo.findById(id);
  }

  public Todo create(CreateTodoRequest data) {
    Todo createdTodo = this.mapper.map(data, Todo.class);
    Category foundCategory = resolveCategory(data.getCategoryId());
    createdTodo.setCategory(foundCategory);
    this.repo.saveAndFlush(createdTodo);
    return createdTodo;
  }

  private Category resolveCategory(Long id) {
    Optional<Category> categoryResult = this.categoryService.findById(id);
    if (categoryResult.isEmpty()) {
      throw new Error("no category was found with this id");
    }

    return categoryResult.get();
  }
}
