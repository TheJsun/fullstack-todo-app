package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import jakarta.persistence.EntityNotFoundException;
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

  public Category findById(Long id) {
    return this.repo
      .findById(id)
      .orElseThrow(() ->
        new EntityNotFoundException("Could not find category with id = " + id)
      );
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

  public Category updateById(Long id, UpdateCategoryRequest data) {
    Category result = this.findById(id);

    this.mapper.map(data, result);
    this.repo.saveAndFlush(result);

    return result;
  }

  public void deleteById(Long id) {
    Category result = this.findById(id);
    this.repo.delete(result);
  }
}
