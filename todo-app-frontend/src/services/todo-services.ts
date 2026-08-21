export interface todoResponse {
  id: number;
  title: string;
  description: string;
  dueDate: string;
  createdAt: string;
  isCompleted: boolean;
  category: CategoryResponse;
}

export interface createTodoRequest {
  title: string;
  description: string;
  dueDate: string;
  categoryId: number;
}
