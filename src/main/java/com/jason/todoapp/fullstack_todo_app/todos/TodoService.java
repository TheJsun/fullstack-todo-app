package com.jason.todoapp.fullstack_todo_app.todos;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.NotFoundException;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.UnprocessableContentException;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

  private final TodoRepository repo;
  private final ModelMapper mapper;
  private final CategoryRepository categoryRepository;

  public TodoService(
    TodoRepository repo,
    ModelMapper mapper,
    CategoryRepository categoryRepository
  ) {
    this.repo = repo;
    this.mapper = mapper;
    this.categoryRepository = categoryRepository;
  }

  public List<Todo> findAll() {
    return this.repo.findAll();
  }

  public Todo findById(Long id) {
    return this.repo
      .findById(id)
      .orElseThrow(() ->
        new NotFoundException("Could not find Todo with id = " + id)
      );
  }

  public Todo create(CreateTodoRequest data) {
    Todo createdTodo = this.mapper.map(data, Todo.class);
    Category foundCategory = resolveCategory(data.getCategoryId());
    createdTodo.setCategory(foundCategory);
    createdTodo.setIsCompleted(false);
    return this.repo.saveAndFlush(createdTodo);
  }

  private Category resolveCategory(Long id) {
    Category categoryResult = this.categoryRepository
      .findById(id)
      .orElseThrow(() ->
        new UnprocessableContentException("No category exists with id = " + id)
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
