package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository repo;
  private final ModelMapper mapper;

  public CategoryService(CategoryRepository repo, ModelMapper mapper) {
    this.repo = repo;
    this.mapper = mapper;
  }

  public Optional<Category> findById(Long id) {
    return this.repo.findById(id);
  }

  public Optional<Category> findByName(String name) {
    return this.repo.findByName(name);
  }

  public List<Category> findAll() {
    return this.repo.findAll();
  }

  public Category create(CreateCategoryRequest data) {
    Category createdCategory = new Category();
    this.mapper.map(data, createdCategory);
    this.repo.saveAndFlush(createdCategory);
    return createdCategory;
  }

  public Optional<Category> updateById(Long id, UpdateCategoryRequest data) {
    Optional<Category> result = this.findById(id);
    if (result.isEmpty()) {
      return result;
    }
    Category foundCategory = result.get();
    this.mapper.map(data, foundCategory);
    this.repo.saveAndFlush(foundCategory);

    return Optional.of(foundCategory);
  }

  public boolean deleteById(Long id) {
    Optional<Category> result = this.findById(id);
    if (result.isEmpty()) {
      return false;
    }
    this.repo.deleteById(id);
    return true;
  }
}
