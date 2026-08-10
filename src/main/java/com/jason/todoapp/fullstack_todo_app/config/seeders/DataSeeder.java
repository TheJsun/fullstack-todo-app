package com.jason.todoapp.fullstack_todo_app.config.seeders;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.todos.TodoRepository;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({ "dev" })
public class DataSeeder implements CommandLineRunner {

  private final CategoryRepository categoryRepo;
  private final TodoRepository todoRepo;

  public DataSeeder(CategoryRepository categoryRepo, TodoRepository todoRepo) {
    this.categoryRepo = categoryRepo;
    this.todoRepo = todoRepo;
  }

  @Override
  public void run(String... args) throws Exception {
    String[] categoryNames = { "Work", "Personal", "Extracurricular", "Daily" };

    if (categoryRepo.count() == 0) {
      for (String name : categoryNames) {
        Category newCategory = new Category();
        newCategory.setName(name);
        categoryRepo.save(newCategory);
      }
    }

    if (todoRepo.count() == 0) {
      Category work = categoryRepo
        .findByName("Work")
        .orElseThrow(() -> new RuntimeException("Work category not found"));
      Category personal = categoryRepo
        .findByName("Personal")
        .orElseThrow(() -> new RuntimeException("Personal category not found"));

      Todo todo1 = new Todo();
      todo1.setTitle("Finish backend");
      todo1.setDescription("Wire up CRUD endpoints");
      todo1.setDueDate(LocalDate.of(2026, 8, 20));
      todo1.setIsCompleted(false);
      todo1.setCategory(work);
      todoRepo.save(todo1);

      Todo todo2 = new Todo();
      todo2.setTitle("Buy groceries");
      todo2.setDescription("Milk, eggs, bread");
      todo2.setDueDate(LocalDate.of(2026, 8, 12));
      todo2.setIsCompleted(false);
      todo2.setCategory(personal);
      todoRepo.save(todo2);
    }
  }
}
