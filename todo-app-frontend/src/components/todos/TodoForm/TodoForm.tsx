import type { CategoryResponse } from "../../../schemas/category-schemas";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import classes from "./TodoForm.module.scss";
import React, { useState } from "react";

export default function TodoForm() {
  const [todos, setTodos] = useState<TodoResponse[]>([]);
  const [categories, setCategories] = useState<CategoryResponse[]>([]);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    dueDate: "",
    category: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  return (
    <>
      <form className={classes.TodoForm} onSubmit={handleSubmit}>
        <label>Todo Name</label>
        <input name="title" value={formData.title} onChange={handleChange} />

        <label>Todo Description</label>
        <input
          name="description"
          value={formData.description}
          onChange={handleChange}
        />

        <label>Todo Duedate</label>
        <input
          type="date"
          name="dueDate"
          value={formData.dueDate}
          onChange={handleChange}
        />
        <label>Todo Category</label>
        <select
          name="category"
          value={formData.category}
          onChange={handleChange}
        >
          {categories.map((c) => (
            <option key={c.id} value={c.name}>
              {c.name}
            </option>
          ))}
        </select>
        <button type="submit">Add Task</button>
      </form>

      <ul>
        {todos.map((u) => (
          <li key={u.id}>{u.title}</li>
        ))}
      </ul>
    </>
  );
}
