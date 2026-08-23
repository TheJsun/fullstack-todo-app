import type { TodoResponse } from "../../../schemas/todo-schemas";
import classes from "./TodoCard.module.scss";

interface TodoCardProps {
  todo: TodoResponse;
  onDelete: (id: number) => void;
  onToggleComplete: (id: number) => void;
  onEdit?: (todo: TodoResponse) => void;
}

export default function TodoCard({
  todo,
  onDelete,
  onToggleComplete,
  onEdit,
}: TodoCardProps) {
  return (
    <article className={classes.card}>
      <input
        type="checkbox"
        checked={todo.isCompleted}
        onChange={() => onToggleComplete(todo.id)}
      />
      <p>{todo.title}</p>
      <p>{todo.description}</p>
      <p>{todo.category.name}</p>
      <p>{todo.dueDate}</p>

      {onEdit && (
        <button type="button" onClick={() => onEdit(todo)}>
          Edit
        </button>
      )}
      <button type="button" onClick={() => onDelete(todo.id)}>
        Delete
      </button>
    </article>
  );
}
