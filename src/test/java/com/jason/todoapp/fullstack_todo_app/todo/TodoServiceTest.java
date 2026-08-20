package com.jason.todoapp.fullstack_todo_app.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.NotFoundException;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.UnprocessableContentException;
import com.jason.todoapp.fullstack_todo_app.todos.TodoRepository;
import com.jason.todoapp.fullstack_todo_app.todos.TodoService;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.CreateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.dtos.UpdateTodoRequest;
import com.jason.todoapp.fullstack_todo_app.todos.entities.Todo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

  @Mock
  private TodoRepository repo;

  @Mock
  private ModelMapper mapper;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private TodoService todoService;

  @Test
  public void findAll_returnsListFromRepository() {
    Todo todo1 = new Todo();
    todo1.setId(1L);

    when(this.repo.findAll()).thenReturn(List.of(todo1));

    List<Todo> result = this.todoService.findAll();

    verify(this.repo).findAll();
    assertEquals(1, result.size());
  }

  @Test
  public void findById_todoExists_returnsTodo() {
    Todo mockTodo = new Todo();
    mockTodo.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(mockTodo));

    Todo result = this.todoService.findById(1L);
    assertEquals(1L, result.getId());
    verify(this.repo).findById(1L);
  }

  @Test
  public void findById_todoDoesNotExist_throwsNotFoundException() {
    when(this.repo.findById(100L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () ->
      this.todoService.findById(100L)
    );
    verify(this.repo).findById(100L);
  }

  @Test
  public void createTodo_whenCategoryExists_savedTodoInDb() {
    Category fakeCategory = new Category();
    fakeCategory.setId(1L);

    CreateTodoRequest dto = new CreateTodoRequest();
    Todo fakeTodo = new Todo();
    dto.setCategoryId(1L);

    when(this.categoryRepository.findById(1L)).thenReturn(
      Optional.of(fakeCategory)
    );
    when(this.mapper.map(dto, Todo.class)).thenReturn(fakeTodo);

    this.todoService.create(dto);

    assertEquals(fakeCategory, fakeTodo.getCategory());
    assertFalse(fakeTodo.getIsCompleted());
    verify(this.repo).saveAndFlush(fakeTodo);
  }

  @Test
  public void createTodo_whenCategoryDoesNotExist_throwsUnprocessableContentException() {
    CreateTodoRequest dto = new CreateTodoRequest();
    dto.setCategoryId(1L);

    Todo fakeTodo = new Todo();

    when(this.mapper.map(dto, Todo.class)).thenReturn(fakeTodo);
    when(this.categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UnprocessableContentException.class, () ->
      this.todoService.create(dto)
    );
    verify(this.repo, never()).saveAndFlush(any(Todo.class));
  }

  @Test
  public void delete_whenTodoExists_deletesFromDb() {
    Todo fakeTodo = new Todo();

    when(this.repo.findById(anyLong())).thenReturn(Optional.of(fakeTodo));

    this.todoService.deleteById(1L);
    verify(this.repo).delete(fakeTodo);
  }

  @Test
  public void delete_whenTodoDoesNotExist_throwsNotFoundException() {
    when(this.repo.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () ->
      this.todoService.deleteById(1L)
    );
    verify(this.repo, never()).delete(any(Todo.class));
  }

  @Test
  public void updateTodoById_noCategoryChange_updatesTodoInDb() {
    UpdateTodoRequest dto = new UpdateTodoRequest();

    Todo fakeTodo = new Todo();
    fakeTodo.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeTodo));

    this.todoService.updateById(1L, dto);

    verify(this.mapper).map(dto, fakeTodo);
    verify(this.repo).saveAndFlush(fakeTodo);
  }

  @Test
  public void updateById_validCategoryChange_updatesTodoInDb() {
    UpdateTodoRequest dto = new UpdateTodoRequest();
    dto.setCategoryId(2L);

    Todo fakeTodo = new Todo();
    fakeTodo.setId(1L);

    Category fakeCategory = new Category();

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeTodo));
    when(this.categoryRepository.findById(2L)).thenReturn(
      Optional.of(fakeCategory)
    );

    this.todoService.updateById(1L, dto);

    verify(this.mapper).map(dto, fakeTodo);
    assertEquals(fakeCategory, fakeTodo.getCategory());
    verify(this.repo).saveAndFlush(fakeTodo);
  }

  @Test
  public void updateById_invalidCategoryChange_throwsUnprocessableContentException() {
    UpdateTodoRequest dto = new UpdateTodoRequest();
    dto.setCategoryId(2L);

    Todo fakeTodo = new Todo();
    fakeTodo.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeTodo));
    when(this.categoryRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(UnprocessableContentException.class, () ->
      this.todoService.updateById(1L, dto)
    );
    verify(this.repo, never()).saveAndFlush(any(Todo.class));
  }

  @Test
  public void updateById_todoDoesNotExist_throwsNotFoundException() {
    UpdateTodoRequest dto = new UpdateTodoRequest();

    when(this.repo.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () ->
      this.todoService.updateById(1L, dto)
    );

    verify(this.repo, never()).saveAndFlush(any(Todo.class));
  }
}
