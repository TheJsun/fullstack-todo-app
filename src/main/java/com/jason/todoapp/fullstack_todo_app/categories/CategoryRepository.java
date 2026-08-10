package com.jason.todoapp.fullstack_todo_app.categories;

import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
}
