import classes from "./App.module.scss";
import { useState } from "react";
import Header from "./components/Header/Header";
import type { TodoResponse } from "./schemas/todo-schemas";
import type { CategoryResponse } from "./schemas/category-schemas";
import TodoList from "./components/todos/TodoList/TodoList";
import { deleteTodo, updateTodo } from "./services/todo-services";
import TodoCreation from "./components/todos/TodoCreation/TodoCreation";

function App() {
  const [todos, setTodos] = useState<TodoResponse[]>([]);
  const [categories, setCategories] = useState<CategoryResponse[]>([]);

  const onTodoCreated = (todo: TodoResponse) => {
    setTodos([...todos, todo]);
  };

  const onCategoryCreated = (category: CategoryResponse) => {
    setCategories([...categories, category]);
  };

  const handleDeleteTodo = async (id: number) => {
    await deleteTodo(id);
    setTodos((prev) => prev.filter((t) => t.id !== id));
  };

  const handleToggleComplete = async (id: number) => {
    const todo = todos.find((t) => t.id === id);
    if (!todo) return;
    const updated = await updateTodo(id, {
      isCompleted: !todo.isCompleted,
    });
    setTodos((prev) => prev.map((t) => (t.id === id ? updated : t)));
  };

  const handleTodoUpdated = (updated: TodoResponse) => {
    setTodos((prev) => prev.map((t) => (t.id === updated.id ? updated : t)));
  };

  return (
    <main className={classes.main}>
      <Header />
      <TodoCreation
        categories={categories}
        onCategoryCreated={onCategoryCreated}
        onTodoCreated={onTodoCreated}
      />
      <TodoList
        todos={todos}
        categories={categories}
        onDelete={handleDeleteTodo}
        onToggleComplete={handleToggleComplete}
        onTodoUpdated={handleTodoUpdated}
      />
    </main>
  );
}

export default App;
