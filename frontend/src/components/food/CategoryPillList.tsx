import React from 'react';
import { Category } from '../../types';

interface CategoryPillListProps {
  categories: Category[];
  selectedCategory: string;
  onSelectCategory: (categoryName: string) => void;
}

export const CategoryPillList: React.FC<CategoryPillListProps> = ({
  categories,
  selectedCategory,
  onSelectCategory,
}) => {
  return (
    <div className="flex items-center gap-2.5 overflow-x-auto pb-2 pt-1 no-scrollbar">
      {categories.map((cat) => {
        const isSelected = selectedCategory === cat.name;
        return (
          <button
            key={cat.id}
            onClick={() => onSelectCategory(cat.name)}
            className={`flex shrink-0 items-center gap-2 rounded-2xl px-4 py-2.5 text-xs font-semibold backdrop-blur-xl border transition-all duration-200 cursor-pointer ${
              isSelected
                ? 'bg-gradient-to-r from-orange-500/25 to-amber-500/25 border-orange-500/60 text-orange-300 shadow-lg shadow-orange-500/20 scale-105'
                : 'bg-white/[0.04] border-white/10 text-slate-300 hover:bg-white/[0.08] hover:border-white/20 hover:text-white'
            }`}
          >
            {cat.icon && <span className="text-base">{cat.icon}</span>}
            <span>{cat.name}</span>
          </button>
        );
      })}
    </div>
  );
};
