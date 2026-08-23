import type { CategoryResponse } from "../../../schemas/category-schemas";

interface CategoryCardProps {
  category: CategoryResponse;
}

export default function CategoryCard({ category }: CategoryCardProps) {
  return (
    <>
      <p>{category.name}</p>
    </>
  );
}
