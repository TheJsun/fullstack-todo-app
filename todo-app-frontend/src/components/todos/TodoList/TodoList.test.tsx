import TodoList from "./TodoList";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import type { CategoryResponse } from "../../../schemas/category-schemas";
import { vi } from "vitest";
import "@testing-library/jest-dom/vitest";

const mockOnDelete = vi.fn();
const mockOnToggleComplete = vi.fn();
const mockOnTodoUpdated = vi.fn();

const mockCategory: CategoryResponse = {
  id: 1,
  name: "mockCategory",
};

const mockCategories: CategoryResponse[] = [mockCategory];

const mockTodos: TodoResponse[] = [
  {
    id: 1,
    title: "Buy milk",
    description: "Go to Coles Friday afternoon",
    dueDate: "12-12-2026",
    createdAt: "12-12-2026",
    isCompleted: false,
    category: mockCategory,
  },
  {
    id: 2,
    title: "Study",
    description: "Prepare for Maths test next week",
    dueDate: "12-12-2026",
    createdAt: "12-12-2026",
    isCompleted: false,
    category: mockCategory,
  },
];

describe("TodoList", () => {
  it("Should display the right number of todos in the list", () => {
    render(
      <TodoList
        todos={mockTodos}
        categories={mockCategories}
        onDelete={mockOnDelete}
        onTodoUpdated={mockOnTodoUpdated}
        onToggleComplete={mockOnToggleComplete}
      />,
    );
    expect(screen.getByText("Buy Milk")).toBeInTheDocument();
  });
});
