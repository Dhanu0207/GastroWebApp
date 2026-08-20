import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  CheckCircle, 
  Phone, 
  MapPin, 
  Clock, 
  Bike, 
  ChevronLeft, 
  ShoppingBag, 
  RefreshCw,
  Sparkles
} from 'lucide-react';
import { Order, OrderStatus } from '../types';
import { orderApi } from '../api/endpoints';
import { LiveOrderStepper } from '../components/order/LiveOrderStepper';
import { MOCK_ACTIVE_ORDER } from '../data/mockData';
import { toast } from 'sonner';

const STATUS_SEQUENCE: OrderStatus[] = [
  'PLACED',
  'CONFIRMED',
  'PREPARING',
  'OUT_FOR_DELIVERY',
  'DELIVERED',
];

export const OrderTrackingPage: React.FC = () => {
  const { orderId } = useParams<{ orderId: string }>();
  const navigate = useNavigate();
  const [order, setOrder] = useState<Order>(MOCK_ACTIVE_ORDER);
  const [currentStatusIndex, setCurrentStatusIndex] = useState(2); // 'PREPARING'

  useEffect(() => {
    const fetchOrder = async () => {
      if (orderId) {
        const data = await orderApi.getOrderById(orderId);
        setOrder(data);
      }
    };
    fetchOrder();
  }, [orderId]);

  const currentStatus = STATUS_SEQUENCE[currentStatusIndex];

  // Simulator for user demo
  const handleAdvanceStatus = () => {
    if (currentStatusIndex < STATUS_SEQUENCE.length - 1) {
      const nextIndex = currentStatusIndex + 1;
      setCurrentStatusIndex(nextIndex);
      toast.success(`Order status updated to: ${STATUS_SEQUENCE[nextIndex]}`);
    } else {
      setCurrentStatusIndex(0);
      toast.info('Resetting tracking status simulator');
    }
  };

  return (
    <div className="space-y-8 pb-20 max-w-4xl mx-auto">
      
      {/* Back to Home */}
      <button
        onClick={() => navigate('/')}
        className="flex items-center gap-1.5 text-xs font-semibold text-slate-400 hover:text-white transition"
      >
        <ChevronLeft className="h-4 w-4" /> Back to Discovery
      </button>

      {/* Main Glass HUD Container */}
      <div className="relative overflow-hidden rounded-3xl border border-white/15 bg-slate-950/70 p-6 sm:p-10 backdrop-blur-2xl shadow-2xl">
        
        {/* Glow Spheres */}
        <div className="pointer-events-none absolute -top-24 -left-24 h-64 w-64 rounded-full bg-orange-500/20 blur-3xl" />
        <div className="pointer-events-none absolute -bottom-24 -right-24 h-64 w-64 rounded-full bg-emerald-500/15 blur-3xl" />

        {/* Order Header */}
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 border-b border-white/10 pb-6">
          <div>
            <div className="flex items-center gap-2">
              <span className="flex h-2 w-2 rounded-full bg-emerald-400 animate-ping" />
              <span className="text-xs font-mono font-semibold uppercase tracking-wider text-emerald-300">
                Live Order Tracking
              </span>
            </div>
            <h1 className="text-2xl sm:text-3xl font-extrabold text-white tracking-tight mt-1">
              Order #{order.orderNumber}
            </h1>
            <p className="text-xs text-slate-400 mt-1">
              From <span className="text-orange-400 font-semibold">{order.restaurantName}</span>
            </p>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={handleAdvanceStatus}
              className="flex items-center gap-1.5 rounded-xl border border-orange-500/30 bg-orange-500/10 px-3.5 py-2 text-xs font-semibold text-orange-300 hover:bg-orange-500/20 transition"
              title="Simulate next status step"
            >
              <RefreshCw className="h-3.5 w-3.5" /> Simulate Step
            </button>
          </div>
        </div>

        {/* Live Stepper HUD */}
        <div className="py-6 border-b border-white/10">
          <LiveOrderStepper currentStatus={currentStatus} />
        </div>

        {/* Delivery Partner Info & ETA */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 py-6 border-b border-white/10">
          
          {/* Estimated Time Card */}
          <div className="rounded-2xl border border-white/10 bg-white/[0.03] p-4 flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-orange-500/20 text-orange-400 border border-orange-500/30">
              <Clock className="h-6 w-6" />
            </div>
            <div>
              <span className="text-[10px] uppercase font-semibold text-slate-400">Estimated Delivery</span>
              <h4 className="text-xl font-bold text-white">
                {currentStatus === 'DELIVERED' ? 'Arrived!' : '18 - 24 Mins'}
              </h4>
              <p className="text-[11px] text-emerald-400 font-medium">On Schedule</p>
            </div>
          </div>

          {/* Valet / Driver Card */}
          <div className="rounded-2xl border border-white/10 bg-white/[0.03] p-4 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-emerald-500/20 text-emerald-400 border border-emerald-500/30">
                <Bike className="h-6 w-6" />
              </div>
              <div>
                <span className="text-[10px] uppercase font-semibold text-slate-400">Delivery Valet</span>
                <h4 className="text-sm font-bold text-white">Rahul Verma</h4>
                <p className="text-[11px] text-slate-400">★ 4.9 (1,240 Deliveries)</p>
              </div>
            </div>

            <button
              onClick={() => toast.info('Calling Rahul (+91 98765 00000)...')}
              className="flex h-9 w-9 items-center justify-center rounded-xl bg-white/10 text-slate-300 hover:bg-emerald-500 hover:text-white transition"
              title="Call Delivery Partner"
            >
              <Phone className="h-4 w-4" />
            </button>
          </div>

        </div>

        {/* Order Details & Summary */}
        <div className="pt-6 space-y-4">
          <h3 className="text-sm font-bold text-white flex items-center gap-2">
            <ShoppingBag className="h-4 w-4 text-orange-400" /> Items in this Order
          </h3>

          <div className="space-y-2">
            {order.items.map((item, idx) => (
              <div key={idx} className="flex justify-between items-center text-xs py-1.5 border-b border-white/5">
                <span className="text-slate-300 font-medium">
                  {item.menuItemName} <span className="text-slate-400 font-mono">x{item.quantity}</span>
                </span>
                <span className="text-white font-mono font-bold">₹{item.price * item.quantity}</span>
              </div>
            ))}
          </div>

          <div className="flex justify-between items-baseline pt-2">
            <span className="text-xs text-slate-400">
              Paid via {order.paymentMethod} • Taxes & Delivery Included
            </span>
            <span className="text-lg font-black text-orange-400 font-mono">₹{order.totalAmount}</span>
          </div>
        </div>

      </div>
    </div>
  );
};
