import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Sparkles, 
  Users, 
  Flame, 
  ArrowRight, 
  ShieldCheck, 
  Clock, 
  Utensils,
  Key,
  Shield,
  Store,
  UserCheck
} from 'lucide-react';
import { Restaurant, MenuItem, Category } from '../types';
import { restaurantApi, menuApi } from '../api/endpoints';
import { RestaurantCard } from '../components/food/RestaurantCard';
import { FoodCard } from '../components/food/FoodCard';
import { CategoryPillList } from '../components/food/CategoryPillList';
import { useGroupCartStore } from '../store/groupCartStore';
import { useAuthStore } from '../store/authStore';
import { toast } from 'sonner';

export const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [featuredItems, setFeaturedItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [joinCode, setJoinCode] = useState('');
  
  const { createSession, joinSession } = useGroupCartStore();
  const { login } = useAuthStore();

  useEffect(() => {
    const fetchData = async () => {
      const [restRes, itemsRes, catRes] = await Promise.all([
        restaurantApi.getAll(),
        menuApi.getAllFeaturedItems(),
        menuApi.getCategories(),
      ]);
      setRestaurants(restRes);
      setFeaturedItems(itemsRes);
      setCategories(catRes);
    };
    fetchData();
  }, []);

  const handleQuickLogin = async (email: string, roleName: string) => {
    await login(email, 'password123');
    toast.success(`Logged in as ${roleName}! (${email})`);
  };

  const handleJoinGroup = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!joinCode.trim()) return;
    const success = await joinSession(joinCode.trim().toUpperCase());
    if (success) {
      toast.success(`Joined Group Cart Session: ${joinCode.toUpperCase()}!`);
      navigate('/group-cart');
    }
  };

  const handleStartGroup = async () => {
    const code = await createSession(1);
    toast.success(`Group Cart Room Created! Session: ${code}`);
    navigate('/group-cart');
  };

  const filteredItems = selectedCategory === 'All'
    ? featuredItems
    : featuredItems.filter((i) => i.categoryName?.toLowerCase().includes(selectedCategory.toLowerCase()));

  return (
    <div className="space-y-16 pb-16">
      
      {/* ── Quick Demo Credentials Access Strip ───────────────────────────── */}
      <section className="rounded-2xl border border-amber-500/30 bg-amber-950/20 px-4 py-3 backdrop-blur-xl flex flex-col sm:flex-row items-center justify-between gap-3 text-xs">
        <div className="flex items-center gap-2 text-amber-300 font-semibold">
          <Key className="h-4 w-4 shrink-0 text-amber-400" />
          <span>Demo Credentials (Password: <span className="font-mono text-white bg-black/40 px-1.5 py-0.5 rounded">password123</span>):</span>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          <button
            onClick={() => handleQuickLogin('customer@gastro.com', 'Customer')}
            className="flex items-center gap-1 px-3 py-1 rounded-xl bg-orange-500/20 hover:bg-orange-500/30 border border-orange-500/40 text-orange-200 font-medium transition"
          >
            <UserCheck className="h-3 w-3" /> Customer (customer@gastro.com)
          </button>
          <button
            onClick={() => handleQuickLogin('admin@gastro.com', 'Admin')}
            className="flex items-center gap-1 px-3 py-1 rounded-xl bg-purple-500/20 hover:bg-purple-500/30 border border-purple-500/40 text-purple-200 font-medium transition"
          >
            <Shield className="h-3 w-3" /> Admin (admin@gastro.com)
          </button>
          <button
            onClick={() => handleQuickLogin('owner@gastro.com', 'Restaurant Owner')}
            className="flex items-center gap-1 px-3 py-1 rounded-xl bg-emerald-500/20 hover:bg-emerald-500/30 border border-emerald-500/40 text-emerald-200 font-medium transition"
          >
            <Store className="h-3 w-3" /> Owner (owner@gastro.com)
          </button>
        </div>
      </section>

      {/* ── Hero Banner Section ────────────────────────────────────────────── */}
      <section className="relative overflow-hidden rounded-3xl border border-white/10 bg-gradient-to-b from-white/[0.07] to-white/[0.02] p-8 sm:p-12 backdrop-blur-2xl">
        {/* Glow Spheres */}
        <div className="pointer-events-none absolute -top-20 -left-20 h-72 w-72 rounded-full bg-orange-500/25 blur-3xl" />
        <div className="pointer-events-none absolute -bottom-20 -right-20 h-72 w-72 rounded-full bg-purple-600/20 blur-3xl" />

        <div className="relative z-10 max-w-3xl">
          <div className="inline-flex items-center gap-2 rounded-full border border-orange-500/30 bg-orange-500/10 px-3.5 py-1.5 text-xs font-semibold text-orange-300 backdrop-blur-md mb-6">
            <Sparkles className="h-4 w-4 text-orange-400" />
            <span>Next-Gen Glassmorphic Food Experience</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-black tracking-tight text-white leading-tight">
            Artisanal Cravings, <br />
            <span className="bg-gradient-to-r from-orange-400 via-amber-300 to-orange-500 bg-clip-text text-transparent">
              Delivered in High Definition.
            </span>
          </h1>

          <p className="mt-4 text-sm sm:text-base text-slate-300 leading-relaxed max-w-xl">
            Indulge in award-winning gourmet dishes, chef-crafted recipes, and real-time collaborative group ordering with friends.
          </p>

          <div className="mt-8 flex flex-wrap items-center gap-4">
            <button
              onClick={() => {
                const el = document.getElementById('featured-dishes');
                el?.scrollIntoView({ behavior: 'smooth' });
              }}
              className="flex items-center gap-2 rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 px-6 py-3.5 text-sm font-bold text-white shadow-xl shadow-orange-500/30 hover:scale-105 active:scale-95 transition-all"
            >
              Explore Menu <ArrowRight className="h-4 w-4" />
            </button>

            <button
              onClick={handleStartGroup}
              className="flex items-center gap-2 rounded-2xl border border-emerald-500/40 bg-emerald-500/10 px-6 py-3.5 text-sm font-bold text-emerald-300 backdrop-blur-lg hover:bg-emerald-500/20 active:scale-95 transition-all"
            >
              <Users className="h-4 w-4 text-emerald-400" /> Start Group Order
            </button>
          </div>

          {/* Quick Metrics */}
          <div className="mt-10 grid grid-cols-3 gap-4 border-t border-white/10 pt-6 max-w-md">
            <div>
              <p className="text-xl font-black text-white">25 mins</p>
              <p className="text-xs text-slate-400 flex items-center gap-1 mt-0.5">
                <Clock className="h-3 w-3 text-orange-400" /> Avg Delivery
              </p>
            </div>
            <div>
              <p className="text-xl font-black text-white">4.9 ★</p>
              <p className="text-xs text-slate-400 flex items-center gap-1 mt-0.5">
                <Sparkles className="h-3 w-3 text-amber-400" /> Top Rated
              </p>
            </div>
            <div>
              <p className="text-xl font-black text-white">100%</p>
              <p className="text-xs text-slate-400 flex items-center gap-1 mt-0.5">
                <ShieldCheck className="h-3 w-3 text-emerald-400" /> Fresh Kitchen
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* ── Collaborative Group Cart Callout Banner ──────────────────────── */}
      <section className="relative overflow-hidden rounded-3xl border border-emerald-500/30 bg-emerald-950/20 p-6 sm:p-8 backdrop-blur-xl">
        <div className="flex flex-col md:flex-row items-center justify-between gap-6">
          <div className="space-y-2 text-center md:text-left">
            <div className="inline-flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-emerald-400">
              <Users className="h-4 w-4" /> Team Lunch or Party?
            </div>
            <h3 className="text-2xl font-bold text-white">Collaborative Group Cart</h3>
            <p className="text-xs text-slate-300 max-w-md">
              Order together with colleagues or friends in real time. Everyone adds their own items and bill splits automatically.
            </p>
          </div>

          {/* Join Session Form */}
          <form onSubmit={handleJoinGroup} className="flex items-center gap-2 w-full md:w-auto">
            <input
              type="text"
              placeholder="Enter Code (e.g. GASTRO-8821)"
              value={joinCode}
              onChange={(e) => setJoinCode(e.target.value)}
              className="w-full md:w-56 uppercase rounded-2xl border border-white/15 bg-white/10 px-4 py-2.5 text-xs font-mono text-white placeholder-slate-400 backdrop-blur-lg focus:outline-none focus:border-emerald-400"
            />
            <button
              type="submit"
              className="shrink-0 rounded-2xl bg-emerald-500 px-5 py-2.5 text-xs font-bold text-slate-950 hover:bg-emerald-400 transition shadow-lg shadow-emerald-500/30"
            >
              Join Room
            </button>
          </form>
        </div>
      </section>

      {/* ── Category Pill Filter ────────────────────────────────────────── */}
      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <h2 className="text-xl font-bold text-white flex items-center gap-2">
            <Utensils className="h-5 w-5 text-orange-400" /> Explore by Category
          </h2>
        </div>
        <CategoryPillList
          categories={categories}
          selectedCategory={selectedCategory}
          onSelectCategory={setSelectedCategory}
        />
      </section>

      {/* ── Top Gourmet Restaurants ─────────────────────────────────────── */}
      <section className="space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-tight">
              Featured Kitchens & Restaurants
            </h2>
            <p className="text-xs text-slate-400 mt-1">
              Handpicked fine-dining and artisanal eateries near you
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {restaurants.map((restaurant) => (
            <RestaurantCard key={restaurant.id} restaurant={restaurant} />
          ))}
        </div>
      </section>

      {/* ── Popular Gourmet Dishes Grid ─────────────────────────────────── */}
      <section id="featured-dishes" className="space-y-6">
        <div className="flex items-end justify-between">
          <div>
            <h2 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
              <Flame className="h-6 w-6 text-orange-500 fill-orange-500" />
              Signature Selection ({selectedCategory})
            </h2>
            <p className="text-xs text-slate-400 mt-1">
              Top curated delicacies freshly prepared on demand
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {filteredItems.map((item) => (
            <FoodCard key={item.id} item={item} />
          ))}
        </div>
      </section>

    </div>
  );
};
