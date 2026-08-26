import type { CategoryResponse } from "../../../schemas/category-schemas";
import { useState } from "react";
import CategoryForm from "../CategoryForm/CategoryForm";
import classes from "./CategoryList.module.scss";
import Button from "../../Button/Button";

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
    <>
      {!showCategoryForm && (
        <section className={`${classes["category-list"]}`}>
          {categories.map((c) => (
            <button
              data-testid="categoryCard"
              key={c.id}
              className={classes["category-card"]}
            >
              {c.name}
            </button>
          ))}

          <Button className="right-side" onClick={onToggleCategoryForm}>
            Add Category
          </Button>
        </section>
      )}

      {showCategoryForm && (
        <div className={classes["category-form"]}>
          <CategoryForm
            onCategoryCreated={onSubmitCategory}
            onCancel={onCancel}
          />
        </div>
      )}
    </>
  );
}
