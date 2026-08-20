import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Star, Clock, MapPin, ArrowUpRight } from 'lucide-react';
import { Restaurant } from '../../types';

interface RestaurantCardProps {
  restaurant: Restaurant;
}

export const RestaurantCard: React.FC<RestaurantCardProps> = ({ restaurant }) => {
  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(`/restaurant/${restaurant.id}`)}
      className="group relative cursor-pointer overflow-hidden rounded-3xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl transition-all duration-300 hover:border-white/25 hover:-translate-y-1.5 hover:shadow-2xl hover:shadow-orange-500/10"
    >
      {/* Dynamic Ambient Glow */}
      <div className="pointer-events-none absolute -top-10 -right-10 h-32 w-32 rounded-full bg-amber-500/15 blur-2xl transition-opacity group-hover:bg-orange-500/25" />

      {/* Image Banner */}
      <div className="relative h-48 w-full overflow-hidden rounded-2xl bg-slate-900">
        <img
          src={restaurant.imageUrl}
          alt={restaurant.name}
          className="h-full w-full object-cover transition-transform duration-700 ease-out group-hover:scale-105"
          loading="lazy"
        />

        {/* Rating Badge */}
        <div className="absolute top-3 right-3 flex items-center gap-1 rounded-xl bg-black/60 px-3 py-1 text-xs font-bold text-amber-400 backdrop-blur-md border border-white/10 shadow-lg">
          <Star className="h-3.5 w-3.5 fill-amber-400" />
          <span>{restaurant.rating || '4.8'}</span>
        </div>

        {/* Delivery Time */}
        <div className="absolute bottom-3 left-3 flex items-center gap-1.5 rounded-xl bg-black/60 px-3 py-1 text-xs font-semibold text-white backdrop-blur-md border border-white/10 shadow-lg">
          <Clock className="h-3.5 w-3.5 text-orange-400" />
          <span>{restaurant.deliveryTime || '25-35 mins'}</span>
        </div>
      </div>

      {/* Content */}
      <div className="mt-4">
        <div className="flex items-start justify-between gap-2">
          <h3 className="font-bold text-lg text-white group-hover:text-orange-400 transition-colors line-clamp-1">
            {restaurant.name}
          </h3>
          <div className="flex h-7 w-7 shrink-0 items-center justify-center rounded-lg bg-white/5 text-slate-300 group-hover:bg-orange-500 group-hover:text-white transition-all">
            <ArrowUpRight className="h-4 w-4" />
          </div>
        </div>

        <p className="mt-1 text-xs text-orange-300/80 font-medium">
          {restaurant.cuisineType} • ₹{restaurant.priceForTwo || 800} for two
        </p>

        <p className="mt-2 text-xs text-slate-400 line-clamp-1 flex items-center gap-1">
          <MapPin className="h-3 w-3 shrink-0 text-slate-500" />
          {restaurant.address}
        </p>
      </div>
    </div>
  );
};
