import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Star, Clock, MapPin, Phone, Users, ChevronLeft, Sparkles, Utensils } from 'lucide-react';
import { Restaurant, MenuItem } from '../types';
import { restaurantApi, menuApi } from '../api/endpoints';
import { FoodCard } from '../components/food/FoodCard';
import { useGroupCartStore } from '../store/groupCartStore';
import { toast } from 'sonner';

export const RestaurantDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [selectedFilter, setSelectedFilter] = useState<'ALL' | 'VEG' | 'NON_VEG'>('ALL');
  const [loading, setLoading] = useState(true);

  const { createSession } = useGroupCartStore();

  useEffect(() => {
    const fetchDetails = async () => {
      if (!id) return;
      setLoading(true);
      const restData = await restaurantApi.getById(Number(id));
      const itemsData = await menuApi.getItemsByRestaurant(Number(id));
      setRestaurant(restData);
      setMenuItems(itemsData);
      setLoading(false);
    };
    fetchDetails();
  }, [id]);

  const handleStartGroupCart = async () => {
    if (!restaurant) return;
    const code = await createSession(restaurant.id);
    toast.success(`Group Cart Room Created for ${restaurant.name}! Code: ${code}`);
    navigate('/group-cart');
  };

  if (loading || !restaurant) {
    return (
      <div className="flex h-96 items-center justify-center">
        <div className="flex items-center gap-3 text-orange-400 text-sm font-semibold animate-pulse">
          <Sparkles className="h-5 w-5 animate-spin" /> Loading Restaurant Menu...
        </div>
      </div>
    );
  }

  const filteredMenu = menuItems.filter((item) => {
    if (selectedFilter === 'VEG') return item.isVegetarian;
    if (selectedFilter === 'NON_VEG') return !item.isVegetarian;
    return true;
  });

  return (
    <div className="space-y-10 pb-20">
      
      {/* Back Button */}
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-1.5 text-xs font-semibold text-slate-400 hover:text-white transition"
      >
        <ChevronLeft className="h-4 w-4" /> Back to Discovery
      </button>

      {/* Restaurant Header Hero Banner */}
      <div className="relative overflow-hidden rounded-3xl border border-white/15 bg-slate-950/60 p-6 sm:p-8 backdrop-blur-2xl shadow-2xl">
        {/* Glow backdrop */}
        <div className="pointer-events-none absolute -top-20 -right-20 h-64 w-64 rounded-full bg-orange-500/20 blur-3xl" />

        <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div className="flex flex-col sm:flex-row items-start sm:items-center gap-6">
            <img
              src={restaurant.imageUrl}
              alt={restaurant.name}
              className="h-28 w-28 sm:h-36 sm:w-36 rounded-2xl object-cover border border-white/10 shadow-xl"
            />
            <div className="space-y-2">
              <span className="inline-block rounded-lg bg-orange-500/20 border border-orange-500/30 px-2.5 py-0.5 text-xs font-semibold text-orange-300">
                {restaurant.cuisineType}
              </span>
              <h1 className="text-3xl sm:text-4xl font-extrabold text-white tracking-tight">
                {restaurant.name}
              </h1>
              <p className="text-xs text-slate-300 max-w-lg">{restaurant.description}</p>
              
              <div className="flex flex-wrap items-center gap-4 text-xs text-slate-400 pt-1">
                <span className="flex items-center gap-1 font-bold text-amber-400 bg-amber-400/10 border border-amber-400/20 px-2 py-0.5 rounded-md">
                  <Star className="h-3.5 w-3.5 fill-amber-400" /> {restaurant.rating || '4.8'}
                </span>
                <span className="flex items-center gap-1">
                  <Clock className="h-3.5 w-3.5 text-orange-400" /> {restaurant.deliveryTime || '30 mins'}
                </span>
                <span className="flex items-center gap-1">
                  <MapPin className="h-3.5 w-3.5 text-slate-400" /> {restaurant.address}
                </span>
                <span className="flex items-center gap-1">
                  <Phone className="h-3.5 w-3.5 text-slate-400" /> {restaurant.phoneNumber}
                </span>
              </div>
            </div>
          </div>

          {/* Actions */}
          <div className="flex flex-col sm:flex-row gap-3 w-full md:w-auto">
            <button
              onClick={handleStartGroupCart}
              className="flex items-center justify-center gap-2 rounded-2xl border border-emerald-500/40 bg-emerald-500/15 px-5 py-3 text-xs font-bold text-emerald-300 backdrop-blur-md hover:bg-emerald-500/25 active:scale-95 transition-all shadow-lg shadow-emerald-950/40"
            >
              <Users className="h-4 w-4" /> Start Group Order Here
            </button>
          </div>
        </div>
      </div>

      {/* Menu Filters */}
      <div className="flex items-center justify-between border-b border-white/10 pb-4">
        <div className="flex items-center gap-2">
          <Utensils className="h-5 w-5 text-orange-400" />
          <h2 className="text-xl font-bold text-white">Full Menu ({filteredMenu.length})</h2>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => setSelectedFilter('ALL')}
            className={`px-3 py-1.5 rounded-xl text-xs font-semibold border transition ${
              selectedFilter === 'ALL'
                ? 'bg-orange-500/20 border-orange-500 text-orange-300'
                : 'bg-white/5 border-white/10 text-slate-400 hover:text-white'
            }`}
          >
            All Items
          </button>
          <button
            onClick={() => setSelectedFilter('VEG')}
            className={`px-3 py-1.5 rounded-xl text-xs font-semibold border transition ${
              selectedFilter === 'VEG'
                ? 'bg-emerald-500/20 border-emerald-500 text-emerald-300'
                : 'bg-white/5 border-white/10 text-slate-400 hover:text-white'
            }`}
          >
            Veg Only
          </button>
          <button
            onClick={() => setSelectedFilter('NON_VEG')}
            className={`px-3 py-1.5 rounded-xl text-xs font-semibold border transition ${
              selectedFilter === 'NON_VEG'
                ? 'bg-rose-500/20 border-rose-500 text-rose-300'
                : 'bg-white/5 border-white/10 text-slate-400 hover:text-white'
            }`}
          >
            Non-Veg
          </button>
        </div>
      </div>

      {/* Menu Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {filteredMenu.map((item) => (
          <FoodCard key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
};
