package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository repo;

  public CategoryService(CategoryRepository repo) {
    this.repo = repo;
  }

  public Optional<Category> findById(Long id) {
    return this.repo.findById(id);
  }

  public Optional<Category> findByName(String name) {
    return this.repo.findByName(name);
  }
}
