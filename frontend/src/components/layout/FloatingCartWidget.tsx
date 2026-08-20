import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { ShoppingBag, ArrowRight, X } from 'lucide-react';
import { useCartStore } from '../../store/cartStore';
import { useGroupCartStore } from '../../store/groupCartStore';

export const FloatingCartWidget: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { items, totalAmount, subtotal } = useCartStore();
  const { session, isInGroupSession } = useGroupCartStore();
  const [minimized, setMinimized] = React.useState(false);

  // Don't show on checkout or full cart page
  if (location.pathname === '/checkout' || location.pathname === '/cart' || items.length === 0) {
    return null;
  }

  if (minimized) {
    return (
      <button
        onClick={() => setMinimized(false)}
        className="fixed bottom-6 right-6 z-50 flex h-14 w-14 items-center justify-center rounded-2xl border border-white/20 bg-gradient-to-tr from-orange-500 to-amber-500 text-white shadow-2xl shadow-orange-500/40 hover:scale-105 transition-transform"
      >
        <ShoppingBag className="h-6 w-6" />
        <span className="absolute -top-1 -right-1 flex h-5 w-5 items-center justify-center rounded-full bg-slate-950 text-[10px] font-bold text-white border border-white/20">
          {items.reduce((s, i) => s + i.quantity, 0)}
        </span>
      </button>
    );
  }

  return (
    <div className="fixed bottom-6 right-6 z-50 w-80 sm:w-96 overflow-hidden rounded-3xl border border-white/15 bg-slate-950/85 p-5 shadow-2xl backdrop-blur-2xl transition-all duration-300 animate-float">
      {/* Ambient background glow inside cart widget */}
      <div className="pointer-events-none absolute -top-12 -right-12 h-32 w-32 rounded-full bg-orange-500/25 blur-2xl" />

      {/* Header */}
      <div className="flex items-center justify-between border-b border-white/10 pb-3">
        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-xl bg-orange-500/20 text-orange-400 border border-orange-500/30">
            <ShoppingBag className="h-4 w-4" />
          </div>
          <div>
            <h4 className="text-sm font-bold text-white">Your Gourmet Cart</h4>
            <p className="text-[11px] text-slate-400">
              {items.length} item{items.length > 1 ? 's' : ''} added
            </p>
          </div>
        </div>
        <button
          onClick={() => setMinimized(true)}
          className="text-slate-400 hover:text-white p-1 rounded-lg hover:bg-white/10 transition"
        >
          <X className="h-4 w-4" />
        </button>
      </div>

      {/* Mini Item List Preview */}
      <div className="my-3 max-h-36 overflow-y-auto space-y-2 pr-1">
        {items.map((item) => (
          <div key={item.menuItemId} className="flex items-center justify-between text-xs py-1">
            <div className="flex items-center gap-2 max-w-[200px]">
              {item.imageUrl && (
                <img src={item.imageUrl} alt={item.menuItemName} className="h-8 w-8 rounded-lg object-cover" />
              )}
              <span className="truncate text-slate-200 font-medium">{item.menuItemName}</span>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-slate-400 font-mono">x{item.quantity}</span>
              <span className="text-white font-mono font-semibold">₹{item.price * item.quantity}</span>
            </div>
          </div>
        ))}
      </div>

      {/* Group Member Stack if Active */}
      {isInGroupSession && session && (
        <div className="mb-3 flex items-center justify-between rounded-xl bg-emerald-500/10 border border-emerald-500/20 p-2 text-xs">
          <span className="text-emerald-300 font-medium">Group Session:</span>
          <div className="flex -space-x-1.5 overflow-hidden">
            {session.members.map((m, idx) => (
              <div
                key={idx}
                className="inline-block h-5 w-5 rounded-full ring-2 ring-slate-900 bg-emerald-600 text-[10px] text-white flex items-center justify-center font-bold"
                title={m.userName}
              >
                {m.userName.charAt(0)}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Footer & CTA */}
      <div className="pt-2 border-t border-white/10 flex items-center justify-between">
        <div>
          <span className="text-[10px] uppercase font-semibold tracking-wider text-slate-400">Total Bill</span>
          <p className="text-lg font-black text-white font-mono leading-none">₹{totalAmount}</p>
        </div>
        <button
          onClick={() => navigate('/cart')}
          className="flex items-center gap-2 rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 px-5 py-2.5 text-xs font-bold text-white shadow-lg shadow-orange-500/40 hover:scale-105 active:scale-95 transition-all"
        >
          View Cart <ArrowRight className="h-3.5 w-3.5" />
        </button>
      </div>
    </div>
  );
};
