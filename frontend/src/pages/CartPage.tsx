import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { 
  ShoppingBag, 
  Trash2, 
  Plus, 
  Minus, 
  ArrowRight, 
  Tag, 
  ArrowLeft, 
  ShieldCheck,
  Building2
} from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { toast } from 'sonner';

export const CartPage: React.FC = () => {
  const navigate = useNavigate();
  const { 
    items, 
    subtotal, 
    deliveryFee, 
    taxAmount, 
    totalAmount, 
    restaurantName,
    updateQuantity, 
    removeItem, 
    clearCart 
  } = useCartStore();

  const [couponCode, setCouponCode] = useState('');
  const [discount, setDiscount] = useState(0);

  const applyCoupon = (e: React.FormEvent) => {
    e.preventDefault();
    if (couponCode.toUpperCase() === 'GASTRO50' || couponCode.toUpperCase() === 'FIRSTBITE') {
      const discountAmount = Math.round(subtotal * 0.15);
      setDiscount(discountAmount);
      toast.success(`Coupon "${couponCode.toUpperCase()}" Applied! You saved ₹${discountAmount}`);
    } else {
      toast.error('Invalid coupon code. Try "GASTRO50"');
    }
  };

  const finalTotal = Math.max(0, totalAmount - discount);

  if (items.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-20 text-center">
        <div className="flex h-24 w-24 items-center justify-center rounded-3xl border border-white/10 bg-white/5 text-orange-400 backdrop-blur-xl shadow-2xl mb-6">
          <ShoppingBag className="h-10 w-10" />
        </div>
        <h2 className="text-2xl font-bold text-white">Your Cart is Empty</h2>
        <p className="text-xs text-slate-400 mt-2 max-w-sm">
          Discover hand-crafted culinary dishes from top artisan kitchens and add them to your order.
        </p>
        <Link
          to="/"
          className="mt-6 flex items-center gap-2 rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 px-6 py-3 text-xs font-bold text-white shadow-lg shadow-orange-500/30 hover:scale-105 transition-all"
        >
          <ArrowLeft className="h-4 w-4" /> Explore Kitchens
        </Link>
      </div>
    );
  }

  return (
    <div className="space-y-8 pb-20 max-w-6xl mx-auto">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 border-b border-white/10 pb-4">
        <div>
          <h1 className="text-3xl font-extrabold text-white tracking-tight flex items-center gap-2">
            <ShoppingBag className="h-7 w-7 text-orange-500" />
            Your Order Cart
          </h1>
          {restaurantName && (
            <p className="text-xs text-slate-400 mt-1 flex items-center gap-1">
              <Building2 className="h-3.5 w-3.5 text-orange-400" /> Ordering from: <span className="text-white font-semibold">{restaurantName}</span>
            </p>
          )}
        </div>
        <button
          onClick={clearCart}
          className="flex items-center gap-1.5 text-xs text-red-400 hover:text-red-300 transition"
        >
          <Trash2 className="h-3.5 w-3.5" /> Empty Cart
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Items List (2 Cols) */}
        <div className="lg:col-span-2 space-y-4">
          {items.map((item) => (
            <div
              key={item.menuItemId}
              className="flex items-center justify-between gap-4 rounded-3xl border border-white/10 bg-slate-950/40 p-4 backdrop-blur-xl transition hover:border-white/20"
            >
              <div className="flex items-center gap-4">
                {item.imageUrl ? (
                  <img
                    src={item.imageUrl}
                    alt={item.menuItemName}
                    className="h-16 w-16 sm:h-20 sm:w-20 rounded-2xl object-cover border border-white/10"
                  />
                ) : (
                  <div className="h-16 w-16 rounded-2xl bg-slate-900 flex items-center justify-center text-slate-600">
                    <ShoppingBag className="h-6 w-6" />
                  </div>
                )}
                <div>
                  <h3 className="font-bold text-white text-sm sm:text-base">{item.menuItemName}</h3>
                  <p className="text-xs text-orange-400 font-mono font-semibold mt-0.5">₹{item.price} each</p>
                </div>
              </div>

              {/* Quantity Selector & Item Total */}
              <div className="flex items-center gap-4 sm:gap-6">
                <div className="flex items-center gap-2 rounded-xl border border-white/10 bg-white/5 p-1 backdrop-blur-md">
                  <button
                    onClick={() => updateQuantity(item.menuItemId, -1)}
                    className="p-1 rounded-lg text-slate-300 hover:bg-white/10 hover:text-white transition"
                  >
                    <Minus className="h-3.5 w-3.5" />
                  </button>
                  <span className="w-6 text-center text-xs font-bold text-white font-mono">{item.quantity}</span>
                  <button
                    onClick={() => updateQuantity(item.menuItemId, 1)}
                    className="p-1 rounded-lg text-slate-300 hover:bg-white/10 hover:text-white transition"
                  >
                    <Plus className="h-3.5 w-3.5" />
                  </button>
                </div>

                <div className="text-right min-w-[70px]">
                  <p className="font-mono font-bold text-white text-base">₹{item.price * item.quantity}</p>
                  <button
                    onClick={() => removeItem(item.menuItemId)}
                    className="text-[10px] text-slate-400 hover:text-red-400 transition mt-0.5"
                  >
                    Remove
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Bill Summary & Checkout (1 Col) */}
        <div className="space-y-6">
          
          {/* Coupon Box */}
          <div className="rounded-3xl border border-white/10 bg-slate-950/40 p-5 backdrop-blur-xl">
            <form onSubmit={applyCoupon} className="flex gap-2">
              <input
                type="text"
                placeholder="Coupon code (GASTRO50)"
                value={couponCode}
                onChange={(e) => setCouponCode(e.target.value)}
                className="w-full rounded-xl border border-white/10 bg-white/5 px-3 py-2 text-xs font-mono uppercase text-white placeholder-slate-400 focus:outline-none focus:border-orange-500"
              />
              <button
                type="submit"
                className="shrink-0 rounded-xl bg-white/10 px-4 py-2 text-xs font-semibold text-white hover:bg-white/20 transition flex items-center gap-1"
              >
                <Tag className="h-3.5 w-3.5 text-orange-400" /> Apply
              </button>
            </form>
          </div>

          {/* Bill Calculation Box */}
          <div className="relative overflow-hidden rounded-3xl border border-white/15 bg-slate-950/60 p-6 backdrop-blur-2xl shadow-2xl">
            <div className="pointer-events-none absolute -top-12 -right-12 h-32 w-32 rounded-full bg-orange-500/20 blur-3xl" />

            <h3 className="text-base font-bold text-white border-b border-white/10 pb-3">Bill Breakdown</h3>

            <div className="space-y-3 py-4 text-xs">
              <div className="flex justify-between text-slate-300">
                <span>Items Subtotal</span>
                <span className="font-mono font-semibold text-white">₹{subtotal}</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Delivery Partner Fee</span>
                <span className="font-mono font-semibold text-white">₹{deliveryFee}</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Govt Taxes & Restaurant GST</span>
                <span className="font-mono font-semibold text-white">₹{taxAmount}</span>
              </div>
              {discount > 0 && (
                <div className="flex justify-between text-emerald-400 font-semibold">
                  <span>Special Promo Discount</span>
                  <span className="font-mono">-₹{discount}</span>
                </div>
              )}
              <div className="border-t border-white/10 pt-3 flex justify-between items-baseline text-sm">
                <span className="font-bold text-white">To Pay (INR)</span>
                <span className="text-xl font-black text-orange-400 font-mono">₹{finalTotal}</span>
              </div>
            </div>

            <button
              onClick={() => navigate('/checkout')}
              className="w-full flex items-center justify-center gap-2 rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 py-3.5 text-sm font-bold text-white shadow-xl shadow-orange-500/40 hover:scale-105 active:scale-95 transition-all cursor-pointer"
            >
              Proceed to Checkout <ArrowRight className="h-4 w-4" />
            </button>

            <div className="mt-4 flex items-center justify-center gap-2 text-[11px] text-slate-400">
              <ShieldCheck className="h-3.5 w-3.5 text-emerald-400" />
              <span>Safe & Encrypted Checkout</span>
            </div>
          </div>

        </div>

      </div>
    </div>
  );
};
