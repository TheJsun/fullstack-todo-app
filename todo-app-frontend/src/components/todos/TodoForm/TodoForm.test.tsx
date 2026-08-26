import { describe, it, expect } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import type { TodoResponse } from "../../../schemas/todo-schemas";
import type { CategoryResponse } from "../../../schemas/category-schemas";
import { vi } from "vitest";
import TodoForm from "./TodoForm";
import { createTodo, updateTodo } from "../../../services/todo-services";

vi.mock("../../../services/todo-services", () => ({
  createTodo: vi.fn(),
  updateTodo: vi.fn(),
}));

const mockCategory: CategoryResponse = { id: 1, name: "Chores" };
const mockCategories: CategoryResponse[] = [mockCategory];

const mockExistingTodo: TodoResponse = {
  id: 1,
  title: "Buy milk",
  description: "Go to Coles",
  dueDate: "2026-12-12",
  createdAt: "2026-12-01",
  isCompleted: false,
  category: mockCategory,
};

const mockOnTodoCreated = vi.fn();
const mockOnTodoUpdated = vi.fn();

describe("TodoForm", () => {
  it("Should render empty fields and an 'Add Task' button", () => {
    render(<TodoForm categories={mockCategories} />);

    expect(screen.getByPlaceholderText("Task name...")).toHaveValue("");
    expect(
      screen.getByPlaceholderText("Add a short description..."),
    ).toHaveValue("");
    expect(
      screen.getByRole("button", { name: /add task/i }),
    ).toBeInTheDocument();
  });

  it("Should call createTodo and onTodoCreated with form data on submit", async () => {
    const user = userEvent.setup();

    const createdTodo: TodoResponse = {
      ...mockExistingTodo,
      id: 2,
      title: "Walk dog",
    };
    vi.mocked(createTodo).mockResolvedValue(createdTodo);

    render(
      <TodoForm
        categories={mockCategories}
        onTodoCreated={mockOnTodoCreated}
      />,
    );

    await user.type(screen.getByPlaceholderText("Task name..."), "Walk dog");
    await user.selectOptions(screen.getByRole("combobox"), "1");
    await user.click(screen.getByRole("button", { name: /add task/i }));

    await waitFor(() => {
      expect(createTodo).toHaveBeenCalledWith({
        title: "Walk dog",
        description: "",
        dueDate: "",
        categoryId: 1,
      });
    });
    expect(mockOnTodoCreated).toHaveBeenCalledWith(createdTodo);
  });

  it("Should reset the form after creating a todo", async () => {
    const user = userEvent.setup();
    vi.mocked(createTodo).mockResolvedValue(mockExistingTodo);

    render(
      <TodoForm
        categories={mockCategories}
        onTodoCreated={mockOnTodoCreated}
      />,
    );
    const titleInput = screen.getByPlaceholderText("Task name...");
    await user.type(titleInput, "Walk dog");
    await user.selectOptions(screen.getByRole("combobox"), "1");
    await user.click(screen.getByRole("button", { name: /add task/i }));

    await waitFor(() => {
      expect(titleInput).toHaveValue("");
    });
  });

  describe("editing todoform", () => {
    it("Should automatically fill fields with existingTodo data", () => {
      render(
        <TodoForm
          categories={mockCategories}
          existingTodo={mockExistingTodo}
        />,
      );

      expect(screen.getByDisplayValue("Buy milk")).toBeInTheDocument();
      expect(screen.getByDisplayValue("Go to Coles")).toBeInTheDocument();
    });

    it("Should display a save button instead of a cancel button", () => {
      render(
        <TodoForm
          categories={mockCategories}
          existingTodo={mockExistingTodo}
        />,
      );
      expect(screen.getByText("Save")).toBeInTheDocument();
    });

    it("Should call updateTodo and onTodoUpdated on submit without resetting the form", async () => {
      const user = userEvent.setup();
      const updatedTodo: TodoResponse = {
        ...mockExistingTodo,
        title: "Buy oat milk",
      };
      vi.mocked(updateTodo).mockResolvedValue(updatedTodo);

      render(
        <TodoForm
          categories={mockCategories}
          existingTodo={mockExistingTodo}
          onTodoUpdated={mockOnTodoUpdated}
        />,
      );

      const titleInput = screen.getByDisplayValue("Buy milk");
      await user.clear(titleInput);
      await user.type(titleInput, "Buy oat milk");
      await user.click(screen.getByRole("button", { name: /save/i }));

      await waitFor(() => {
        expect(updateTodo).toHaveBeenCalledWith(mockExistingTodo.id, {
          title: "Buy oat milk",
          description: "Go to Coles",
          dueDate: "2026-12-12",
          categoryId: 1,
        });
      });
      expect(mockOnTodoUpdated).toHaveBeenCalledWith(updatedTodo);
    });
  });
});
