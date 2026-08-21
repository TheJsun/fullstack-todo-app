import type { CategoryResponse } from "./category-schemas";

export interface TodoResponse {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  createdAt: string;
  isCompleted: boolean;
  category: CategoryResponse;
}

export interface CreateTodoRequest {
  title: string;
  description: string;
  dueDate: string;
  categoryId: number;
}
