import type { CategoryResponse } from "../../../schemas/category-schemas";
import type {
  CreateTodoRequest,
  TodoResponse,
  UpdateTodoRequest,
} from "../../../schemas/todo-schemas";
import { createTodo, updateTodo } from "../../../services/todo-services";
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
      console.log(requestBody);
      const createdTodo = await createTodo(requestBody);
      console.log(createdTodo);
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
    <>
      <form className={classes.TodoForm} onSubmit={handleSubmit}>
        <label>Todo Name</label>
        <input
          name="title"
          value={todoFormData.title}
          onChange={handleChange}
        />

        <label>Todo Description</label>
        <input
          name="description"
          value={todoFormData.description}
          onChange={handleChange}
        />

        <label>Todo Duedate</label>
        <input
          type="date"
          name="dueDate"
          value={todoFormData.dueDate}
          onChange={handleChange}
        />
        <label>Todo Category</label>
        <select
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
        <button type="submit">{existingTodo ? "Save" : "Add Task"}</button>
        {onCancel && (
          <button type="button" onClick={onCancel}>
            Cancel
          </button>
        )}
      </form>
    </>
  );
}
