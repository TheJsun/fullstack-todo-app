import TodoList from "./TodoList";
import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import type { CategoryResponse } from "../../../schemas/category-schemas";
import { vi } from "vitest";
import "@testing-library/jest-dom/vitest";
import userEvent from "@testing-library/user-event";

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
    const items = screen.getAllByRole("article");
    expect(items).toHaveLength(mockTodos.length);
  });

  it("Should correctly render all jokes passed in", () => {
    render(
      <TodoList
        todos={mockTodos}
        categories={mockCategories}
        onDelete={mockOnDelete}
        onTodoUpdated={mockOnTodoUpdated}
        onToggleComplete={mockOnToggleComplete}
      />,
    );
    const todoItems = screen.getAllByRole("article");
    expect(todoItems[0]).toHaveTextContent(mockTodos[0].title);
    expect(todoItems[0]).toHaveTextContent(mockTodos[0].description);
    expect(todoItems[0]).toHaveTextContent(mockTodos[0].dueDate);
    expect(todoItems[0]).toHaveTextContent(mockTodos[0].category.name);

    expect(todoItems[1]).toHaveTextContent(mockTodos[1].title);
    expect(todoItems[1]).toHaveTextContent(mockTodos[1].description);
    expect(todoItems[1]).toHaveTextContent(mockTodos[1].dueDate);
    expect(todoItems[1]).toHaveTextContent(mockTodos[1].category.name);
  });

  it("Should display an empty message when there are no todos", () => {
    render(
      <TodoList
        todos={[]}
        categories={mockCategories}
        onDelete={mockOnDelete}
        onTodoUpdated={mockOnTodoUpdated}
        onToggleComplete={mockOnToggleComplete}
      />,
    );
    expect(screen.getByTestId("emptyMessage")).toBeInTheDocument();
  });

  it("Should not display an empty message when there are todos", () => {
    render(
      <TodoList
        todos={mockTodos}
        categories={mockCategories}
        onDelete={mockOnDelete}
        onTodoUpdated={mockOnTodoUpdated}
        onToggleComplete={mockOnToggleComplete}
      />,
    );
    expect(screen.queryByTestId("emptyMessage")).not.toBeInTheDocument();
  });

  it("Should show a todoForm instead of TodoCard when editing", async () => {
    const user = userEvent.setup();
    render(
      <TodoList
        todos={mockTodos}
        categories={mockCategories}
        onDelete={mockOnDelete}
        onTodoUpdated={mockOnTodoUpdated}
        onToggleComplete={mockOnToggleComplete}
      />,
    );

    const editButtons = screen.getAllByRole("button", { name: /edit/i });
    await user.click(editButtons[0]);

    expect(screen.getByDisplayValue(mockTodos[0].title)).toBeInTheDocument();
  });
});
