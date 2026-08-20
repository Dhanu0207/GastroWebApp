import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { 
  ShoppingBag, 
  Users, 
  Search, 
  User as UserIcon, 
  LogOut, 
  Compass, 
  Clock, 
  LayoutDashboard,
  Sparkles,
  Key
} from 'lucide-react';
import { useAuthStore } from '../../store/authStore';
import { useCartStore } from '../../store/cartStore';
import { useGroupCartStore } from '../../store/groupCartStore';
import { AuthModal } from '../common/AuthModal';

export const GlassNavbar: React.FC<{ onSearch?: (query: string) => void }> = ({ onSearch }) => {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useAuthStore();
  const { items, totalAmount } = useCartStore();
  const { session, isInGroupSession } = useGroupCartStore();
  const [isAuthOpen, setIsAuthOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const cartItemCount = items.reduce((sum, item) => sum + item.quantity, 0);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value;
    setSearchQuery(val);
    if (onSearch) onSearch(val);
  };

  return (
    <>
      <header className="sticky top-4 z-40 mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <nav className="relative flex items-center justify-between rounded-3xl border border-white/15 bg-slate-950/75 px-5 py-3.5 shadow-2xl backdrop-blur-2xl transition-all duration-300">
          
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2.5 group">
            <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-gradient-to-tr from-orange-500 to-amber-400 text-white font-extrabold shadow-lg shadow-orange-500/30 group-hover:scale-105 transition-transform">
              <Sparkles className="h-5 w-5" />
            </div>
            <span className="text-xl font-black tracking-tight text-white">
              GASTRO<span className="text-orange-500">.</span>
            </span>
          </Link>

          {/* Search Bar */}
          <div className="hidden md:flex relative w-72 lg:w-80 items-center">
            <Search className="absolute left-3.5 h-4 w-4 text-slate-400 pointer-events-none" />
            <input
              type="text"
              placeholder="Search truffle burgers, ramen..."
              value={searchQuery}
              onChange={handleSearchChange}
              className="w-full rounded-2xl border border-white/10 bg-white/[0.05] py-2 pl-10 pr-4 text-xs text-white placeholder-slate-400 backdrop-blur-lg focus:border-orange-500/60 focus:bg-white/[0.08] focus:outline-none transition-all"
            />
          </div>

          {/* Nav Links & Actions */}
          <div className="flex items-center gap-2.5">
            <Link
              to="/"
              className="hidden lg:flex items-center gap-1.5 text-xs font-medium text-slate-300 hover:text-white px-3 py-2 rounded-xl hover:bg-white/5 transition"
            >
              <Compass className="h-4 w-4 text-orange-400" />
              Explore
            </Link>

            <Link
              to="/orders"
              className="hidden sm:flex items-center gap-1.5 text-xs font-medium text-slate-300 hover:text-white px-3 py-2 rounded-xl hover:bg-white/5 transition"
            >
              <Clock className="h-4 w-4 text-amber-400" />
              Orders
            </Link>

            {/* Group Cart Active Pill */}
            {isInGroupSession && session && (
              <Link
                to="/group-cart"
                className="flex items-center gap-2 rounded-xl border border-emerald-500/30 bg-emerald-500/15 px-3 py-1.5 text-xs font-semibold text-emerald-300 backdrop-blur-md hover:bg-emerald-500/25 transition shadow-lg shadow-emerald-950/40 animate-pulse-subtle"
              >
                <Users className="h-3.5 w-3.5 text-emerald-400" />
                <span className="hidden sm:inline">Group:</span>
                <span className="font-mono bg-emerald-950/60 px-1.5 py-0.5 rounded text-[11px] text-emerald-200">
                  {session.sessionCode}
                </span>
              </Link>
            )}

            {/* Cart Button */}
            <Link
              to="/cart"
              className="relative flex items-center gap-2 rounded-xl border border-white/15 bg-white/10 px-3.5 py-2 text-xs font-semibold text-white backdrop-blur-lg hover:bg-white/15 hover:border-white/25 active:scale-95 transition-all"
            >
              <ShoppingBag className="h-4 w-4 text-orange-400" />
              <span className="hidden md:inline font-mono">₹{totalAmount}</span>
              {cartItemCount > 0 && (
                <span className="flex h-5 w-5 items-center justify-center rounded-full bg-orange-500 text-[10px] font-bold text-white shadow-md shadow-orange-500/40">
                  {cartItemCount}
                </span>
              )}
            </Link>

            {/* Admin Portal Link */}
            {user?.role === 'ROLE_ADMIN' && (
              <Link
                to="/admin"
                className="hidden xl:flex items-center gap-1.5 text-xs font-semibold text-purple-300 bg-purple-500/15 border border-purple-500/30 px-3 py-2 rounded-xl hover:bg-purple-500/25 transition"
              >
                <LayoutDashboard className="h-3.5 w-3.5" />
                Admin
              </Link>
            )}

            {/* Demo Credentials Trigger Button */}
            <button
              onClick={() => setIsAuthOpen(true)}
              className="flex items-center gap-1 rounded-xl border border-amber-500/40 bg-amber-500/10 px-3 py-2 text-xs font-semibold text-amber-300 hover:bg-amber-500/20 transition shadow-sm"
              title="View & select demo login credentials"
            >
              <Key className="h-3.5 w-3.5 text-amber-400" />
              <span className="hidden sm:inline">Demo Logins</span>
            </button>

            {/* Auth / Profile Button */}
            {isAuthenticated && user ? (
              <div className="flex items-center gap-1.5">
                <div 
                  onClick={() => navigate('/orders')}
                  className="flex items-center gap-2 rounded-xl border border-white/10 bg-white/5 px-3 py-1.5 cursor-pointer hover:bg-white/10 transition"
                  title="View Profile / Orders"
                >
                  <div className="h-6 w-6 rounded-full bg-gradient-to-tr from-orange-400 to-amber-500 flex items-center justify-center text-xs font-bold text-white">
                    {user.fullName.charAt(0)}
                  </div>
                  <span className="hidden md:inline text-xs font-medium text-slate-200">
                    {user.fullName.split(' ')[0]}
                  </span>
                </div>
                <button
                  onClick={logout}
                  className="p-2 rounded-xl text-slate-400 hover:text-red-400 hover:bg-red-500/10 transition"
                  title="Sign Out"
                >
                  <LogOut className="h-4 w-4" />
                </button>
              </div>
            ) : (
              <button
                onClick={() => setIsAuthOpen(true)}
                className="flex items-center gap-1.5 rounded-xl bg-orange-500 px-3.5 py-2 text-xs font-semibold text-white shadow-lg shadow-orange-500/30 hover:bg-orange-600 active:scale-95 transition-all"
              >
                <UserIcon className="h-3.5 w-3.5" />
                Sign In
              </button>
            )}
          </div>
        </nav>
      </header>

      {/* Auth Modal Trigger */}
      <AuthModal isOpen={isAuthOpen} onClose={() => setIsAuthOpen(false)} />
    </>
  );
};
