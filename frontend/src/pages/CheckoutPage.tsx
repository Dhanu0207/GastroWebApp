import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  CreditCard, 
  MapPin, 
  ShieldCheck, 
  CheckCircle2, 
  ArrowRight, 
  QrCode, 
  Banknote, 
  Home, 
  Briefcase,
  Plus
} from 'lucide-react';
import { useCartStore } from '../store/cartStore';
import { MOCK_SAVED_ADDRESSES } from '../data/mockData';
import { orderApi } from '../api/endpoints';
import { toast } from 'sonner';

export const CheckoutPage: React.FC = () => {
  const navigate = useNavigate();
  const { items, subtotal, deliveryFee, taxAmount, totalAmount, clearCart } = useCartStore();

  const [selectedAddressId, setSelectedAddressId] = useState<number>(1);
  const [paymentMethod, setPaymentMethod] = useState<'ONLINE_RAZORPAY' | 'CARD' | 'UPI' | 'COD'>('ONLINE_RAZORPAY');
  const [isProcessing, setIsProcessing] = useState(false);

  const handlePlaceOrder = async () => {
    if (items.length === 0) {
      toast.error('Your cart is empty!');
      return;
    }

    setIsProcessing(true);
    toast.info('Securing payment gateway & submitting order...');

    try {
      const order = await orderApi.createOrder({
        deliveryAddressId: selectedAddressId,
        paymentMethod,
      });

      setTimeout(() => {
        setIsProcessing(false);
        clearCart();
        toast.success(`Order Placed Successfully! #${order.orderNumber}`);
        navigate(`/order-tracking/${order.orderNumber}`);
      }, 1500);
    } catch {
      setIsProcessing(false);
      toast.error('Failed to place order. Please try again.');
    }
  };

  return (
    <div className="space-y-8 pb-20 max-w-5xl mx-auto">
      
      {/* Title */}
      <div>
        <h1 className="text-3xl font-extrabold text-white tracking-tight">
          Checkout & Finalize
        </h1>
        <p className="text-xs text-slate-400 mt-1">
          Select delivery location and preferred payment method
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Left Column (2 Cols): Address & Payment Selector */}
        <div className="lg:col-span-2 space-y-8">
          
          {/* 1. Delivery Address */}
          <section className="space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-base font-bold text-white flex items-center gap-2">
                <MapPin className="h-4 w-4 text-orange-400" />
                1. Select Delivery Address
              </h2>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              {MOCK_SAVED_ADDRESSES.map((addr) => {
                const isSelected = selectedAddressId === addr.id;
                return (
                  <div
                    key={addr.id}
                    onClick={() => setSelectedAddressId(addr.id || 1)}
                    className={`relative cursor-pointer overflow-hidden rounded-3xl border p-5 backdrop-blur-xl transition-all duration-200 ${
                      isSelected
                        ? 'border-orange-500/80 bg-orange-500/10 shadow-lg shadow-orange-500/20'
                        : 'border-white/10 bg-slate-950/40 hover:border-white/20'
                    }`}
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        {addr.tag === 'Home' ? (
                          <Home className="h-4 w-4 text-orange-400" />
                        ) : (
                          <Briefcase className="h-4 w-4 text-amber-400" />
                        )}
                        <span className="font-bold text-sm text-white">{addr.tag}</span>
                      </div>
                      {isSelected && <CheckCircle2 className="h-4 w-4 text-orange-400" />}
                    </div>

                    <p className="mt-2 text-xs text-slate-300 leading-relaxed font-normal">{addr.street}</p>
                    <p className="text-[11px] text-slate-400 mt-1 font-mono">
                      {addr.city}, {addr.postalCode}
                    </p>
                  </div>
                );
              })}
            </div>
          </section>

          {/* 2. Payment Method */}
          <section className="space-y-4">
            <h2 className="text-base font-bold text-white flex items-center gap-2">
              <CreditCard className="h-4 w-4 text-emerald-400" />
              2. Payment Options
            </h2>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              
              {/* Razorpay Instant */}
              <div
                onClick={() => setPaymentMethod('ONLINE_RAZORPAY')}
                className={`relative cursor-pointer overflow-hidden rounded-3xl border p-5 backdrop-blur-xl transition-all ${
                  paymentMethod === 'ONLINE_RAZORPAY'
                    ? 'border-emerald-500/80 bg-emerald-500/10 shadow-lg shadow-emerald-500/20'
                    : 'border-white/10 bg-slate-950/40 hover:border-white/20'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <span className="flex h-7 w-7 items-center justify-center rounded-lg bg-blue-500/20 text-blue-400 font-bold text-xs">
                      RZ
                    </span>
                    <div>
                      <h4 className="text-sm font-bold text-white">Razorpay Gateway</h4>
                      <p className="text-[10px] text-slate-400">Cards, Netbanking & UPI</p>
                    </div>
                  </div>
                  {paymentMethod === 'ONLINE_RAZORPAY' && <CheckCircle2 className="h-4 w-4 text-emerald-400" />}
                </div>
              </div>

              {/* UPI Instant QR */}
              <div
                onClick={() => setPaymentMethod('UPI')}
                className={`relative cursor-pointer overflow-hidden rounded-3xl border p-5 backdrop-blur-xl transition-all ${
                  paymentMethod === 'UPI'
                    ? 'border-emerald-500/80 bg-emerald-500/10 shadow-lg shadow-emerald-500/20'
                    : 'border-white/10 bg-slate-950/40 hover:border-white/20'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <QrCode className="h-5 w-5 text-purple-400" />
                    <div>
                      <h4 className="text-sm font-bold text-white">Instant UPI</h4>
                      <p className="text-[10px] text-slate-400">Google Pay, PhonePe, Paytm</p>
                    </div>
                  </div>
                  {paymentMethod === 'UPI' && <CheckCircle2 className="h-4 w-4 text-emerald-400" />}
                </div>
              </div>

              {/* Credit / Debit Card */}
              <div
                onClick={() => setPaymentMethod('CARD')}
                className={`relative cursor-pointer overflow-hidden rounded-3xl border p-5 backdrop-blur-xl transition-all ${
                  paymentMethod === 'CARD'
                    ? 'border-emerald-500/80 bg-emerald-500/10 shadow-lg shadow-emerald-500/20'
                    : 'border-white/10 bg-slate-950/40 hover:border-white/20'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <CreditCard className="h-5 w-5 text-amber-400" />
                    <div>
                      <h4 className="text-sm font-bold text-white">Credit / Debit Card</h4>
                      <p className="text-[10px] text-slate-400">Visa, Mastercard, RuPay</p>
                    </div>
                  </div>
                  {paymentMethod === 'CARD' && <CheckCircle2 className="h-4 w-4 text-emerald-400" />}
                </div>
              </div>

              {/* Cash On Delivery */}
              <div
                onClick={() => setPaymentMethod('COD')}
                className={`relative cursor-pointer overflow-hidden rounded-3xl border p-5 backdrop-blur-xl transition-all ${
                  paymentMethod === 'COD'
                    ? 'border-emerald-500/80 bg-emerald-500/10 shadow-lg shadow-emerald-500/20'
                    : 'border-white/10 bg-slate-950/40 hover:border-white/20'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <Banknote className="h-5 w-5 text-emerald-400" />
                    <div>
                      <h4 className="text-sm font-bold text-white">Cash on Delivery</h4>
                      <p className="text-[10px] text-slate-400">Pay when food arrives</p>
                    </div>
                  </div>
                  {paymentMethod === 'COD' && <CheckCircle2 className="h-4 w-4 text-emerald-400" />}
                </div>
              </div>

            </div>
          </section>

        </div>

        {/* Right Column (1 Col): Order Summary */}
        <div className="space-y-6">
          <div className="relative overflow-hidden rounded-3xl border border-white/15 bg-slate-950/60 p-6 backdrop-blur-2xl shadow-2xl">
            <div className="pointer-events-none absolute -top-12 -right-12 h-32 w-32 rounded-full bg-orange-500/20 blur-3xl" />

            <h3 className="text-base font-bold text-white border-b border-white/10 pb-3">Order Summary</h3>

            {/* Items mini review */}
            <div className="py-3 space-y-2 border-b border-white/10 max-h-48 overflow-y-auto pr-1">
              {items.map((i) => (
                <div key={i.menuItemId} className="flex justify-between text-xs text-slate-300">
                  <span className="truncate max-w-[170px]">{i.menuItemName} x{i.quantity}</span>
                  <span className="font-mono font-semibold text-white">₹{i.price * i.quantity}</span>
                </div>
              ))}
            </div>

            {/* Pricing */}
            <div className="space-y-2.5 py-4 text-xs border-b border-white/10">
              <div className="flex justify-between text-slate-300">
                <span>Subtotal</span>
                <span className="font-mono text-white font-semibold">₹{subtotal}</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Delivery Fee</span>
                <span className="font-mono text-white font-semibold">₹{deliveryFee}</span>
              </div>
              <div className="flex justify-between text-slate-300">
                <span>Taxes (GST)</span>
                <span className="font-mono text-white font-semibold">₹{taxAmount}</span>
              </div>
              <div className="pt-2 flex justify-between items-baseline text-sm">
                <span className="font-bold text-white">Grand Total</span>
                <span className="text-2xl font-black text-orange-400 font-mono">₹{totalAmount}</span>
              </div>
            </div>

            {/* Place Order CTA */}
            <button
              disabled={isProcessing}
              onClick={handlePlaceOrder}
              className="mt-4 w-full flex items-center justify-center gap-2 rounded-2xl bg-gradient-to-r from-orange-500 to-amber-500 py-3.5 text-sm font-bold text-white shadow-xl shadow-orange-500/40 hover:scale-105 active:scale-95 transition-all cursor-pointer disabled:opacity-50"
            >
              {isProcessing ? 'Processing Order...' : 'Pay & Place Order'} <ArrowRight className="h-4 w-4" />
            </button>

            <div className="mt-4 flex items-center justify-center gap-2 text-[11px] text-slate-400">
              <ShieldCheck className="h-3.5 w-3.5 text-emerald-400" />
              <span>100% Secure Spring Boot Backend</span>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};
