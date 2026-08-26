import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import type { CategoryResponse } from "../../../schemas/category-schemas";
import { vi } from "vitest";
import userEvent from "@testing-library/user-event";

import "@testing-library/jest-dom/vitest";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import TodoCard from "./TodoCard";

const mockOnDelete = vi.fn();
const mockOnToggleComplete = vi.fn();
const mockOnEdit = vi.fn();

const mockCategory: CategoryResponse = {
  id: 1,
  name: "mockCategory",
};

const mockTodo: TodoResponse = {
  id: 1,
  title: "Buy milk",
  description: "Go to Coles Friday afternoon",
  dueDate: "12-12-2026",
  createdAt: "12-12-2026",
  isCompleted: false,
  category: mockCategory,
};

describe("TodoCard", () => {
  it("Should correctly render the todo attributes and a checkbox, edit and delete button", () => {
    render(
      <TodoCard
        todo={mockTodo}
        onDelete={mockOnDelete}
        onEdit={mockOnEdit}
        onToggleComplete={mockOnToggleComplete}
      />,
    );
    const todoCard = screen.getByRole("article");
    expect(todoCard).toHaveTextContent(mockTodo.title);
    expect(todoCard).toHaveTextContent(mockTodo.description);
    expect(todoCard).toHaveTextContent(mockTodo.dueDate);
    expect(screen.getByRole("checkbox")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /edit/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: "×" })).toBeInTheDocument();
  });
  it("shows the checkbox as unchecked when isCompleted is false", () => {
    render(
      <TodoCard
        todo={mockTodo}
        onDelete={mockOnDelete}
        onToggleComplete={mockOnToggleComplete}
        onEdit={mockOnEdit}
      />,
    );

    expect(screen.getByRole("checkbox")).not.toBeChecked();
  });
  it("shows the checkbox as checked when isCompleted is true", () => {
    render(
      <TodoCard
        todo={{ ...mockTodo, isCompleted: true }}
        onDelete={mockOnDelete}
        onToggleComplete={mockOnToggleComplete}
        onEdit={mockOnEdit}
      />,
    );

    expect(screen.getByRole("checkbox")).toBeChecked();
  });

  it("calls onToggleComplete with the todo's id when the checkbox is clicked", async () => {
    const user = userEvent.setup();
    render(
      <TodoCard
        todo={mockTodo}
        onDelete={mockOnDelete}
        onToggleComplete={mockOnToggleComplete}
        onEdit={mockOnEdit}
      />,
    );

    await user.click(screen.getByRole("checkbox"));
    expect(mockOnToggleComplete).toHaveBeenCalledWith(mockTodo.id);
  });
  it("calls onEdit with the full todo when the edit button is clicked", async () => {
    const user = userEvent.setup();
    render(
      <TodoCard
        todo={mockTodo}
        onDelete={mockOnDelete}
        onToggleComplete={mockOnToggleComplete}
        onEdit={mockOnEdit}
      />,
    );

    await user.click(screen.getByRole("button", { name: /edit/i }));
    expect(mockOnEdit).toHaveBeenCalledWith(mockTodo);
  });
});
