import { describe, it, expect } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import CategoryForm from "./CategoryForm";
import { createCategory } from "../../../services/category-services";
import userEvent from "@testing-library/user-event";
import type { CategoryResponse } from "../../../schemas/category-schemas";

const mockOnCategoryCreated = vi.fn();
const mockOnCancel = vi.fn();

vi.mock("../../../services/category-services", () => ({
  createCategory: vi.fn(),
}));

describe("CategoryForm", () => {
  it("Should render empty name field and an add and cancel button", () => {
    render(
      <CategoryForm
        onCategoryCreated={mockOnCategoryCreated}
        onCancel={mockOnCancel}
      />,
    );

    expect(screen.getByPlaceholderText("Category name")).toHaveValue("");
    expect(screen.getByRole("button", { name: /add/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /×/i })).toBeInTheDocument();
  });

  it("Should call createCategory and onCategoryCreated with form data on submit", async () => {
    const user = userEvent.setup();

    const createdCategory: CategoryResponse = {
      id: 1,
      name: "Work",
    };

    vi.mocked(createCategory).mockResolvedValue(createdCategory);
    render(
      <CategoryForm
        onCategoryCreated={mockOnCategoryCreated}
        onCancel={mockOnCancel}
      />,
    );
    await user.type(screen.getByPlaceholderText("Category name"), "Work");
    await user.click(screen.getByRole("button", { name: /add/i }));

    await waitFor(() => {
      expect(createCategory).toHaveBeenCalledWith({
        name: "Work",
      });
    });
    expect(mockOnCategoryCreated).toHaveBeenCalledWith(createdCategory);
  });

  it("updates the input as the user types", async () => {
    const user = userEvent.setup();
    render(
      <CategoryForm
        onCategoryCreated={mockOnCategoryCreated}
        onCancel={mockOnCancel}
      />,
    );

    const input = screen.getByPlaceholderText("Category name");
    await user.type(input, "Work");

    expect(input).toHaveValue("Work");
  });

  it("calls onCancel when the cancel button is clicked", async () => {
    const user = userEvent.setup();

    render(
      <CategoryForm
        onCategoryCreated={mockOnCategoryCreated}
        onCancel={mockOnCancel}
      />,
    );

    await user.click(screen.getByText("×"));
    expect(mockOnCancel).toHaveBeenCalledTimes(1);
  });
});
