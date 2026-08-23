import type { CategoryResponse } from "../../../schemas/category-schemas";

interface CategoryListProps {
  categories: CategoryResponse[];
}

export default function CategoryList({ categories }: CategoryListProps) {
  return (
    <>
      <ul>
        {categories.map((c) => (
          <li key={c.id}>{c.name}</li>
        ))}
      </ul>
    </>
  );
}
