package com.jason.todoapp.fullstack_todo_app.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jason.todoapp.fullstack_todo_app.categories.CategoryRepository;
import com.jason.todoapp.fullstack_todo_app.categories.CategoryService;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.CreateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.dtos.UpdateCategoryRequest;
import com.jason.todoapp.fullstack_todo_app.categories.entities.Category;
import com.jason.todoapp.fullstack_todo_app.common.exceptions.NotFoundException;
import com.jason.todoapp.fullstack_todo_app.config.ModelMapperConfiguration;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository repo;

  @Mock
  private ModelMapper mapper;

  @InjectMocks
  private CategoryService categoryService;

  @Test
  public void findAll_returnsListFromRepository() {
    Category category1 = new Category();
    category1.setId(1L);

    when(this.repo.findAll()).thenReturn(List.of(category1));

    List<Category> result = this.categoryService.findAll();

    verify(this.repo).findAll();
    assertEquals(1, result.size());
  }

  @Test
  public void findById_categoryExists_returnsCategory() {
    Category fakeCategory = new Category();
    fakeCategory.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeCategory));

    Category result = this.categoryService.findById(1L);
    assertEquals(fakeCategory, result);
    verify(this.repo).findById(1L);
  }

  @Test
  public void findById_categoryDoesNotExist_throwsNotFoundException() {
    when(this.repo.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () ->
      this.categoryService.findById(1L)
    );
    verify(this.repo).findById(1L);
  }

  @Test
  public void createCategory_savesCategoryInDb() {
    CreateCategoryRequest dto = new CreateCategoryRequest();
    dto.setName("fake name");

    Category fakeCategory = new Category();
    fakeCategory.setName("fake name");

    when(this.mapper.map(dto, Category.class)).thenReturn(fakeCategory);

    this.categoryService.create(dto);
    verify(this.repo).saveAndFlush(fakeCategory);
  }

  @Test
  public void updateById_validDTO_updatesDTOInDb() {
    UpdateCategoryRequest dto = new UpdateCategoryRequest();
    dto.setName("updated name");

    Category fakeCategory = new Category();
    fakeCategory.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeCategory));

    this.categoryService.updateById(1L, dto);
    verify(this.mapper).map(dto, fakeCategory);
    verify(this.repo).saveAndFlush(fakeCategory);
  }

  @Test
  public void updateById_categoryDoesNotExist_throwsNotFoundException() {
    UpdateCategoryRequest dto = new UpdateCategoryRequest();

    when(this.repo.findById(anyLong())).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () ->
      this.categoryService.updateById(1L, dto)
    );
    verify(this.repo, never()).saveAndFlush(any(Category.class));
  }

  @Test
  public void updateById_newName_updatesCategoryNameInDb() {
    ModelMapper realMapper = new ModelMapperConfiguration().modelMapper();
    CategoryService serviceWithRealMapper = new CategoryService(
      this.repo,
      realMapper
    );

    UpdateCategoryRequest dto = new UpdateCategoryRequest();
    dto.setName("new name");

    Category fakeCategory = new Category();
    fakeCategory.setId(1L);
    fakeCategory.setName("old name");

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeCategory));

    Category result = serviceWithRealMapper.updateById(1L, dto);

    assertEquals("new name", result.getName());
  }

  @Test
  public void updateById_nullName_originalNamePreserved() {
    ModelMapper realMapper = new ModelMapperConfiguration().modelMapper();
    CategoryService serviceWithRealMapper = new CategoryService(
      this.repo,
      realMapper
    );

    UpdateCategoryRequest dto = new UpdateCategoryRequest();
    dto.setName(null);

    Category fakeCategory = new Category();
    fakeCategory.setId(1L);
    fakeCategory.setName("old name");

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeCategory));

    Category result = serviceWithRealMapper.updateById(1L, dto);

    assertEquals("old name", result.getName());
  }

  @Test
  public void deleteById_categoryExists_categoryDeletedFromDb() {
    Category fakeCategory = new Category();
    fakeCategory.setId(1L);

    when(this.repo.findById(1L)).thenReturn(Optional.of(fakeCategory));
    this.categoryService.deleteById(1L);

    verify(this.repo).delete(fakeCategory);
  }

  @Test
  public void deleteById_categoryDoesNotExist_throwsNotFoundException() {
    when(this.repo.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () ->
      this.categoryService.deleteById(1L)
    );
    verify(this.repo, never()).delete(any(Category.class));
  }
}
