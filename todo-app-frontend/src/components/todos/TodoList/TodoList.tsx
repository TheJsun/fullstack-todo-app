import { useState } from "react";

import type { CategoryResponse } from "../../../schemas/category-schemas";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import TodoCard from "../TodoCard/TodoCard";
import TodoForm from "../TodoForm/TodoForm";
import classes from "./TodoList.module.scss";

interface TodoListProps {
  todos: TodoResponse[];
  categories: CategoryResponse[];
  onDelete: (id: number) => void;
  onToggleComplete: (id: number) => void;
  onTodoUpdated: (todo: TodoResponse) => void;
}

export default function TodoList({
  todos,
  categories,
  onDelete,
  onToggleComplete,
  onTodoUpdated,
}: TodoListProps) {
  const [editingId, setEditingId] = useState<number | null>(null);

  return (
    <section className={classes.todoListContainer}>
      <ul>
        {todos.map((todo) =>
          todo.id === editingId ? (
            <TodoForm
              key={todo.id}
              categories={categories}
              existingTodo={todo}
              onTodoUpdated={(updated) => {
                onTodoUpdated(updated);
                setEditingId(null);
              }}
              onCancel={() => setEditingId(null)}
            />
          ) : (
            <TodoCard
              key={todo.id}
              todo={todo}
              onDelete={onDelete}
              onToggleComplete={onToggleComplete}
              onEdit={(t) => setEditingId(t.id)}
            />
          ),
        )}
      </ul>
    </section>
  );
}
