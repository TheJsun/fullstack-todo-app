import { useState } from "react";
import type {
  CategoryResponse,
  CreateCategoryRequest,
} from "../../../schemas/category-schemas";
import classes from "./CategoryForm.module.scss";
import { createCategory } from "../../../services/category-services";
import Button from "../../Button/Button";

function toCreateCategoryRequest(
  form: CategoryFormState,
): CreateCategoryRequest {
  return { name: form.name };
}

interface CategoryFormProps {
  onCategoryCreated: (category: CategoryResponse) => void;
  onCancel?: () => void;
}

interface CategoryFormState {
  name: string;
}

export default function CategoryForm({
  onCategoryCreated,
  onCancel,
}: CategoryFormProps) {
  const [categoryFormData, setCategoryFormData] = useState<CategoryFormState>({
    name: "",
  });

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    const requestBody = toCreateCategoryRequest(categoryFormData);
    const createdCategory = await createCategory(requestBody);
    console.log("created category");
    onCategoryCreated(createdCategory);
    setCategoryFormData({ name: "" });
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setCategoryFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <>
      <form className={classes.form} onSubmit={handleSubmit}>
        <input
          className={classes["category-form"]}
          name="name"
          value={categoryFormData.name}
          onChange={handleChange}
          placeholder="Category name"
        />
        <Button type="submit">Add</Button>
        <Button variant="delete" onClick={onCancel}>
          &times;
        </Button>
      </form>
    </>
  );
}
