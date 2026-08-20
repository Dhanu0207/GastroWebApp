import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  DollarSign, 
  ShoppingBag, 
  Store, 
  Utensils, 
  CheckCircle, 
  Clock, 
  Plus,
  ToggleLeft,
  ToggleRight
} from 'lucide-react';
import { MOCK_MENU_ITEMS, MOCK_RESTAURANTS, MOCK_ACTIVE_ORDER } from '../data/mockData';
import { toast } from 'sonner';

export const AdminDashboardPage: React.FC = () => {
  const [menuItems, setMenuItems] = useState(MOCK_MENU_ITEMS);
  const [activeTab, setActiveTab] = useState<'ORDERS' | 'MENU' | 'RESTAURANTS'>('ORDERS');

  const toggleAvailability = (itemId: number) => {
    setMenuItems((prev) =>
      prev.map((item) =>
        item.id === itemId ? { ...item, isAvailable: !item.isAvailable } : item
      )
    );
    toast.success('Menu item availability updated!');
  };

  return (
    <div className="space-y-8 pb-20 max-w-6xl mx-auto">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 border-b border-white/10 pb-4">
        <div>
          <div className="inline-flex items-center gap-2 rounded-full border border-purple-500/40 bg-purple-500/10 px-3 py-1 text-xs font-semibold text-purple-300 mb-2">
            <LayoutDashboard className="h-3.5 w-3.5" /> Admin Management Hub
          </div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight">
            Kitchen & Platform Operations
          </h1>
        </div>

        {/* Tab Selector */}
        <div className="flex items-center gap-2 rounded-2xl border border-white/10 bg-slate-950/60 p-1 backdrop-blur-xl">
          <button
            onClick={() => setActiveTab('ORDERS')}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
              activeTab === 'ORDERS' ? 'bg-purple-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'
            }`}
          >
            Live Orders
          </button>
          <button
            onClick={() => setActiveTab('MENU')}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
              activeTab === 'MENU' ? 'bg-purple-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'
            }`}
          >
            Menu Inventory
          </button>
          <button
            onClick={() => setActiveTab('RESTAURANTS')}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
              activeTab === 'RESTAURANTS' ? 'bg-purple-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'
            }`}
          >
            Kitchens
          </button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        <div className="rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-semibold uppercase">Total Revenue</span>
            <div className="flex h-8 w-8 items-center justify-center rounded-xl bg-emerald-500/20 text-emerald-400">
              <DollarSign className="h-4 w-4" />
            </div>
          </div>
          <p className="text-2xl font-black text-white font-mono mt-3">₹42,850</p>
          <p className="text-[11px] text-emerald-400 font-medium mt-1">↑ +18.4% this week</p>
        </div>

        <div className="rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-semibold uppercase">Active Orders</span>
            <div className="flex h-8 w-8 items-center justify-center rounded-xl bg-orange-500/20 text-orange-400">
              <ShoppingBag className="h-4 w-4" />
            </div>
          </div>
          <p className="text-2xl font-black text-white font-mono mt-3">14 Live</p>
          <p className="text-[11px] text-orange-400 font-medium mt-1">Avg prep 19 mins</p>
        </div>

        <div className="rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-semibold uppercase">Active Kitchens</span>
            <div className="flex h-8 w-8 items-center justify-center rounded-xl bg-blue-500/20 text-blue-400">
              <Store className="h-4 w-4" />
            </div>
          </div>
          <p className="text-2xl font-black text-white font-mono mt-3">{MOCK_RESTAURANTS.length}</p>
          <p className="text-[11px] text-blue-400 font-medium mt-1">100% Operational</p>
        </div>

        <div className="rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl">
          <div className="flex items-center justify-between">
            <span className="text-xs text-slate-400 font-semibold uppercase">Menu Catalog</span>
            <div className="flex h-8 w-8 items-center justify-center rounded-xl bg-purple-500/20 text-purple-400">
              <Utensils className="h-4 w-4" />
            </div>
          </div>
          <p className="text-2xl font-black text-white font-mono mt-3">{menuItems.length} Dishes</p>
          <p className="text-[11px] text-purple-400 font-medium mt-1">Gourmet verified</p>
        </div>
      </div>

      {/* Tab 1: Live Kitchen Orders */}
      {activeTab === 'ORDERS' && (
        <div className="space-y-4">
          <h2 className="text-lg font-bold text-white flex items-center gap-2">
            <Clock className="h-4 w-4 text-orange-400" /> Active Order Stream
          </h2>

          <div className="overflow-x-auto rounded-3xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl">
            <table className="w-full text-left text-xs text-slate-300">
              <thead>
                <tr className="border-b border-white/10 text-slate-400 uppercase tracking-wider text-[10px]">
                  <th className="p-3">Order #</th>
                  <th className="p-3">Restaurant</th>
                  <th className="p-3">Items</th>
                  <th className="p-3">Amount</th>
                  <th className="p-3">Status</th>
                  <th className="p-3">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-white/5">
                <tr className="hover:bg-white/[0.02]">
                  <td className="p-3 font-mono font-bold text-white">#{MOCK_ACTIVE_ORDER.orderNumber}</td>
                  <td className="p-3">{MOCK_ACTIVE_ORDER.restaurantName}</td>
                  <td className="p-3">2 items (Truffle Burger x2, Fries)</td>
                  <td className="p-3 font-mono font-bold text-orange-400">₹{MOCK_ACTIVE_ORDER.totalAmount}</td>
                  <td className="p-3">
                    <span className="rounded-full bg-amber-500/20 text-amber-300 px-2.5 py-1 text-[10px] font-bold border border-amber-500/30">
                      PREPARING
                    </span>
                  </td>
                  <td className="p-3">
                    <button
                      onClick={() => toast.success('Dispatched to delivery valet!')}
                      className="rounded-xl bg-orange-500 px-3 py-1 text-xs font-semibold text-white hover:bg-orange-600 transition"
                    >
                      Dispatch
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Tab 2: Menu Inventory */}
      {activeTab === 'MENU' && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-bold text-white flex items-center gap-2">
              <Utensils className="h-4 w-4 text-purple-400" /> Menu Catalog & Stock Toggles
            </h2>
            <button
              onClick={() => toast.info('Menu item editor ready for new dishes')}
              className="flex items-center gap-1.5 rounded-xl bg-purple-600 px-3.5 py-1.5 text-xs font-bold text-white hover:bg-purple-500 transition"
            >
              <Plus className="h-3.5 w-3.5" /> Add New Dish
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {menuItems.map((dish) => (
              <div
                key={dish.id}
                className="flex items-center justify-between rounded-2xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl"
              >
                <div className="flex items-center gap-3">
                  <img src={dish.imageUrl} alt={dish.name} className="h-12 w-12 rounded-xl object-cover" />
                  <div>
                    <h4 className="font-bold text-sm text-white line-clamp-1">{dish.name}</h4>
                    <p className="text-xs text-orange-400 font-mono">₹{dish.price}</p>
                  </div>
                </div>

                <button
                  onClick={() => toggleAvailability(dish.id)}
                  className={`flex items-center gap-1 text-xs font-semibold px-2.5 py-1 rounded-xl border transition ${
                    dish.isAvailable
                      ? 'bg-emerald-500/20 text-emerald-300 border-emerald-500/30'
                      : 'bg-red-500/20 text-red-300 border-red-500/30'
                  }`}
                >
                  {dish.isAvailable ? 'In Stock' : 'Out of Stock'}
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Tab 3: Restaurants */}
      {activeTab === 'RESTAURANTS' && (
        <div className="space-y-4">
          <h2 className="text-lg font-bold text-white flex items-center gap-2">
            <Store className="h-4 w-4 text-blue-400" /> Partner Kitchens
          </h2>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {MOCK_RESTAURANTS.map((r) => (
              <div
                key={r.id}
                className="flex items-center justify-between rounded-2xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl"
              >
                <div className="flex items-center gap-3">
                  <img src={r.imageUrl} alt={r.name} className="h-14 w-14 rounded-xl object-cover" />
                  <div>
                    <h4 className="font-bold text-sm text-white">{r.name}</h4>
                    <p className="text-xs text-slate-400">{r.cuisineType} • {r.address}</p>
                  </div>
                </div>
                <span className="rounded-full bg-emerald-500/20 text-emerald-300 px-2.5 py-0.5 text-xs font-semibold border border-emerald-500/30">
                  Active
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

    </div>
  );
};
