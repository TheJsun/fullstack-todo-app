import type {
  CreateCategoryRequest,
  CategoryResponse,
} from "../schemas/category-schemas";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const getCategories = async () => {
  const response = await fetch(BACKEND_URL + "/categories");
  if (!response.ok) {
    throw new Error("Failed to fetch categories");
  }
  return (await response.json()) as CategoryResponse[];
};

export const createCategory = async (data: CreateCategoryRequest) => {
  const response = await fetch(BACKEND_URL + "/categories", {
    method: "POST",
    body: JSON.stringify(data),
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    throw new Error("Failed to create new category");
  }
  return (await response.json()) as CategoryResponse;
};

// export const createTodos = async (data: CreateTodoRequest) => {
//   const response = await fetch(BACKEND_URL + "/todos", {
//     method: "POST",
//     body: JSON.stringify(data),
//     headers: { "Content-Type": "application/json" },
//   });
//   if (!response.ok) {
//     throw new Error("Failed to create todo");
//   }
//   return (await response.json()) as TodoResponse;
// };
