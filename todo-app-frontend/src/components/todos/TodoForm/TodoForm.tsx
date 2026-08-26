import type { CategoryResponse } from "../../../schemas/category-schemas";
import type {
  CreateTodoRequest,
  TodoResponse,
  UpdateTodoRequest,
} from "../../../schemas/todo-schemas";
import { createTodo, updateTodo } from "../../../services/todo-services";
import Button from "../../Button/Button";
import classes from "./TodoForm.module.scss";
import React, { useState } from "react";

interface TodoFormState {
  title: string;
  description: string;
  dueDate: string;
  categoryId: string;
}

function toCreateTodoRequest(form: TodoFormState): CreateTodoRequest {
  return {
    title: form.title,
    description: form.description,
    dueDate: form.dueDate,
    categoryId: Number(form.categoryId),
  };
}

function toUpdateTodoRequest(form: TodoFormState): UpdateTodoRequest {
  return {
    title: form.title,
    description: form.description,
    dueDate: form.dueDate,
    categoryId: Number(form.categoryId),
  };
}

interface TodoFormProps {
  categories: CategoryResponse[];
  existingTodo?: TodoResponse;
  onTodoCreated?: (todo: TodoResponse) => void;
  onTodoUpdated?: (todo: TodoResponse) => void;
  onCancel?: () => void;
}

export default function TodoForm({
  categories,
  existingTodo,
  onTodoCreated,
  onTodoUpdated,
  onCancel,
}: TodoFormProps) {
  const [todoFormData, setTodoFormData] = useState<TodoFormState>(
    existingTodo
      ? {
          title: existingTodo.title,
          description: existingTodo.description,
          dueDate: existingTodo.dueDate,
          categoryId: existingTodo.category.id.toString(),
        }
      : {
          title: "",
          description: "",
          dueDate: "",
          categoryId: "",
        },
  );

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setTodoFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (existingTodo) {
      const updated = await updateTodo(
        existingTodo.id,
        toUpdateTodoRequest(todoFormData),
      );
      onTodoUpdated?.(updated);
    } else {
      const requestBody = toCreateTodoRequest(todoFormData);
      const createdTodo = await createTodo(requestBody);
      onTodoCreated?.(createdTodo);
      setTodoFormData({
        title: "",
        description: "",
        dueDate: "",
        categoryId: "",
      });
    }
  };

  return (
    <form className={classes.TodoForm} onSubmit={handleSubmit}>
      <div className={classes.TodoForm__row}>
        <input
          className={`${classes.TodoForm__title} ${classes.field}`}
          name="title"
          value={todoFormData.title}
          onChange={handleChange}
          placeholder="Task name..."
        />

        <select
          className={`${classes.TodoForm__category} ${classes.field}`}
          name="categoryId"
          value={todoFormData.categoryId}
          onChange={handleChange}
          required
        >
          <option value="" disabled>
            -- Select a category --
          </option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>

        <input
          className={`${classes.TodoForm__dueDate} ${classes.field}`}
          type="date"
          name="dueDate"
          value={todoFormData.dueDate}
          onChange={handleChange}
        />
      </div>

      <input
        className={`${classes.TodoForm__desc} ${classes.field}`}
        name="description"
        value={todoFormData.description}
        onChange={handleChange}
        placeholder="Add a short description..."
      />

      <div className={classes.buttons}>
        <Button type="submit">{existingTodo ? "Save" : "Add Task"}</Button>
        {onCancel && (
          <Button variant="delete" onClick={onCancel}>
            x
          </Button>
        )}
      </div>
    </form>
  );
}
