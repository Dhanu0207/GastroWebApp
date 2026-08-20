import React from 'react';
import { Plus, Star, Leaf, Flame, Clock } from 'lucide-react';
import { MenuItem } from '../../types';
import { useCartStore } from '../../store/cartStore';
import { useGroupCartStore } from '../../store/groupCartStore';
import { toast } from 'sonner';

interface FoodCardProps {
  item: MenuItem;
}

export const FoodCard: React.FC<FoodCardProps> = ({ item }) => {
  const { addItem } = useCartStore();
  const { addItemToGroup, isInGroupSession } = useGroupCartStore();

  const handleAddToCart = (e: React.MouseEvent) => {
    e.stopPropagation();
    addItem(item, 1);
    toast.success(`Added ${item.name} to cart!`, {
      description: `₹${item.price} • ${item.categoryName || 'Gourmet Selection'}`,
    });

    if (isInGroupSession) {
      addItemToGroup(item, 1);
    }
  };

  return (
    <div className="group relative flex flex-col justify-between overflow-hidden rounded-3xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl transition-all duration-300 hover:border-white/25 hover:-translate-y-1.5 hover:shadow-2xl hover:shadow-orange-500/10">
      
      {/* Ambient Radial Underglow behind card */}
      <div className="pointer-events-none absolute -top-12 -right-12 h-36 w-36 rounded-full bg-orange-500/15 blur-3xl transition-opacity duration-300 group-hover:bg-orange-500/25" />

      <div>
        {/* Image Container with Badges */}
        <div className="relative h-44 w-full overflow-hidden rounded-2xl bg-slate-900">
          <img
            src={item.imageUrl}
            alt={item.name}
            className="h-full w-full object-cover transition-transform duration-700 ease-out group-hover:scale-110"
            loading="lazy"
          />

          {/* Rating Badge */}
          <div className="absolute top-2.5 right-2.5 flex items-center gap-1 rounded-xl bg-black/60 px-2.5 py-1 text-xs font-bold text-amber-400 backdrop-blur-md border border-white/10 shadow-lg">
            <Star className="h-3 w-3 fill-amber-400" />
            <span>{item.rating || '4.8'}</span>
          </div>

          {/* Dietary Indicator */}
          <div className="absolute top-2.5 left-2.5 flex items-center gap-1 rounded-xl bg-black/60 px-2.5 py-1 text-[11px] font-semibold backdrop-blur-md border border-white/10 shadow-lg">
            {item.isVegetarian ? (
              <span className="flex items-center gap-1 text-emerald-400">
                <Leaf className="h-3 w-3" /> Veg
              </span>
            ) : (
              <span className="flex items-center gap-1 text-rose-400">
                <Flame className="h-3 w-3" /> Non-Veg
              </span>
            )}
          </div>

          {/* Preparation Time */}
          {item.preparationTimeMinutes && (
            <div className="absolute bottom-2.5 left-2.5 flex items-center gap-1 rounded-lg bg-black/50 px-2 py-0.5 text-[10px] text-slate-300 backdrop-blur-sm">
              <Clock className="h-3 w-3" />
              <span>{item.preparationTimeMinutes} mins</span>
            </div>
          )}
        </div>

        {/* Content Section */}
        <div className="mt-4">
          <div className="flex items-baseline justify-between gap-2">
            <h3 className="font-bold text-base text-white tracking-tight group-hover:text-orange-400 transition-colors line-clamp-1">
              {item.name}
            </h3>
          </div>
          <p className="mt-1 text-xs text-slate-400 line-clamp-2 leading-relaxed font-normal">
            {item.description}
          </p>
        </div>
      </div>

      {/* Footer Section */}
      <div className="mt-4 flex items-center justify-between border-t border-white/5 pt-3">
        <div>
          <span className="text-[10px] uppercase font-semibold tracking-wider text-slate-400">Price</span>
          <p className="text-lg font-black text-white font-mono">₹{item.price}</p>
        </div>

        <button
          onClick={handleAddToCart}
          className="flex items-center gap-1.5 rounded-xl bg-gradient-to-r from-orange-500 to-amber-500 px-4 py-2 text-xs font-bold text-white shadow-lg shadow-orange-500/30 transition-all hover:scale-105 hover:brightness-110 active:scale-95 cursor-pointer"
        >
          <Plus className="h-4 w-4" /> Add
        </button>
      </div>
    </div>
  );
};
