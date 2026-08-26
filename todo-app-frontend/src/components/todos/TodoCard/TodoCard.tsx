import type { TodoResponse } from "../../../schemas/todo-schemas";
import Button from "../../Button/Button";
import classes from "./TodoCard.module.scss";

interface TodoCardProps {
  todo: TodoResponse;
  onDelete: (id: number) => void;
  onToggleComplete: (id: number) => void;
  onEdit: (todo: TodoResponse) => void;
}

export default function TodoCard({
  todo,
  onDelete,
  onToggleComplete,
  onEdit,
}: TodoCardProps) {
  return (
    <article className={classes.todo}>
      <input
        className={classes.todo__checkbox}
        type="checkbox"
        checked={todo.isCompleted}
        onChange={() => onToggleComplete(todo.id)}
      />
      <div className={classes.todo__body}>
        <p className={classes.todo__title}>{todo.title}</p>
        <p className={classes.todo__desc}>{todo.description}</p>
        <div className={classes.todo__meta}>
          <p className={classes.todo__category}>{todo.category.name}</p>
          <p className={classes.todo__dueDate}>{todo.dueDate}</p>
        </div>
      </div>

      <div className={classes.todo__buttons}>
        <Button variant="edit" onClick={() => onEdit(todo)}>
          Edit
        </Button>

        <Button variant="delete" onClick={() => onDelete(todo.id)}>
          &times;
        </Button>
      </div>
    </article>
  );
}
