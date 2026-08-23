import type { CategoryResponse } from "../../../schemas/category-schemas";
import { useState } from "react";
import CategoryCard from "../CategoryCard/CategoryCard";
import CategoryForm from "../CategoryForm/CategoryForm";
import classes from "./CategoryList.module.scss";

interface CategoryListProps {
  categories: CategoryResponse[];
  onCategoryCreated: (category: CategoryResponse) => void;
}

export default function CategoryList({
  categories,
  onCategoryCreated,
}: CategoryListProps) {
  const [showCategoryForm, setShowCategoryForm] = useState(false);

  const onToggleCategoryForm = () => {
    setShowCategoryForm(!showCategoryForm);
  };

  const onSubmitCategory = (category: CategoryResponse) => {
    onCategoryCreated(category);
    setShowCategoryForm(false);
  };

  const onCancel = () => {
    setShowCategoryForm(false);
  };

  return (
    <main className={classes.categories}>
      <div className={classes.heading}>
        <p>CATEGORIES</p>
        <button type="button" onClick={onToggleCategoryForm}>
          + New
        </button>
      </div>
      {showCategoryForm && (
        <div>
          <CategoryForm
            onCategoryCreated={onSubmitCategory}
            onCancel={onCancel}
          />
        </div>
      )}

      <ul>
        {categories.map((c) => (
          <li key={c.id}>
            <CategoryCard category={c} />
          </li>
        ))}
      </ul>
    </main>
  );
}
