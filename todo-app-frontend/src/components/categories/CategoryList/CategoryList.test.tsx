import { describe, it, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import type { CategoryResponse } from "../../../schemas/category-schemas";
import { vi } from "vitest";
import "@testing-library/jest-dom/vitest";
import userEvent from "@testing-library/user-event";
import CategoryList from "./CategoryList";

const mockOnCategoryCreated = vi.fn();
const mockCategories: CategoryResponse[] = [
  {
    id: 1,
    name: "mockCategory1",
  },
  {
    id: 2,
    name: "mockCategory2",
  },
];

describe("CategoryList", () => {
  it("Should display the correct number of categories in the list", () => {
    render(
      <CategoryList
        categories={mockCategories}
        onCategoryCreated={mockOnCategoryCreated}
      />,
    );

    const items = screen.getAllByTestId("categoryCard");
    expect(items).toHaveLength(mockCategories.length);
  });

  it("Should correctly render all categories passed in", () => {
    render(
      <CategoryList
        categories={mockCategories}
        onCategoryCreated={mockOnCategoryCreated}
      />,
    );

    const categoryItems = screen.getAllByTestId("categoryCard");
    expect(categoryItems[0]).toHaveTextContent("mockCategory1");
    expect(categoryItems[1]).toHaveTextContent("mockCategory2");
  });

  it("Should display a categoryForm when adding a new category", async () => {
    const user = userEvent.setup();
    render(
      <CategoryList
        categories={mockCategories}
        onCategoryCreated={mockOnCategoryCreated}
      />,
    );

    const addButton = screen.getByRole("button", { name: /add category/i });
    await user.click(addButton);

    expect(screen.getByPlaceholderText("Category name")).toBeInTheDocument();
  });
});
