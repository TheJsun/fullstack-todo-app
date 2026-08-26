import type { CategoryResponse } from "../../../schemas/category-schemas";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import CategoryList from "../../categories/CategoryList/CategoryList";
import TodoForm from "../TodoForm/TodoForm";
import classes from "./TodoCreation.module.scss";

interface TodoCreationProps {
  categories: CategoryResponse[];
  onCategoryCreated: (category: CategoryResponse) => void;
  onTodoCreated: (todo: TodoResponse) => void;
}

export default function TodoCreation({
  categories,
  onCategoryCreated,
  onTodoCreated,
}: TodoCreationProps) {
  return (
    <section>
      <h2 className={classes.heading}>Add Task</h2>
      <CategoryList
        categories={categories}
        onCategoryCreated={onCategoryCreated}
      />
      <TodoForm categories={categories} onTodoCreated={onTodoCreated} />
    </section>
  );
}
